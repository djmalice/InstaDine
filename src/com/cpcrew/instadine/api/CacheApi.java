/**
 * 
 */
package com.cpcrew.instadine.api;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * @author Rajalakshmi Balakrishnan
 * 
 */
public class CacheApi {
	
	/** Declaring the interface, to invoke a callback function in the implementing activity class */
	CacheApiListener mCacheApiListener;
	
	
	public CacheApi(Activity activity) {
		mCacheApiListener = (CacheApiListener) activity;
	}
	
	   /** An interface to be implemented in the hosting activity  */
    public interface CacheApiListener {
        public void onGetSelectedUsersFromCacheResult(List<User> users);
        public void onGetFriendsFromCacheResult(List<User> users);
    }
    
	

	public static void cacheSelectedUsers(ArrayList<User> users) {
		ParseObject.pinAllInBackground("selectedusers", users);
	}

	public static void removeSelectedUsersFromCache() {
		ParseObject.unpinAllInBackground("selectedusers");
	}

	public void getSelectedUsersFromCache() {
		ParseQuery<User> query = ParseQuery.getQuery("selectedusers");
				query.fromLocalDatastore();
				query.findInBackground(new FindCallback<User>() {
					public void done(List<User> users, ParseException e) {
						mCacheApiListener.onGetSelectedUsersFromCacheResult(users);
					}
				});
	}
	
	public static void cacheFriends(List<User> friends) {
		User.pinAllInBackground("selfriends", friends);
	}

	public static void removeFriends() {
		User.unpinAllInBackground("selfriends");
	}

	public void getFriendsFromCache() {
		ParseQuery<User> query = ParseQuery.getQuery("selfriends");
				query.fromLocalDatastore();
				query.findInBackground(new FindCallback<User>() {
					public void done(List<User> users, ParseException e) {
						if (e == null ) {
							System.out.println("Number of cached friends" + users.size());
							mCacheApiListener.onGetFriendsFromCacheResult(users);
							
						}
						else {
							System.out.println(e.getMessage());
						}
					}
				});
	}
}