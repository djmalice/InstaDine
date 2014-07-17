package com.cpcrew.instadine.models;

import com.parse.ParseUser;


public class LoggedInUser {
    private static User currentUser;
    public static void setCurrentUser(ParseUser parseUser) {
    	
        currentUser = new User(parseUser);
      
    }
    
    public static User getcurrentUser() {
    	return currentUser;
    }

}
