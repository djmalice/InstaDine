package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
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
		addDummyRestaurant();
	}
	
	public void callSearchActivity(View v){
		addDummyRestaurant();
		Intent i = new Intent(this, MapActivity.class);
		startActivity(i);
		//addDummyRestaurant();
	}
	
	public void addDummyRestaurant() {
		 String[] restaurants = {"World Wraps", "Dish Dash", "Olive Garden" , "Thai Basil"};
		if (currentEvent == null) {
			Rest restaurant = new Rest();
			restaurant.restName = restaurants[counter++];
			restaurant.count = 1;
			currentSelection.add(restaurant.restName);
			restAdapter.add(restaurant);
		}
	}
	
	public void onDone(View v) {
		Toast.makeText(this,"Sending out invitations to " + currentGroup.getGroupName() , Toast.LENGTH_SHORT).show();
//		if (currentEvent == null) {
//			parseEventApi.createEvent(currentGroup, "07/20/2014", LoggedInUser.getcurrentUser().getObjectId(),currentSelection);
//		} else {
//			parseEventApi.updateEvent(currentEvent, LoggedInUser.getcurrentUser().getObjectId(), currentSelection );
//		}
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
		}
		
	}

	@Override
	public void onGetRestaurantsForEventResult(List<Restaurant> restaurants) {
		// TODO Auto-generated method stub
		
	}
}
