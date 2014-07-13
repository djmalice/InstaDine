package com.cpcrew.instadine.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("Event")
public class Event extends ParseObject {
	
	public Event() {
		super();
	}
//	public void setId(int id ) {
//		put("eid",id);
//	}
	
	public String getId() {
		return getObjectId();
	}
	
	public void setEventName(String eventName) {
		put("ename" , eventName);
	}
	
	public String getEventName() {
		return getString("ename");
	}
	
	public void setDate(String date) {
		put("date", date);
	}
	
	public String getDate() {
		return getString("date");
	}
	
	public void setGroup(Group group) {
		put("group", group);
	}
	
	public Group getGroup() {
		return (Group) getParseObject("group");
	}
	
	public ParseRelation<Restaurant> getRestaurantRelation() {
	      return getRelation("restaurants");
	  }

	  public void addRestaurant(Restaurant restaurant) {
	    getRestaurantRelation().add(restaurant);
	    saveInBackground();
	  }

	  public void removeRestaurant(Restaurant restaurant) {
	     getRestaurantRelation().remove(restaurant);
	     saveInBackground();
	  }

}
