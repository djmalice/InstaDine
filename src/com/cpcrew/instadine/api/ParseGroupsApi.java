package com.cpcrew.instadine.api;

import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

public class ParseGroupsApi {
	
	/** Declaring the interface, to invoke a callback function in the implementing activity class */
	ParseGroupsApiListener mParseApiListner;
	public ParseGroupsApi(Activity activity) {
		mParseApiListner = (ParseGroupsApiListener) activity;
	}

	/**
	 * Create a new Group
	 * @param currentUser (who will be the group admin)
	 * @param groupName
	 * @param users
	 */
	public static void createGroup(String groupName, List<User> users, User currentUser) {
		Group group = new Group();
		group.setGroupName(groupName);
		for (User user : users) {
			group.addUser(user);
		}
		group.saveInBackground();

	}

	/**
	 * Get groups the user belongs to
	 * callback to return a list of users
	 * @param user
	 */
	public void getGroupsForUser(User user) {
		
		ParseQuery<Group> query = ParseQuery.getQuery(Group.class);

		// now we will query the users relation to see if the user object we
		// have is contained therein
		query.whereEqualTo("uid", user);
		
		// execute the query
		query.findInBackground(new FindCallback<Group>() {
		    public void done(List<Group> groups, com.parse.ParseException e) {
		    	if ( e == null ) {
		    		System.out.println(" getGroupsForUser: Number of grups the user belongs to " + groups.size());
		    		mParseApiListner.onGetGroupsForUserResults(groups);
		    	}
		    	
		    	else
		    		System.out.println(e.getMessage());
		    }

		});

	}
	
	/**
	 * Get the list of users belonging to a specific group
	 * callback to return list of users
	 * @param group
	 */
	public void getUsersOfGroup(Group group) {
		ParseRelation<User> relation = group.getRelation("uid");
		ParseQuery<User> query = relation.getQuery();
		query.findInBackground(new FindCallback<User>() {
		    public void done(List<User> users, com.parse.ParseException e) {
		    	if ( e == null ) {
		    		System.out.println(" Number of grups the user belongs to " + users.size());
		    		mParseApiListner.onGetUserOfGroupResults( users);
		    	}
		    	else
		    		System.out.println(e.getMessage());
		    }

		});
		
		
	}
	
 
    /** An interface to be implemented in the hosting activity  */
    public interface ParseGroupsApiListener {
        public void onallUsersResults(List<User> users);
        public void onGetUserOfGroupResults(List<User> users);
        public void onGetGroupsForUserResults(List<Group> groups);
        public void onGetGroupResult(List<Group> group);
        public void onGetFriendsResult(List<User> friends);
        public void retrieveUser(User user);
    }
    
    /**
     * Search for a group by a group name
     * callback to return a list of groups
     * @param groupName
     */
    public void getGroupByName( String groupName ) {
    	System.out.println("Get group");
    	ParseQuery<Group> query = ParseQuery.getQuery(Group.class);
    	query.whereEqualTo("gname", groupName);
		// execute the query
		query.findInBackground(new FindCallback<Group>() {
		    public void done(List<Group> groups, com.parse.ParseException e) {
		    	if ( e == null )
		    		mParseApiListner.onGetGroupResult(groups);
		    	else
		    		System.out.println(e.getMessage());
		    }

		});
    }
    
    
    /**
     * Get all users in the DB
     */
	public void getAllUsers() {
		ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.findInBackground(new FindCallback<User>() {
                public void done(List<User> userList, ParseException e) {
                        if (e == null) {
                                Log.d("debug", "size of response: " + userList.size());
                                // Call back here
                                mParseApiListner.onallUsersResults(userList);

                        } else {
                                Log.d("item", "Error: " + e.getMessage());
                        }
                }
        });

	}
	

	public void getFriendsOfUser(User user) {
		ParseRelation<User> relation = user.getRelation("friends");
		ParseQuery<User> query = relation.getQuery();
		query.findInBackground(new FindCallback<User>() {
		    public void done(List<User> users, com.parse.ParseException e) {
		    	if ( e == null ) {
		    		System.out.println(" Number of friends " + users.size());
		    		mParseApiListner.onGetFriendsResult( users);
		    	}
		    	else
		    		System.out.println(e.getMessage());
		    }

		});
		
	}
	
	/**
	 * Search for a friend of the current user by partial first name
	 * @param partialName
	 * @param currentUser
	 */
	public void findFriend(String partialName, User currentUser) {
		ParseRelation<User> relation = currentUser.getRelation("friends");
		ParseQuery<User> query = relation.getQuery();
		query.whereStartsWith("first", partialName);
		query.findInBackground(new FindCallback<User>() {
		    public void done(List<User> users, com.parse.ParseException e) {
		    	if ( e == null ) {
		    		System.out.println(" Number of friends " + users.size());
		    		mParseApiListner.onGetFriendsResult( users);
		    	}
		    	else
		    		System.out.println(e.getMessage());
		    }

		});
	}
	
	
	// mainly for testing
	public void retrieveUser(String objectId) {
		ParseQuery<User> query = ParseQuery.getQuery(User.class);
		query.getInBackground(objectId, new GetCallback<User>() {
		  public void done(User user, ParseException e) {
		    if (e == null) {
		      mParseApiListner.retrieveUser(user);
		    } else {
		    	System.out.println("Retrieve user :" + e.getMessage());
		    }
		  }
		});
	}

}
