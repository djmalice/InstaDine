package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.adapters.ImageAdapter;
import com.cpcrew.instadine.adapters.InfoWindowImageAdapter;
import com.cpcrew.instadine.adapters.RestaurantDropDownAdapter;
import com.cpcrew.instadine.models.LoggedInUser;
import com.cpcrew.instadine.models.Rest;
import com.cpcrew.instadine.utils.ClearableAutoCompleteTextView;
import com.cpcrew.instadine.utils.Utils;
import com.cpcrew.instadine.utils.ClearableAutoCompleteTextView.OnClearListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MapActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	
	ImageView searchIcon;
	ClearableAutoCompleteTextView searchBox;
	//AutoCompleteTextView searchBox;
	Rest listSelectedRest;
	Rest markerSelectedRest;
	private SupportMapFragment mapFragment;
	private GoogleMap map;
	private LocationClient mLocationClient;
	RestaurantDropDownAdapter adapter;
	TextView tvRestaurantName;
	TextView tvLocation;
	TextView tvVote;
	HashMap<String,Rest> dealMap;
	HashMap<String, Rest> restMap;
	HashMap<String, Rest> searchBusiness;
	HashMap<String, Integer> restCount;
	HashMap<Marker,String> markerMap;
	HashMap<String,String> groupUsersFacebookIds;
	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mLocationClient = new LocationClient(this, this, this);
		mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
		if (mapFragment != null) {
			map = mapFragment.getMap();
			if (map != null) {
				Log.d("debug","Map Fragment was loaded properly!");
				map.setMyLocationEnabled(true);
			} else {
				Log.d("debug","Error - Map was null!!");
			}
		} else {
			Log.d("debug","Error - Map Fragment was null!!");
		}
		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				tvRestaurantName.setText(arg0.getTitle());
//				Log.d("debug","searchBusiness: " + searchBusiness.toString());
//				Log.d("debug","Marker.get(arg0): " + markerMap.get(arg0));
				if(restMap.get(markerMap.get(arg0)) == null) {
					markerSelectedRest = searchBusiness.get(markerMap.get(arg0));
				} else {
//					Log.d("debug","restMap: " + restMap.toString());
					markerSelectedRest = restMap.get(markerMap.get(arg0));
				}
				tvRestaurantName.setText(arg0.getTitle());
				tvLocation.setText(markerSelectedRest.getCity());
				// Log.d("debug", "get from map:" + restCount.get(markerSelectedBusiness.getId()));
