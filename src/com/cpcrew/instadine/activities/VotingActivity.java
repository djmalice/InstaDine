package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.adapters.RestaurantArrayAdapter;
import com.cpcrew.instadine.api.ParseEventsApi;
import com.cpcrew.instadine.api.ParseEventsApi.ParseEventApiListener;
import com.cpcrew.instadine.models.Business;
import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.LoggedInUser;
import com.cpcrew.instadine.models.Rest;
import com.cpcrew.instadine.models.Restaurant;
import com.cpcrew.instadine.utils.Constants;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class VotingActivity extends FragmentActivity implements ParseEventApiListener, 
	CalendarDatePickerDialog.OnDateSetListener, RadialTimePickerDialog.OnTimeSetListener {
	
	private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
	private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
 
	Business userChoice;
	private HashMap<String,Rest> Restaurants; 

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {        	
	    	Toast.makeText(getApplicationContext(), "onReceive invoked!", Toast.LENGTH_LONG).show();
	    }
	};
	
	private TextView tvDateSelected;
	private Button btnDateSelect;
	private Button btnTimeSelect;
	EditText etLocation;
	TextView tvDate ;
	TextView tvTime; 
	
	private String timeOfEvent;
	private String dateOfEvent;
	
	private ParseEventsApi parseEventApi;
	private Event currentEvent;
	private Group currentGroup;
	private String groupId = null;
	private HashMap<String, Business> restMap;
	private HashSet<String> mySelection;
	private HashSet<String> prevSelection;
	private ArrayList<Rest> restaurants;
	
	private RestaurantArrayAdapter restAdapter;
	protected PullToRefreshListView lvRestaurants;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		if ( groupId == null)
			groupId = getIntent().getStringExtra("group_id");
		setContentView(R.layout.activity_voting);
		lvRestaurants = (PullToRefreshListView) findViewById(R.id.lvRestaurants);
		
		//initialize
		restaurants = new ArrayList<Rest>();

		mySelection = new HashSet<String>();
		Restaurants = new HashMap<String, Rest>();
		prevSelection = new HashSet<String>();
		restMap = new HashMap<String, Business>();

		parseEventApi = new ParseEventsApi(this);
		
		// Fetch the Event
		findEvent(groupId);
		
		restAdapter = new RestaurantArrayAdapter(this, restaurants);
		lvRestaurants.setAdapter(restAdapter);
		lvRestaurants.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				findEvent(groupId);
				lvRestaurants.onRefreshComplete();
				
			}
		});
		onRestaurantSelected();		

        // Specify an Activity to handle all pushes by default
		PushService.setDefaultPushCallback(this, VotingActivity.class);
	}

	
	// pushNotifications onPause routine
	@Override
    public void onPause() {
        super.onPause();
       LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
    
	
	// had created this picker before we decided to use a meal button (breakfast, lunch, dinner)
	// leaving code here for now...
	public void createTimePicker() {
		
		btnTimeSelect = (Button) findViewById(R.id.btnTimeSelect);
		
		btnTimeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DateTime now = DateTime.now();
                RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
                        .newInstance(VotingActivity.this, now.getHourOfDay(), now.getMinuteOfHour(),
                                DateFormat.is24HourFormat(VotingActivity.this));
                timePickerDialog.setThemeDark(true);
                timePickerDialog.show(fm, FRAG_TAG_TIME_PICKER);
            }
        });
	}
	

    @Override
    public void onTimeSet(RadialTimePickerDialog dialog, int hourOfDay, int minute) {
    	if (hourOfDay==0){
    		hourOfDay=12;
    	}    	
    	if (hourOfDay > 12) { 
    		hourOfDay = hourOfDay - 12;
    		if (minute < 10) {
    			btnTimeSelect.setText("" + hourOfDay + ":0" + minute + "PM");
    			timeOfEvent = hourOfDay + ":0" + minute + "PM";
    		}
    		else {
    			btnTimeSelect.setText("" + hourOfDay + ":" + minute + "PM");
    			timeOfEvent = hourOfDay + ":" + minute + "PM";
    		}
    	}
    	else {
    		if (minute < 10) {
    			btnTimeSelect.setText("" + hourOfDay + ":0" + minute + "AM");
    			timeOfEvent = hourOfDay + ":0" + minute + "AM";
    		}
    		else {
    			btnTimeSelect.setText("" + hourOfDay + ":" + minute + "AM");
    			timeOfEvent = hourOfDay + ":" + minute + "AM";
    		}    		
    	}
    	 
    }

	public void createDatePicker() {
  
        btnDateSelect.setText("Set Date");
        
        btnDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DateTime now = DateTime.now();
                CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                        .newInstance(VotingActivity.this, now.getYear(), now.getMonthOfYear() - 1,
                                now.getDayOfMonth());
                calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
            }
        });
	}
	
    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
    	btnDateSelect.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
    	dateOfEvent = monthOfYear + "/" + dayOfMonth + "/" + year;
    }
    
    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        
        // pushNotifications onResume routine, calls the custom defined VotingActivityReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(VotingActivityReceiver.intentAction));
        
        CalendarDatePickerDialog calendarDatePickerDialog = (CalendarDatePickerDialog) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG_DATE_PICKER);
        if (calendarDatePickerDialog != null) {
            calendarDatePickerDialog.setOnDateSetListener(this);
        }
        
        RadialTimePickerDialog rtpd = (RadialTimePickerDialog) getSupportFragmentManager().findFragmentByTag(
                FRAG_TAG_TIME_PICKER);
        if (rtpd != null) {
            rtpd.setOnTimeSetListener(this);
        }        
    }    
	
	public void callSearchActivity(View v){
		
		HashMap<String, Integer> restCount = new HashMap<String, Integer>();
		for (Rest rest : restaurants) {
			//srestMap.put(rest.getRestaurant().getId(), rest.getRestaurant());
			restCount.put(rest.getId(), rest.getCount());
		}
		Intent i = new Intent(VotingActivity.this, MapActivity.class);
		i.putExtra("rest_map" , restMap);
		i.putExtra("rest_count",restCount);
		startActivityForResult(i,Constants.MAP_REQUEST_CODE);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // REQUEST_CODE is defined above
	  if (resultCode == RESULT_OK && requestCode == Constants.MAP_REQUEST_CODE) {

		  // User selected choice from MapActivity
		  userChoice = (Business)data.getSerializableExtra("user_choice");
	     
		  if ( userChoice != null ) {
	     // Refresh the Restaurant ListView
	      addRestaurantWithBusinessInfo(userChoice.getId(), 
	    		  LoggedInUser.getcurrentUser().getId(), userChoice);
		  } else {
			  System.out.println(" Returned null from Search Activity");
		  }

	  }
	} 
	
	public void populateBusinessInfo() {
		// Given a list of restaurant IDs return business Objects
		// Populate the restaurants with the business info
		// addRestaurantSelection
		
		for (final Rest res : restaurants) {
			final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
			final String TYPE_DETAILS = "/details";
			final String OUT_JSON = "/json";
			final String API_KEY = "AIzaSyDUWcjTHpCoU93QIFWHJ_glTbd4wfiP8bw";
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS
					+ OUT_JSON);
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("key", API_KEY);
			params.put("placeid", res.getRestId());
			client.get(sb.toString(), params, new JsonHttpResponseHandler() {

				public void onSuccess(JSONObject response) {

					try {
						Business b =new Business();
						JSONObject jsonObject = response
								.getJSONObject("result");
						b = Business.fromDetailJson(jsonObject);
						Log.d("debug",
								"Business Object in Voting: "
										+ b.toString());
						res.inflateBusinessObject(b);  // We have the business info now !!
						restMap.put(b.getId(), b) ; // for the Search Activity
						restAdapter.notifyDataSetChanged(); // Notify the ListView
						
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
		
	public void onRestaurantSelected() {
		lvRestaurants.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				Rest listItem = (Rest) lvRestaurants.getItemAtPosition(position);
				// Is the Restaurant already selected
				boolean removeSelection = false;
				for (String cs : mySelection) {
					if (listItem.getRestId().equals(cs)) 
						removeSelection = true;
					
				}
				if ( removeSelection == false )
					addRestaurantSelection(listItem);
				else
					removeRestaruantSelection(listItem);
				
			}
		});
	}

	// Remove not supported in this version
	public void removeRestaruantSelection(Rest restaurant) {
		System.out.println("Removing restaurant not supported");
//		mySelection.remove(restaurant.getRestId());
//		for ( Rest rt : restaurants){
//			if ( rt.getRestName().equals(restaurant.getRestName()) ){
//				rt.removeUser(LoggedInUser.getcurrentUser().getId());
//			}
//		}
//		// reflect in the adpater
//		restAdapter.notifyDataSetChanged();
		
	}
	
	public void addRestaurantSelection(Rest restaurant) {
		mySelection.add(restaurant.getRestId());
		boolean isActionDone = false;
		for ( Rest rt : restaurants){
			if ( rt.getRestId().equals(restaurant.getRestId()) ){
				rt.addUser(LoggedInUser.getcurrentUser().getId());
				isActionDone = true;
			}
		}
		if ( isActionDone == false) {
			restaurants.add(restaurant);
			restaurant.addUser(LoggedInUser.getcurrentUser().getId());
		}
		restAdapter.notifyDataSetChanged();
	}
	
	public void getViews() {
		etLocation = (EditText)findViewById(R.id.etLocation);
		tvDate = (TextView)findViewById(R.id.tvDateSelected);
		tvTime = (TextView)findViewById(R.id.tvTimeSelected);
		btnTimeSelect = (Button) findViewById(R.id.btnTimeSelect);
		btnDateSelect = (Button) findViewById(R.id.btnDateSelect);
	    tvDateSelected = (TextView) findViewById(R.id.tvDateSelected);
	    btnDateSelect = (Button) findViewById(R.id.btnDateSelect);
	}
	
	public void loadEvent() {
		
		// Read from the currentEvent
	
		etLocation.setText(currentEvent.getLocation());
		tvDate.setText(currentEvent.getDate());
		tvTime.setText(currentEvent.getTime());
		
		// load the restaurant id and count
		List<String> prevSelections =  currentEvent.getSelection();
		if (prevSelections != null) {
			for (int i = 0; i < prevSelections.size(); ++i) {

				String restId = Event.getSelectionRest(prevSelections.get(i));
				String userId = Event.getSelectionUser(prevSelections.get(i));
				addRestaurant(restId, userId);
				// TODO Not sure if I need to have the previous Selection information
				// since we do not allow more than one selection.
				// Add my previous selections
				if (userId.equals(LoggedInUser.getcurrentUser().getId())) { // comes after addRestaurant(..)
					mySelection.add(restId);
					prevSelection.add(restId);
				}
			}
			// restaurants is populated with Business info.
			populateBusinessInfo();
			
		}
		etLocation.setEnabled(false);
		tvDate.setEnabled(false);
		// Show in the listView
		
	}
	
	public void addRestaurant(String restId, String userId ) {
		Rest rest;
		if (!mySelection.contains(restId)) { // user already selected restaurant

			if (Restaurants.containsKey(restId)) { // preexisting
				rest = Restaurants.get(restId);
			} else { // new restaurant
				rest = new Rest();
				rest.setRestId(restId);
				Restaurants.put(rest.getRestId(), rest);
				restaurants.add(rest);
			}
			rest.addUser(userId);
		}
	}
	
	public void addRestaurantWithBusinessInfo(String restId, String userId , Business businessInfo) {
		if (!mySelection.contains(restId)) { // user already selected restaurant
			Rest rest;
			if (Restaurants.containsKey(restId)) { // preexisting restaurant
				rest = Restaurants.get(restId);
			} else { // new restaurant
				rest = new Rest();
				rest.setRestId(restId);
				rest.inflateBusinessObject(businessInfo);
				restMap.put(businessInfo.getId(), businessInfo);
				Restaurants.put(rest.getRestId(), rest);
				restaurants.add(rest);
			}
			mySelection.add(restId);
			rest.addUser(userId);
			restAdapter.notifyDataSetChanged();
		}	
	}

	public void onDone(View v) {
		Toast.makeText(this,"Sending out invitations to " + currentGroup.getGroupName() , Toast.LENGTH_SHORT).show();
		if (currentEvent == null) {
			parseEventApi.createEvent(currentGroup, dateOfEvent, timeOfEvent, etLocation.getText().toString(), LoggedInUser.getcurrentUser().getId(), newSelections());
		} else {
			parseEventApi.updateEvent(currentEvent, LoggedInUser.getcurrentUser().getId(), newSelections() );
		}
		
		// TODO Reload the page to get the Event ID
		
		// Get the latest values from the ParseInstallation object.
		ParseInstallation.getCurrentInstallation().refreshInBackground(new RefreshCallback() {
			
			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					pushToVotingActivity();
				}
			}
		});	
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
	
	public void pushToVotingActivity() {

		JSONObject obj;
		try {
			obj = new JSONObject();
			obj.put("alert", "All users of " + currentGroup.getGroupName() + " are recieving this notification!");
			obj.put("title", "New event invite!");
			obj.put("action", VotingActivityReceiver.intentAction);
			obj.put("customdata","My message");
			
			/* 
			 * Every Parse application installed on a device registered for push notifications has an associated Installation object. 
			 * The Installation object is where you store all the data needed to target push notifications.
			 * 
			 *  Installation objects are available through the ParseInstallation class, a subclass of ParseObject. 
			 *  It uses the same API for storing and retrieving data.
			 */
						
			/*
			 * You can even create relationships between your Installation objects and other classes saved on Parse. 
			 * To associate an Installation with a particular user, for example, 
			 * you can simply store the current user on the ParseInstallation.
			 */
			
			ParseInstallation.getCurrentInstallation().put("groupname", currentGroup.getGroupName());
			ParseInstallation.getCurrentInstallation().put("currentuser", LoggedInUser.getcurrentUser().getFirstName());
			
			ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {
						Toast toast = Toast.makeText(getApplicationContext(), "ParseInstallation Saved!", Toast.LENGTH_SHORT);
						toast.show();
					} else {
						e.printStackTrace();

						Toast toast = Toast.makeText(getApplicationContext(), "ParseInstallation Save Failed :(" , Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			});
			
			/*
			 * Once you have your data stored on your Installation objects, 
			 * you can use a ParseQuery to target a subset of these devices. 
			 * 
			 * Installation queries work just like any other Parse query, 
			 * but we use the special static method ParseInstallation.getQuery() to create it.
			 */
			
			ParsePush push = new ParsePush();
			ParseQuery query = ParseInstallation.getQuery();
			
			// Send push notification to query - in this case, only the current group's users will be notified
			query.whereEqualTo("groupname", currentGroup.getGroupName());
			push.setQuery(query);
			push.setData(obj);
			push.sendInBackground(); 
			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}
	
	public void onNo(View v) {
		parseEventApi.updateEvent(currentEvent, LoggedInUser.getcurrentUser());
	}
	
	public void findEvent(String groupId) {
		// First get the group
		// TODO combine group finding and event in  one query
		parseEventApi.getGroupById(groupId);	
	}
	
	@Override
	public void onGetGroupByIdResult(Group group) {
		// Now get the event associated with the group
		
		if ( group != null ) {
			currentGroup = group;
			parseEventApi.getEventsForGroup(group);
			// change the title of the View
			setTitle(currentGroup.getGroupName());
			getActionBar().setSubtitle(currentGroup.getDesc());
		}
	}

	@Override
	public void onGetEventsForGroupResult(List<Event> events) {
		Button btnTimeSelect = (Button) findViewById(R.id.btnTimeSelect);
		Button btnDateSelect = (Button) findViewById(R.id.btnDateSelect);
		
		if (events != null && events.size() > 0) {
			currentEvent = events.get(0);
			existEventView();
			loadEvent(); // Set Existing View in loadEvent
		} else {
			// New Event View
			newEventView();
			
			createDatePicker();
			createTimePicker();
		}
	}
	
	public void newEventView() {
		getViews();
		btnTimeSelect.setVisibility(View.VISIBLE);
		btnDateSelect.setVisibility(View.VISIBLE);
	}
	
	public void existEventView() {
		getViews();
		btnTimeSelect.setVisibility(View.GONE);
		btnDateSelect.setVisibility(View.GONE);
		etLocation.setFocusable(false);
		etLocation.setEnabled(false);
		//etLocation.setBackgroundResource("#00000000");
	}
	

	@Override
	public void onGetRestaurantsForEventResult(List<Restaurant> restaurants) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGetEventByIdResult(Event event) {
		// TODO Auto-generated method stub
		
	}
}
