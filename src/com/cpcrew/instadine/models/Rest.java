/**
 * 
 */
package com.cpcrew.instadine.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;


/**
 * @author raji
 *
 */
public class Rest implements Serializable {
	
	//private Business restaurant;
	private String id;
	private int count = 0;
	private HashSet<String> userids; // objectIds of users that selected this restaurant, no duplicates
	
	public String getRestId() {
		return id;
	}
	public void setRestId(String id) {
		this.id = id;
	}
	public int getCount() {
		if ( userids == null ) return 0;
		else return userids.size();
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public void addUser(String userid) {
		if(userids == null)
			userids = new HashSet<String>();
		userids.add(userid);
	}
	
	public void removeUser(String userid) {
		userids.remove(userid);
	}
	
	public HashSet<String> getUsers() {
		return userids;
	}
	
//	public Business getRestaurant() {
//		return null;
//	}
//	public void setRestaurant(Business restaurant) {
//		//this.restaurant = restaurant;
//	}
	private String name;
	private String phone;
	private String imageUrl;
	private String city;
	private double rating;

	
	public String getId() {
		return id;
	}

	public String getCity() {
		return city;
	}



	public double getRating() {
		return rating;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public String getImageUrl() {
		return this.imageUrl;
	}
	
	public void inflateBusinessObject( Business b) {
		name = b.getName();
		city = b.getCity();
		rating = b.getRating();
		imageUrl = b.getImageUrl();
		id = b.getId();
		phone = b.getPhone();
		
	}
	
	// To show the user images
	private HashMap<String, String> groupUserFacebookIds;
	
	public HashMap<String, String> getGroupUserFacebookIds() {
		return groupUserFacebookIds;
	}
	
	public void addGroupUser(String userid, String facebookId) {
		if(groupUserFacebookIds == null)
			groupUserFacebookIds = new HashMap<String, String>();
		groupUserFacebookIds.put(userid, facebookId);
	}
	
}
