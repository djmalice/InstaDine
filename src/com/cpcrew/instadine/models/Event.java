package com.cpcrew.instadine.models;

import java.util.Arrays;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
	
	public void addSelection(String userid, String resid) {
		
		// Broken in Parse
		// does not save in proper JSON format
//		JSONObject value = new JSONObject();
//		try {
//			value.put("userid", userid);
//			value.put("resid", resid);
//			add("selections" ,value);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		add("selections" , userid + "," + resid);
		
	}
	
//	public  List<JSONObject> getSelection() {
//		return getList("selections");
//	}
	
	public List<String> getSelection() {
		return getList("selections");
	}
	
	public static String getSelectionRest(String selection) {
		List<String> ll = Arrays.asList(selection.split(","));
		return ll.get(1);
	}
	
	public static String getSelectionUser(String selection) {
		List<String> ll = Arrays.asList(selection.split(","));
		return ll.get(0);
	}
	
	public void addRejectedUser(User user) {
		add("rejectedusers" ,user.getParseUser());
	}
	
	public List<ParseUser> getRejectedUsers() {
		return getList("rejectedusers");
	}
	
	public void addOrganizer(User user) {
		add("organizer", user.getParseUser());
	}
	
	public ParseUser getParseUser() {
		return getParseUser("organizer");
	}
	
//	public ParseRelation<Restaurant> getRestaurantRelation() {
//	      return getRelation("restaurants");
//	  }
//
//	  public void addRestaurant(Restaurant restaurant) {
//	    getRestaurantRelation().add(restaurant);
//	    saveInBackground();
//	  }
//
//	  public void removeRestaurant(Restaurant restaurant) {
//	     getRestaurantRelation().remove(restaurant);
//	     saveInBackground();
//	  }

}
