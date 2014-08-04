/**
 * 
 */
package com.cpcrew.instadine.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.activities.MapActivity;
import com.cpcrew.instadine.adapters.ImageAdapter;
import com.cpcrew.instadine.adapters.RestaurantArrayAdapter;
import com.cpcrew.instadine.models.LoggedInUser;
import com.cpcrew.instadine.models.Rest;
import com.cpcrew.instadine.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

/**
 * @author raji
 * 
 */
public class RestarauntListFragment extends Fragment {

	private static String TAG = RestarauntListFragment.class.getSimpleName();
	private String selectedRestaurant = null;

	private RefreshListener refreshListener;

	private HashMap<String, Rest> Restaurants;
	private HashMap<String, Rest> restMap;
	private HashSet<String> mySelection;
	private HashSet<String> prevSelection;
	private HashSet<String> removePrevSelections;
	private ArrayList<Rest> restaurants;
	private RestaurantArrayAdapter restAdapter;
	protected PullToRefreshListView lvRestaurants;
	private HashMap<String, String> groupUsersFacebookIds;

	// private Button btnSearch;

	Rest userChoice;

	public interface RefreshListener {
		public void onParentRefresh();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// initialize
		restaurants = new ArrayList<Rest>();
		mySelection = new HashSet<String>();
		Restaurants = new HashMap<String, Rest>();
		prevSelection = new HashSet<String>();
		restMap = new HashMap<String, Rest>();
		groupUsersFacebookIds = new HashMap<String, String>();
		refreshListener = (RefreshListener) getActivity();
		restAdapter = new RestaurantArrayAdapter(getActivity(), restaurants);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_rest, container, false);
		lvRestaurants = (PullToRefreshListView) v
				.findViewById(R.id.lvRestaurants);

		lvRestaurants.setAdapter(restAdapter);
		// btnSearch = (Button) v.findViewById(R.id.btnSearch);
		// btnSearch.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// callSearchActivity();
		// }
		//
		// });

		// TODO Cannot refresh the activity Should change to refresh only
		// restaraunts
		lvRestaurants.setOnRefreshListener(new OnRefreshListener() {
			// doesn't work anymore

			@Override
			public void onRefresh() {
				refreshListener.onParentRefresh();
				lvRestaurants.onRefreshComplete();

			}
		});

