package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.adapters.RestaurantArrayAdapter;
import com.cpcrew.instadine.api.ParseEventsApi;
import com.cpcrew.instadine.api.ParseEventsApi.ParseEventApiListener;
import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.LoggedInUser;
import com.cpcrew.instadine.models.Rest;
import com.cpcrew.instadine.models.Restaurant;
import com.parse.ParseObject;

public class VotingActivity extends Activity implements ParseEventApiListener {
	
	private ParseEventsApi parseEventApi;
	private Event currentEvent;
	private Group currentGroup;
	private ArrayList<Rest> restaurants;
	private RestaurantArrayAdapter restAdapter;
	protected ListView lvRestaurants;
	private static int counter = 0;
	private ArrayList<String> currentSelection;
	private String groupId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("oncreate");
		super.onCreate(savedInstanceState);
		if ( groupId == null)
			groupId = getIntent().getStringExtra("group_id");
		setContentView(R.layout.activity_voting);
		lvRestaurants = (ListView) findViewById(R.id.lvRestaurants);
		//initialize
		restaurants = new ArrayList<Rest>();
		currentSelection = new ArrayList<String>();
		parseEventApi = new ParseEventsApi(this);
		findEvent(groupId);
		
		restAdapter = new RestaurantArrayAdapter(this, restaurants);
		lvRestaurants.setAdapter(restAdapter);
		//addDummyRestaurant();
	}
	
	public void callSearchActivity(View v){
		addDummyRestaurant();
		Intent i = new Intent(this, MapActivity.class);
		startActivity(i);
		//addDummyRestaurant();
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
		for ( int i = 0; i < prevSelections.size();++i) {

				String restName = Event.getSelectionRest(prevSelections.get(i));
				String userId = Event.getSelectionUser(prevSelections.get(i));
				Rest restaurantSel;
				if ( restIds.containsKey(restName))
					restaurantSel = restIds.get(restName);
				else {
					restaurantSel = new Rest();
					restaurantSel.setRestName(restName);
					restIds.put(restName, restaurantSel);
				}
				restaurantSel.addUser(userId);
				restAdapter.add(restaurantSel); // View 
		
		}
		etLocation.setEnabled(false);
		tvDate.setEnabled(false);
	}
	
	public void addDummyRestaurant() {
		 String[] restaurants = {"World Wraps", "Dish Dash", "Olive Garden" , "Thai Basil"};
		if (currentEvent == null) {
			Rest restaurant = new Rest();
			restaurant.setRestName(restaurants[counter++]);
			restaurant.setCount(1);
			currentSelection.add(restaurant.getRestName());
			restAdapter.add(restaurant);
		}
	}
	
	public void onDone(View v) {
		Toast.makeText(this,"Sending out invitations to " + currentGroup.getGroupName() , Toast.LENGTH_SHORT).show();
		if (currentEvent == null) {
			parseEventApi.createEvent(currentGroup, "07/20/2014", LoggedInUser.getcurrentUser().getObjectId(),currentSelection);
		} else {
			parseEventApi.updateEvent(currentEvent, LoggedInUser.getcurrentUser().getId(), currentSelection );
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
