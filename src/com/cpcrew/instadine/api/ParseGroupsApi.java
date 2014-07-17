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
import com.parse.ParseUser;

/**
* APIs to read, write and query from Parse
* for Groups and Users
*
* @author  Rajalakshmi Balakrishnan
* @version 1.0
* @since   2014-07-07 
*/

public class ParseGroupsApi {
	
	/** Declaring the interface, to invoke a callback function in the implementing activity class */
	ParseGroupsApiListener mParseApiListner;
	private static String TAG = ParseGroupsApi.class.getSimpleName();
	
	
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
		StringBuffer desc = new StringBuffer();
		group.setGroupName(groupName);
		for (User user : users) {
			group.addUser(user);
			desc.append(user.getFirstName() + ", ");
		}
		String str = desc.toString();
		str = str.replaceAll(" ,$", "");
		group.setDesc(str);
		Log.d(TAG, "Handle groupadmin");
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
		query.whereEqualTo("users", user.getParseUser());
		Log.d(TAG, "Starting async getGroupsForUser");
		// execute the query
		query.findInBackground(new FindCallback<Group>() {
		    public void done(List<Group> groups, com.parse.ParseException e) {
		    	if ( e == null ) {
		    		System.out.println(" getGroupsForUser: Number of grups the user belongs to " + groups.size());
		    		mParseApiListner.onGetGroupsForUserResults(groups);
		    	}
		    	
		    	else
		    		System.out.println("TEST" + e.getMessage());
		    }

		});

	}
	
	
//	public void getGroupsForUser(User user) {
//		
//		ParseRelation<Group> relation = user.getRelation("gid");
//		ParseQuery<Group> query = relation.getQuery();
//		Log.d(TAG, "Starting async getGroupsForUser");
//		// execute the query
//		query.findInBackground(new FindCallback<Group>() {
//		    public void done(List<Group> groups, com.parse.ParseException e) {
//		    	if ( e == null ) {
//		    		System.out.println(" getGroupsForUser: Number of grups the user belongs to " + groups.size());
//		    		mParseApiListner.onGetGroupsForUserResults(groups);
//		    	}
//		    	
//		    	else
//		    		System.out.println("TEST" + e.getMessage());
//		    }
//
//		});
//
//	}
	
	/**
	 * Get the list of users belonging to a specific group
	 * callback to return list of users
	 * @param group
	 */
	public void getUsersOfGroup(Group group) {
		ParseRelation<ParseUser> relation = group.getRelation("users");
		ParseQuery<ParseUser> query = relation.getQuery();
		Log.d(TAG, "Starting async getUsersOfGroup");
		query.findInBackground(new FindCallback<ParseUser>() {
		    public void done(List<ParseUser> users, com.parse.ParseException e) {
		    	if ( e == null ) {
		    		System.out.println(" Number of grups the user belongs to " + users.size());
		    		
		    		mParseApiListner.onGetUserOfGroupResults( User.wrapParseUsers(users));
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
    	Log.d(TAG, "Starting async getGroupByName");
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
		Log.d(TAG, "Starting async getAllUsers");
		ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> userList, ParseException e) {
                        if (e == null) {
                                Log.d("debug", "size of response: " + userList.size());
                                // Call back here
                                mParseApiListner.onallUsersResults(User.wrapParseUsers(userList));

                        } else {
                                Log.d("item", "Error: " + e.getMessage());
                        }
                }
        });

	}
	

	/**
	 * Get friends of the user
	 * callback to return a list of users who are friends of input user.
	 * @param user 
	 */
	public void getFriendsOfUser(User user) {
		Log.d(TAG, "Starting async getFriendsOfUser");
		ParseRelation<ParseUser> relation = user.getParseUser().getRelation("pfriends");
		ParseQuery<ParseUser> query = relation.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
		    public void done(List<ParseUser> users, com.parse.ParseException e) {
		    	if ( e == null ) {
		    		System.out.println(" Number of friends " + users.size());
		    		List<User> us = User.wrapParseUsers(users);
		    		for (User u : us) {
		    			System.out.println("ff " + u.getId());
		    		}
		    		mParseApiListner.onGetFriendsResult( us);
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
		Log.d(TAG, "Starting async findFriend");
		ParseRelation<ParseUser> relation = currentUser.getParseUser().getRelation("pfriends");
		ParseQuery<ParseUser> query = relation.getQuery();
		query.whereStartsWith("first", partialName);
		query.findInBackground(new FindCallback<ParseUser>() {
		    public void done(List<ParseUser> users, com.parse.ParseException e) {
		    	if ( e == null ) {
		    		System.out.println(" Number of friends " + users.size());
		    		mParseApiListner.onGetFriendsResult( User.wrapParseUsers(users));
		    	}
		    	else
		    		System.out.println(e.getMessage());
		    }

		});
	}
	
	
	/**
	 * Get the user object given the objectId of the user.
	 * @param objectId
	 */
	public void retrieveUser(String objectId) {
		Log.d(TAG, "Starting async retrieveUser");
		ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
		query.getInBackground(objectId, new GetCallback<ParseUser>() {
		  public void done(ParseUser user, ParseException e) {
		    if (e == null) {
		      mParseApiListner.retrieveUser(User.wrapParseUser(user));
		    } else {
		    	System.out.println("Retrieve user :" + e.getMessage());
		    }
		  }

		});
	}
	
//	public void retrieveUserWithFacebookId(String facebookId) {
//		Log.d(TAG, "Starting async retrieveUserWithFacebookId");
//		ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
//		query.getInBackground(facebookId, new GetCallback<ParseUser>() {
//		  public void done(ParseUser user, ParseException e) {
//		    if (e == null) {
//		      mParseApiListner.retrieveUser(User.wrapParseUser(user));
//		    } else {
//		    	System.out.println("Retrieve user :" + e.getMessage());
//		    }
//		  }
//
//		});
//	}
	

	
	
}