		onRestaurantSelected();
		return v;
	}

	public void setFacebookIds(HashMap<String, String> fbMap) {
		groupUsersFacebookIds.clear();
		groupUsersFacebookIds.putAll(fbMap);
	}

	public ArrayList<Rest> getRestarauntsArray() {
		return restaurants;
	}

	public void addMySelection(String restId) {
		mySelection.add(restId);

	}

	public void addMyPrevSelection(String restId) {
		prevSelection.add(restId);
	}

	public int getCount() {
		return restAdapter.getCount();
	}

	public void callSearchActivity() {
		HashMap<String, Integer> restCount = new HashMap<String, Integer>();
		for (Rest rest : restaurants) {
			// srestMap.put(rest.getRestaurant().getId(), rest.getRestaurant());
			restCount.put(rest.getId(), rest.getCount());
		}
		Intent i = new Intent(getActivity(), MapActivity.class);
		i.putExtra("rest_map", restMap);
		i.putExtra("rest_count", restCount);
		i.putExtra("user_fb_map", groupUsersFacebookIds);
		startActivityForResult(i, Constants.MAP_REQUEST_CODE);
		getActivity().overridePendingTransition(R.anim.right_in,
				R.anim.left_out);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// REQUEST_CODE is defined above
		if (resultCode == Activity.RESULT_OK
				&& requestCode == Constants.MAP_REQUEST_CODE) {

			// User selected choice from MapActivity
			userChoice = (Rest) data.getSerializableExtra("user_choice");

			if (userChoice != null) {
				// Refresh the Restaurant ListView
				addRestaurantWithBusinessInfo(userChoice.getId(), LoggedInUser
						.getcurrentUser().getId(), userChoice);
			} else {
				System.out.println(" Returned null from Search Activity");
			}
			selectedRestaurant = userChoice.getName();

		}
	}

	public void onRestaurantSelected() {
		lvRestaurants.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				LinearLayout llCount = (LinearLayout) view
						.findViewById(R.id.llCount);
				Rest listItem = (Rest) lvRestaurants
						.getItemAtPosition(position + 1);
				// Is the Restaurant already selected
				boolean removeSelection = false;
				for (String cs : mySelection) {
					if (listItem.getRestId().equals(cs))
						removeSelection = true;
				}
				if (removeSelection == false) {
					addRestaurantSelection(listItem);
					if (llCount != null)
						restAdapter.flipit(llCount);
				} else
					removeRestaruantSelection(listItem);

			}
		});
	}

	// Remove adding support
	// Handles corner cases - When the votes reduce to zero, removes from the Rest ListView
	public void removeRestaruantSelection(Rest restaurant) {
		System.out.println("Removing restaurant not supported");
		mySelection.remove(restaurant.getRestId());
		for (Rest rt : restaurants) {
			if (rt.getName().equals(restaurant.getName())) {
				rt.removeUser(LoggedInUser.getcurrentUser().getId());
				rt.removeGroupUser(LoggedInUser.getcurrentUser().getId());
				if (rt.getCount() == 0)
					restaurants.remove(rt);
				break;
			}
		}
		
		// reflect in the adpater
		restAdapter.notifyDataSetChanged();

	}

	public void addRestaurantSelection(Rest restaurant) {
		mySelection.add(restaurant.getRestId());
		boolean isActionDone = false;
		for (Rest rt : restaurants) {
			if (rt.getRestId().equals(restaurant.getRestId())) {
				rt.addUser(LoggedInUser.getcurrentUser().getId());
				restaurant.addGroupUser(LoggedInUser.getcurrentUser().getId(),
						LoggedInUser.getcurrentUser().getFacebookId());
				isActionDone = true;
			}
		}
		if (isActionDone == false) {
			restaurants.add(restaurant);
			restaurant.addUser(LoggedInUser.getcurrentUser().getId());
			restaurant.addGroupUser(LoggedInUser.getcurrentUser().getId(),
					LoggedInUser.getcurrentUser().getFacebookId());
		}
		restAdapter.notifyDataSetChanged();
	}

	public void addRestaurant(String restId, String userId) {
		Rest rest;
		// if (!mySelection.contains(restId)) { // user already selected
		// restaurant BUG FIX: Doesn't update if another user has made the same
		// selection as you )

		if (Restaurants.containsKey(restId)) { // preexisting
			rest = Restaurants.get(restId);
		} else { // new restaurant
			rest = new Rest();
			rest.setRestId(restId);
			Restaurants.put(rest.getRestId(), rest);
			restaurants.add(rest);

		}
		rest.addUser(userId);
		assert (groupUsersFacebookIds != null);
		rest.addGroupUser(userId, groupUsersFacebookIds.get(userId)); // for
																		// images
		// }
	}

	public void addRestaurantWithBusinessInfo(String restId, String userId,
			Rest restInfo) {

		// if (!mySelection.contains(restId)) { // user already selected
		// restaurant ( BUG FIX: Doesn't update if another user has made the
		// same selection as you )
		Rest rest = null;
		restInfo.addUser(userId);
		restInfo.addGroupUser(userId, groupUsersFacebookIds.get(userId)); // for
																			// images
		if (Restaurants.containsKey(restId)) { // preexisting restaurant
			rest = Restaurants.get(restId);
		} else { // new restaurant
			restMap.put(restInfo.getId(), restInfo);
			Restaurants.put(restInfo.getId(), restInfo);
			restaurants.add(restInfo);
		}
		mySelection.add(restId);
		restAdapter.notifyDataSetChanged();
		// }
	}

	public ArrayList<String> newSelections() {
		Set<String> symmetricDiff = new HashSet<String>(mySelection);
		symmetricDiff.addAll(prevSelection);
		// symmetricDiff now contains the union
		Set<String> tmp = new HashSet<String>(mySelection);
		tmp.retainAll(prevSelection);
		// tmp now contains the intersection
		symmetricDiff.removeAll(tmp);
		// union minus intersection equals symmetric-difference
		ArrayList<String> list = new ArrayList<String>(symmetricDiff);
		System.out.println("prev Selection " + prevSelection.size());
		System.out.println("my selections " + mySelection.size());
		System.out.println("Num selections " + symmetricDiff.size());
		return list;
	}
	
	public ArrayList<String> removeSelections() {
		Set<String> tmp = new HashSet<String>(mySelection);
		ArrayList<String> removeSelectionsList = new ArrayList<String>();
		for( String resId : prevSelection) {
			if (!tmp.contains(resId)) 
				removeSelectionsList.add(resId);
		}
		return removeSelectionsList;
	}

	public void populateBusinessInfo() {
		// Given a list of restaurant IDs return business Objects
		// Populate the restaurants with the business info
		// addRestaurantSelection

		for (final Rest res : restaurants) {
			final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
			final String TYPE_DETAILS = "/details";
			final String OUT_JSON = "/json";
			final String API_KEY = com.cpcrew.instadine.utils.Constants.GOOGLE_PLACES_API_KEY;
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS
					+ OUT_JSON);
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("key", API_KEY);
			params.put("placeid", res.getRestId());
			client.get(sb.toString(), params, new JsonHttpResponseHandler() {

				public void onSuccess(JSONObject response) {

					try {
						Rest r = new Rest();
						JSONObject jsonObject = response
								.getJSONObject("result");
						r = Rest.fromDetailJson(jsonObject);
						Log.d("debug",
								"Business Object in Voting: " + r.toString());
						res.inflateRestaurantDetails(r);
						restMap.put(res.getId(), res); // for the Search
														// Activity
						restAdapter.notifyDataSetChanged(); // Notify the
															// ListView
						// updateDeciderView(); TODO has to be a callback

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};

				@Override
				public void onFailure(Throwable e, String s) {
					Log.d("ERROR", e.toString());
				}
			});
		}
	}

	// currently does not handle ties
	public Rest highestVotedRestaraunt() {
		int votes = 0;
		Rest restarauntSelected = null;
		for (Rest rest : restaurants) {
			if (rest.getCount() > votes) {
				votes = rest.getCount();
				return rest;
			}
		}
		return restarauntSelected;
	}

}
