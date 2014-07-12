package com.cpcrew.instadine.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("Group")
public class Group extends ParseObject {
	
	public Group() {
		super();
	}
	
	public Group(int id, String name) {
		super();
		
	}
	
	private void setGroupId(int id) {
		put("gid",id);
	}
	
	public int getGroupId() {
		return getInt("objectId");
	}
	
	
	public void setGroupName(String name) {
		put("gname", name);
	}
	
	public String getGroupName() {
		return getString("gname");
	}

	// From parse.com 
	// We do not recommend storing large pieces of binary data 
	// like images or documents using byte[] fields on ParseObject. 
	// ParseObjectss should not exceed 128 kilobytes in size. To store more,
	// we recommend you use ParseFile.
	
	public ParseFile getPhotoFile() {
        return getParseFile("gimage");
    }
 
    public void setPhotoFile(ParseFile file) {
        put("gimage", file);
    }
    
    public ParseRelation<User> getUserRelation() {
    	return getRelation("uid");
    }
    
	public void addUser(User user ) {
		getUserRelation().add(user);
		saveInBackground();
	}
	
	public void removeUser(User user) {
		getUserRelation().remove(user);
		saveInBackground();
	}
	
	
	// Adding Events to a Group
	public ParseRelation<Event> getEventRelation() {
	      return getRelation("events");
	  }

	  public void addEvent(Event event) {
	    getEventRelation().add(event);
	    saveInBackground();
	  }

	  public void removeEvent(Event event) {
	     getEventRelation().remove(event);
	     saveInBackground();
	  }


}
