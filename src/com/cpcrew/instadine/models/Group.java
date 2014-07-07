package com.cpcrew.instadine.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
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
    

	public void addUser(int uid ) {
		addUnique("parent", ParseObject.createWithoutData("User", String.valueOf(uid)));
	}

}
