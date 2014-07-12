package com.cpcrew.instadine.models;


public class LoggedInUser {
    private static User currentUser;
    public static void setCurrentUser(User user) {
    	
    	// testing
        currentUser = user;
      
        // store in shared preferences ??
//        if (user == null) {
//            InstaDineApplication.getSharedPreferences().edit().remove("current_user")
//                    .commit();
//        } else {
//            InstaDineApplication.getSharedPreferences().edit()
//                    .putString("current_user", user.jsonObject.toString()).commit();
//        }
    }
    
    public static User getcurrentUser() {
    	return currentUser;
    }

}
