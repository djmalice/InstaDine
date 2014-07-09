package com.cpcrew.instadine.models;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("User")
public class User extends ParseObject {
	
	
	public User() {
		super();
	}
	public void setId(int id ) {
		put("uid",id);
	}
	
	public int getId() {
		return getInt("uid");
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
	
//	public ParseRelation<Group> getGroupRelation() {
//		return getRelation("gid");
//	}
//	
//	/**
//	 *  link Group and User using just their ids like so:
//	 * @param gid
//	 */
//	public void addToGroup(Group group ) {
//		getGroupRelation().add(group);
//	}
//	
//	public void removeFromGroup(Group group) {
//		getGroupRelation().remove(group);
//	}
//	


}
