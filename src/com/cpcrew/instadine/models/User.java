package com.cpcrew.instadine.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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

}
