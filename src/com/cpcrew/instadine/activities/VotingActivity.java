package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

public class VotingActivity extends FragmentActivity implements ParseEventApiListener, 
	CalendarDatePickerDialog.OnDateSetListener, RadialTimePickerDialog.OnTimeSetListener {
	
	private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
	private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {        	
	    	Toast.makeText(getApplicationContext(), "onReceive invoked!", Toast.LENGTH_LONG).show();
	    }
	};
	
	private TextView tvDateSelected;
	private Button btnDateSelect;
	private Button btnTimeSelect;
	
	private ParseEventsApi parseEventApi;
	private Event currentEvent;
	private Group currentGroup;
	
	private ArrayList<String> mySelection;
	private ArrayList<Rest> restaurants;
	private RestaurantArrayAdapter restAdapter;
	protected ListView lvRestaurants;
	private static int counter = 0;
	
	private String groupId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		if ( groupId == null)
			groupId = getIntent().getStringExtra("group_id");
		setContentView(R.layout.activity_voting);
		lvRestaurants = (ListView) findViewById(R.id.lvRestaurants);
		
		//initialize
		restaurants = new ArrayList<Rest>();
		mySelection = new ArrayList<String>();
		parseEventApi = new ParseEventsApi(this);
		
		findEvent(groupId);
		
		restAdapter = new RestaurantArrayAdapter(this, restaurants);
		lvRestaurants.setAdapter(restAdapter);
		onRestaurantSelected();
		populateBusinessInfo();
		
		createDatePicker();
        createTimePicker();
    	
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
    		}
    		else {
    			btnTimeSelect.setText("" + hourOfDay + ":" + minute + "PM");
    		}
    	}
    	else {
    		if (minute < 10) {
    			btnTimeSelect.setText("" + hourOfDay + ":0" + minute + "AM");
    		}
    		else {
    			btnTimeSelect.setText("" + hourOfDay + ":" + minute + "AM");
    		}    		
    	}
    }

	public void createDatePicker() {
        tvDateSelected = (TextView) findViewById(R.id.tvDateSelected);
        btnDateSelect = (Button) findViewById(R.id.btnDateSelect);
        
        tvDateSelected.setText("--");
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
		addDummyRestaurant();
		
		Intent i = new Intent(this, MapActivity.class);
		startActivityForResult(i,Constants.REQUEST_CODE);
		//addRestaurantSelection(rest);
		//addDummyRestaurant();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // REQUEST_CODE is defined above
	  if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE) {
	     // Extract name value from result extras
	     Business business = (Business) data.getExtras().getSerializable("restaurant");
	     // Toast the name to display temporarily on screen
	     Toast.makeText(this, business.getName(), Toast.LENGTH_SHORT).show();
	     
	     // Add this to the Restaurant ListView
	  }
	} 
	
	public void populateBusinessInfo() {
		// Given a list of restaurant IDs return business Objects
		// Populate the restaurants with the business info
		//addRestaurantSelection
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
					if (listItem.getRestName().equals(cs)) 
						removeSelection = true;
					
				}
				if ( removeSelection == false )
					addRestaurantSelection(listItem);
				else
					removeRestaruantSelection(listItem);
				
			}
		});
	}

	public void removeRestaruantSelection(Rest restaurant) {
		mySelection.remove(restaurant.getRestName());
		for ( Rest rt : restaurants){
			if ( rt.getRestName().equals(restaurant.getRestName()) ){
				rt.removeUser(LoggedInUser.getcurrentUser().getId());
			}
		}
		// reflect in the adpater
		restAdapter.notifyDataSetChanged();
		
	}
	
	public void addRestaurantSelection(Rest restaurant) {
		mySelection.add(restaurant.getRestName());
		boolean isActionDone = false;
		for ( Rest rt : restaurants){
			if ( rt.getRestName().equals(restaurant.getRestName()) ){
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
	
	public void loadEvent() {
		EditText etLocation = (EditText)findViewById(R.id.etLocation);
		TextView tvDate = (TextView)findViewById(R.id.tvDate);
		
		// Read from the currentEvent
		etLocation.setText(currentEvent.getEventName());
		tvDate.setText(currentEvent.getDate());

		// load the restaurants
		List<String> prevSelections =  currentEvent.getSelection();
		HashMap<String, Rest> restIds = new HashMap<String, Rest>();
		if (prevSelections != null) {
			for (int i = 0; i < prevSelections.size(); ++i) {

				String restId = Event.getSelectionRest(prevSelections.get(i));
				String userId = Event.getSelectionUser(prevSelections.get(i));
				Rest restaurantSel;
				if (restIds.containsKey(restId))
					restaurantSel = restIds.get(restId);
				else {
					restaurantSel = new Rest();
					restaurantSel.setRestName(restId);
					restIds.put(restId, restaurantSel);
				}
				restaurantSel.addUser(userId);

				// Add my previous selections
				System.out.println(userId + "::: "
						+ LoggedInUser.getcurrentUser().getId());
				if (userId.equals(LoggedInUser.getcurrentUser().getId()))
					mySelection.add(restId);
				restAdapter.add(restaurantSel); // View

			}
		}
		etLocation.setEnabled(false);
		tvDate.setEnabled(false);
	}
	
	public void addDummyRestaurant() {
		 String[] restaurants = {"World Wraps", "Dish Dash", "Olive Garden" , "Thai Basil"};
		if (currentEvent == null) {
			Rest restaurant = new Rest();
			restaurant.setRestName(restaurants[counter++]);
//			restaurant.setCount(1);
//			currentSelection.add(restaurant.getRestName());
//			restAdapter.add(restaurant);
			addRestaurantSelection(restaurant);
		}
	}
	
	public void onDone(View v) {
		Toast.makeText(this,"Sending out invitations to " + currentGroup.getGroupName() , Toast.LENGTH_SHORT).show();
		if (currentEvent == null) {
			parseEventApi.createEvent(currentGroup, "07/20/2014", LoggedInUser.getcurrentUser().getId(),mySelection);
		} else {
			parseEventApi.updateEvent(currentEvent, LoggedInUser.getcurrentUser().getId(), mySelection );
		}
		
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
		}
	}

	@Override
	public void onGetEventsForGroupResult(List<Event> events) {
		if (events != null && events.size() > 0) {
			currentEvent = events.get(0);
			loadEvent();
		}
	}

	@Override
	public void onGetRestaurantsForEventResult(List<Restaurant> restaurants) {
		// TODO Auto-generated method stub
		
	}
}
