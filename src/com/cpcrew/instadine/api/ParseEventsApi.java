package com.cpcrew.instadine.api;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.Restaurant;
import com.cpcrew.instadine.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class ParseEventsApi {

	/**
	 * Declaring the interface, to invoke a callback function in the
	 * implementing activity class
	 */
	ParseEventApiListener mParseApiListner;

	public ParseEventsApi(Activity activity) {
		mParseApiListner = (ParseEventApiListener) activity;
	}

	/**
	 * An interface to be implemented in the hosting activity for "OK" button
	 * click listener
	 */
	public interface ParseEventApiListener {
		public void onGetEventsForGroupResult(List<Event> events);
		public void onGetRestaurantsForEventResult(List<Restaurant> restaurants);
		public void onGetGroupByIdResult(Group group);
	}

	// Won't work
	// public void getEventsForGroup(Group group) {
	//
	// // implement sort by time
	// group.getEventRelation().getQuery().findInBackground(new
	// FindCallback<Event>() {
	// public void done(List<Event> events, ParseException e) {
	// if ( e == null ) {
	// Log.d("debug","Number of events for this group: " + events.size());
	// mParseApiListner.onGetEventsForGroupResult(events);
	// }
	//
	// else
	// Log.d("debug",e.getMessage());
	// }
	// });
	// }

//	public void getRestaurantsForEvent(Event event) {
//
//		event.getRestaurantRelation().getQuery()
//				.findInBackground(new FindCallback<Restaurant>() {
//					public void done(List<Restaurant> restaurants,
//							ParseException e) {
//						if (e == null) {
//							Log.d("debug",
//									"Number of restaurants for this event: "
//											+ restaurants.size());
//							mParseApiListner
//									.onGetRestaurantsForEventResult(restaurants);
//						}
//
//						else
//							Log.d("debug", e.getMessage());
//					}
//				});
//	}
//
//	public void createEvent(Group group, Date date) {
//		Event event = new Event();
//		event.setDate(date.toString());
//		event.setGroup(group);
//		event.saveInBackground();
//	}
//
	public void getEventsForGroup(Group group) {
		ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
		eventQuery.whereEqualTo("group", group);
		// execute the query
		eventQuery.findInBackground(new FindCallback<Event>() {
			public void done(List<Event> events, ParseException e) {
				if (e == null)
					mParseApiListner.onGetEventsForGroupResult(events);
				else
					Log.d("debug", e.getMessage());
			}
		});
	}
	
	public void createEvent(Group group, String date , String userid , ArrayList<String> restid) {
		Event event = new Event();
		event.setDate(date);
		event.setGroup(group);
		for ( String rest : restid)
			event.addSelection(userid, rest);
		event.saveInBackground();
	}
	
	public void updateEvent(Event event, String userid, ArrayList<String> restid) {
		for ( String rest : restid)
			event.addSelection(userid, rest);
		event.saveInBackground();
	}
	
	public void updateEvent(Event event, User rejectedUser) {
		event.addRejectedUser(rejectedUser);
		event.saveInBackground();
	}
	
	public void getEventsForGroup(String groupId) {
		ParseQuery<Group> innerQuery = ParseQuery.getQuery(Group.class);
		innerQuery.whereExists(groupId);
		ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
		query.whereMatchesQuery("group", innerQuery);
		// execute the query
		query.findInBackground(new FindCallback<Event>() {
			public void done(List<Event> events, ParseException e) {
				if (e == null)
					mParseApiListner.onGetEventsForGroupResult(events);
				else
					Log.d("debug", e.getMessage());
			}
		});
	}
	
	public void getGroupById(String groupId) {
		ParseQuery<Group> query = ParseQuery.getQuery(Group.class);
		query.getInBackground(groupId, new GetCallback<Group>() {
		  public void done(Group group, ParseException e) {
		    if (e == null) {
		    	mParseApiListner.onGetGroupByIdResult(group);
		    } else {
		    	Log.d("debug", e.getMessage());
		    }
		  }
		});	
	}
	
//	public void createRestaurant(final Restaurant restaurant) {
//
//	    restaurant.saveInBackground(new SaveCallback() {
//	    	
//	        public void done(ParseException e) {
//	            if (e == null) {
//	                // Saved successfully.
//	                Log.d(TAG, "User update saved!");
//	                String id = po.getObjectId();
//	                Log.d(TAG, "The object id is: " + id);
//	            } else {
//	                // The save failed.
//	                Log.d(TAG, "User update error: " + e);
//	            }
//	        }
//	    });
//	}
	


}
