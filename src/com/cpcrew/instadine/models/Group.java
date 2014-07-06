package com.cpcrew.instadine.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Group")
public class Group extends ParseObject {
	
	public Group() {
		super();
	}
	
	public Group(int id, String name) {
		super();
		
	}
	
	public void setGroupId(int id) {
		put("gid",id);
	}
	
	public int getGroupId() {
		return getInt("gid");
	}
	
	
	public void setGroupName(String name) {
		put("gname", name);
	}
	
	public String getGroupName() {
		return getString("gname");
	}

	// TODO set group image
}
