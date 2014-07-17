/**
 * 
 */
package com.cpcrew.instadine.models;

import java.util.HashSet;


/**
 * @author raji
 *
 */
public class Rest {
	
	private Business restaurant;
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
	public Business getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Business restaurant) {
		this.restaurant = restaurant;
	}
	


}
