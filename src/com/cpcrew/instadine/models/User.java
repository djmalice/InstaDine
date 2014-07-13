package com.cpcrew.instadine.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("User")
public class User extends ParseObject {
	
	
	public User() {
		super();
	}

//	public String getObjectId() {
//		return getString("objectId");
//	}
//	
	public String getId() {
		//return getString("objectId");
		return getObjectId();
	}
	
	public void setFirstName(String first) {
		put("first" , first);
	}
	
	public String getFirstName() {
		return getString("first");
	}
	
	public void setLastName(String last) {
		put("last", last);
	}
	
	public String getLastName() {
		return getString("last");
	}
	
	public ParseRelation<User> getFriendRelation() {
		return getRelation("friends");
	}
	
	/**
	 *  link Friend(User) and User
	 *  
	 */
	public void addToFriends(User friend ) {
		getFriendRelation().add(friend);
		saveInBackground();
	}
	
	public void removeFromFriends(User friend) {
		getFriendRelation().remove(friend);
		saveInBackground();
	}
	
//	public ParseRelation<Group> getGroupRelation() {
//		return getRelation("gid");
//	}
	
	
	
// Never do this ( it uses inverse relationship concept to query)
//	/**
//	 *  link Group and User
//	 *  
//	 */
//	public void addToGroup(Group group ) {
//		getGroupRelation().add(group);
//		saveInBackground();
//	}
//	
//	public void removeFromGroup(Group group) {
//		getGroupRelation().remove(group);
//		saveInBackground();
//	}
}
