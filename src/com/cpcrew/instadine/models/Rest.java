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
	
	private String restName;
	private int count;
	private HashSet<String> userids; // objectIds of users that selected this restaurant, no duplicates
	
	public String getRestName() {
		return restName;
	}
	public void setRestName(String restName) {
		this.restName = restName;
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
	
	public HashSet<String> getUsers() {
		return userids;
	}
	


}
