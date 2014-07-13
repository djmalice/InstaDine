package com.cpcrew.instadine.api;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.Restaurant;
import com.cpcrew.instadine.models.User;
import com.parse.FindCallback;
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

	public void getRestaurantsForEvent(Event event) {

		event.getRestaurantRelation().getQuery()
				.findInBackground(new FindCallback<Restaurant>() {
					public void done(List<Restaurant> restaurants,
							ParseException e) {
						if (e == null) {
							Log.d("debug",
									"Number of restaurants for this event: "
											+ restaurants.size());
							mParseApiListner
									.onGetRestaurantsForEventResult(restaurants);
						}

						else
							Log.d("debug", e.getMessage());
					}
				});
	}

	public void createEvent(Group group, Date date) {
		Event event = new Event();
		event.setDate(date.toString());
		event.setGroup(group);
		event.saveInBackground();
	}

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
	
	public void createRestaurant(String name , User user, Event event) {
		
	}

}
