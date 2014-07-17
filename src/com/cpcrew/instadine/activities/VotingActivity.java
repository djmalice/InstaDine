package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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

public class VotingActivity extends Activity implements ParseEventApiListener {
	
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
	}
	
	public void callSearchActivity(View v){
		addDummyRestaurant();
		
//		Intent i = new Intent(this, MapActivity.class);
//		startActivityForResult(i,Constants.REQUEST_CODE);
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
		for ( int i = 0; i < prevSelections.size();++i) {

				String restId = Event.getSelectionRest(prevSelections.get(i));
				String userId = Event.getSelectionUser(prevSelections.get(i));
				Rest restaurantSel;
				if ( restIds.containsKey(restId))
					restaurantSel = restIds.get(restId);
				else {
					restaurantSel = new Rest();
					restaurantSel.setRestName(restId);
					restIds.put(restId, restaurantSel);
				}
				restaurantSel.addUser(userId);
				
				// Add my previous selections
				System.out.println(userId + "::: " + LoggedInUser.getcurrentUser().getId());
				if ( userId.equals(LoggedInUser.getcurrentUser().getId()))
					mySelection.add(restId);
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
