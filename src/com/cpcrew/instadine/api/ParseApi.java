package com.cpcrew.instadine.api;

import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

public class ParseApi {
	
	/** Declaring the interface, to invoke a callback function in the implementing activity class */
	ParseApiListener mParseApiListner;
	public ParseApi(Activity activity) {
		mParseApiListner = (ParseApiListener) activity;
	}

	public static void createGroup(String groupName, List<User> users) {
		final int gid = 44;
		Group group = new Group();
		group.setGroupId(gid);
		group.setGroupName(groupName);
		for (User user : users) {
			group.addUser(user);
		}
		group.saveInBackground();

	}

	public static void getGroupsForUser(User user) {
		
		ParseQuery<Group> query = ParseQuery.getQuery("Group");

		// now we will query the users relation to see if the user object we
		// have is contained therein
		query.whereEqualTo("uid", user);
		
		// execute the query
		query.findInBackground(new FindCallback<Group>() {
		    public void done(List<Group> groups, com.parse.ParseException e) {
		    	if ( e == null )
		    		System.out.println(" Number of grups the user belongs to " + groups.size());
		    	else
		    		System.out.println(e.getMessage());
		    }

		});

	}
	
	
	public static void getUsersOfGroup(Group group) {
		ParseRelation<User> relation = group.getRelation("uid");
		ParseQuery<User> query = relation.getQuery();
		query.findInBackground(new FindCallback<User>() {
		    public void done(List<User> users, com.parse.ParseException e) {
		    	if ( e == null )
		    		System.out.println(" Number of grups the user belongs to " + users.size());
		    	else
		    		System.out.println(e.getMessage());
		    }

		});
		
		
	}
	
	
	 
    
 
    /** An interface to be implemented in the hosting activity for "OK" button click listener */
    public interface ParseApiListener {
        public void onallUsersResults(List<User> users);
    }
    
    
	public void getAllUsers() {
		ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.findInBackground(new FindCallback<User>() {
                public void done(List<User> userList, ParseException e) {
                        if (e == null) {
                                // Access the array of results here
                                // String firstItemId = itemList.get(0).getObjectId();
                                // Toast.makeText(TodoItemsActivity.this, firstItemId,
                                // Toast.LENGTH_SHORT).show();
                                Log.d("debug", "size of response: " + userList.size());
                                // Call back here
                                mParseApiListner.onallUsersResults(userList);

                        } else {
                                Log.d("item", "Error: " + e.getMessage());
                        }
                }
        });

	}
	
	public static void getEventsForGroup(Group group) {
		
		
		
		group.getEventRelation().getQuery().findInBackground(new FindCallback<Event>() {
		    public void done(List<Event> events, ParseException e) {
		    	if ( e == null )
		    		Log.d("debug","Number of events for this group: " + events.size());
		    	else
		    		Log.d("debug",e.getMessage());
		    }

		});

	}
	
	

}