//				Log.d("debug", "RestCOunt: " + restCount.toString());
//				if(restCount.get(markerSelectedBusiness.getId()) != null) {
//					tvVote.setText(restCount.get(markerSelectedBusiness.getId()).toString());
//				}
				return false;
			}
		});
		Intent intent = getIntent();
		restMap = new HashMap<String,Rest>();
		dealMap = new HashMap<String,Rest>();
		restCount = new HashMap<String,Integer>();
		markerMap = new HashMap<Marker, String>();
		searchBusiness = new HashMap<String,Rest>();
	    restMap = (HashMap<String, Rest>)intent.getSerializableExtra("rest_map");
	    restCount = (HashMap<String, Integer>)intent.getSerializableExtra("rest_count");
	    groupUsersFacebookIds = (HashMap<String,String>)intent.getSerializableExtra("user_fb_map");
	    // Log.d("HashMapTest", restMap.get("key").toString());
		
		
		
		
		tvRestaurantName = (TextView)findViewById(R.id.tvRestaurantName);
		tvLocation = (TextView)findViewById(R.id.tvLocation);
		// tvVote = (TextView)findViewById(R.id.tvSelectionCount);
		// AutoComplete SearchBar
		ActionBar actionBar = getActionBar(); // you can use ABS or the non-bc ActionBar
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME
	            | ActionBar.DISPLAY_HOME_AS_UP); // what's mainly important here is DISPLAY_SHOW_CUSTOM. the rest is optional
	    
	    LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    // inflate the view that we created before
	    View v = inflater.inflate(R.layout.actionbar_search, null);
	    actionBar.setCustomView(v);
	    
	    
	    
	    
	    
	   // searchBox =  (ClearableAutoCompleteTextView) v.findViewById(R.id.search_box);
	    searchBox =  (ClearableAutoCompleteTextView) v.findViewById(R.id.search_box);
	    adapter = new RestaurantDropDownAdapter(this, R.layout.actionbar_search_item);
	    searchBox.setAdapter(adapter);
	    
	    
	    
	    
	    searchBox.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Log.d("debug", "in onTextChanged calling Filter");
				adapter.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    searchIcon = (ImageView) v.findViewById(R.id.action_search);
	    searchBox.setVisibility(View.INVISIBLE);
	    
		searchIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleSearch(false);
			}
		});
		
		searchBox.setOnClearListener(new OnClearListener() {
			
			@Override
			public void onClear() {
				toggleSearch(true);
			}
		});
		
		searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Rest rest = (Rest) parent.getItemAtPosition(position);
				searchBox.setText(rest.getName());
				fetchBusinessLocation(rest);
				
				
			}
			
		});
		
		setupCustomInfoWindowForMap();
		
		// load restaurants from restMap and restCount provided by voting activity
		displayRestOnMap();
	}
	
	protected void toggleSearch(boolean reset) {
		if (reset) {
			// hide search box and show search icon
			searchBox.setText("");
			searchBox.setVisibility(View.GONE);
			searchIcon.setVisibility(View.VISIBLE);
			// hide the keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
		} else {
			// hide search icon and show search box
			searchIcon.setVisibility(View.GONE);
			searchBox.setVisibility(View.VISIBLE);
			searchBox.requestFocus();
			// show the keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
		}
		
	}
	
	public void fetchBusinessLocation(Rest r){
		
		final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
		final String TYPE_DETAILS = "/details";
		final String OUT_JSON = "/json";
		final String API_KEY = com.cpcrew.instadine.utils.Constants.GOOGLE_PLACES_API_KEY;
		StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("key", API_KEY);
		params.put("placeid", r.getId());
		client.get(sb.toString(), params, new
		    JsonHttpResponseHandler() {
			
			
		        public void onSuccess(JSONObject response) {
		        	
    				try {
						 JSONObject jsonObject = response.getJSONObject("result");
						 listSelectedRest = Rest.fromDetailJson(jsonObject);
				         Log.d("debug", " List Business Object: " + listSelectedRest.toString());
				        
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	
    				displayMapMarker(listSelectedRest,false);
		        };
		        

		        @Override
		        public void onFailure(Throwable e, String s) {
		            Log.d("ERROR", e.toString());
		        }	
		    }
		);
    }
	
	public void displayMapMarker(Rest rest,boolean currentUserChoice){
		final LatLng restaurant = new LatLng(rest.getLat(), rest.getLongi());
		Marker res;
		if(dealMap.containsKey(rest.getId())){
			currentUserChoice=true;
		}
		if(currentUserChoice) {
			res = map.addMarker(new MarkerOptions()
            .position(restaurant)
            .title(rest.getName())
            .flat(true)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_deals))); 
		} else {
			res = map.addMarker(new MarkerOptions()
	                              .position(restaurant)
	                              .title(rest.getName())
	                              .flat(true)
	                              .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)));
		}
	    markerMap.put(res, rest.getId());
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurant, 10));
	    searchBusiness.put(rest.getId(), rest);
		
	    // res.showInfoWindow();
	    // Add to temporary search business map for all markers in the system
	    
	    
	    
	}
	
	
    

	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		if (isGooglePlayServicesAvailable()) {
			mLocationClient.connect();
		}

	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}
	
	// Setup markers on map for previously selected restaurants
	public void displayRestOnMap(){
		dealMap = Utils.populateDealMap();
		
		for(Rest r:restMap.values()){
			if(dealMap.containsKey(r.getId())){
				displayMapMarker(r,true);
			} else {
				displayMapMarker(r,false);
			}
		}
		
		for(Rest r:dealMap.values()){
			if(!restMap.containsKey(r.getId())){
				displayMapMarker(r,true);
			}
		}
		
		
	}
		
	
	
	
	
	

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				mLocationClient.connect();
				break;
			}

		}
	}

	private boolean isGooglePlayServicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			return true;
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getSupportFragmentManager(), "Location Updates");
			}

			return false;
		}
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Location location = mLocationClient.getLastLocation();
		if (location != null) {
			Log.d("debug", "GPS location was found!");
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
			map.animateCamera(cameraUpdate);
		} else {
			Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
		}
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {

		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}
		
	
	public void sendSelectionToVoting(View v){
		if (markerSelectedRest == null) {
			Toast.makeText(getBaseContext(),
					"Please select a marker before pressing done",
					Toast.LENGTH_LONG).show();
		} else {
			if (restMap.containsKey(markerSelectedRest.getId())) {
				restCount.put(markerSelectedRest.getId(),
						restCount.get(markerSelectedRest.getId()) + 1);
			} else {
				restMap.put(markerSelectedRest.getId(),
						markerSelectedRest);
				restCount.put(markerSelectedRest.getId(), 1);
			}

			// Send to voting acitivity
			Intent data = new Intent();
			data.putExtra("rest_map", restMap);
			data.putExtra("rest_count", restCount);
			data.putExtra("user_choice", markerSelectedRest);
			setResult(RESULT_OK, data);
			finish();
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
		}
		
		
	}
	
	
public void setupCustomInfoWindowForMap(){
		map.setInfoWindowAdapter(new InfoWindowAdapter() {
			ImageAdapter userImages = new ImageAdapter(getBaseContext(), null);

			@Override
			public View getInfoWindow(Marker arg0) {
				View v = getLayoutInflater().inflate(
						R.layout.info_window_layout, null);

				// Getting the position from the marker
				String restName = arg0.getTitle();

				// Getting reference to the TextView to set Restaurant Name
				TextView tvInfoWindowRestName = (TextView) v
						.findViewById(R.id.tvInfoWindowRestName);
				Rest thisRest = new Rest();

				if (restMap.get(markerMap.get(arg0)) == null) {
					thisRest = searchBusiness.get(markerMap.get(arg0));
					thisRest.addGroupUser(
							LoggedInUser.getcurrentUser().getId(), LoggedInUser
									.getcurrentUser().getFacebookId());
				} else {
					// Log.d("debug","restMap: " + restMap.toString());
					thisRest = restMap.get(markerMap.get(arg0));
				}

				Log.d("debug", "thisRest: " + thisRest.toString());
				HashMap<String, String> fbidmap = thisRest
						.getGroupUserFacebookIds();

				// Getting reference to GirdView to set User Images
				GridView gvInfoWindowUserImages = (GridView) v
						.findViewById(R.id.gvInfoWindowUserImages);
				if (!fbidmap.values().isEmpty()) {
					gvInfoWindowUserImages
							.setAdapter(new InfoWindowImageAdapter(
									getBaseContext(), new ArrayList<String>(
											fbidmap.values())));
				}
				// Setting the rest name
				tvInfoWindowRestName.setText(restName);

				// Returning the view containing InfoWindow contents
				return v;
			}

			@Override
			public View getInfoContents(Marker arg0) {
				return null;
			}
		});
}
	
	
	
	

}