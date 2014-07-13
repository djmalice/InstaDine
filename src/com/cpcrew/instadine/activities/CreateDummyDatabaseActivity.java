/**
 * 
 */
package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.api.ParseGroupsApi;
import com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.User;

/**
 * @author raji
 *
 */
public class CreateDummyDatabaseActivity extends Activity implements ParseGroupsApiListener {
	
	ParseGroupsApi parseApi;
	ArrayList<User> allUsers;
	String groupName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dummy);
		parseApi = new ParseGroupsApi(this);
		// Get all users
		parseApi.getAllUsers();
		groupName ="group1";
		// Get the group 
		// Create friends
		// add users to group
	}

	/* (non-Javadoc)
	 * @see com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener#onallUsersResults(java.util.List)
	 */
	@Override
	public void onallUsersResults(List<User> users) {
		allUsers = new ArrayList<User>(users);
		System.out.println(" All users = " + users.size());
		// Make them friends of each other
		for ( int i =0; i < users.size(); ++i) {
			User first = users.get(i);
			for (User user: users) {
				System.out.println(first.getObjectId());
				System.out.println(first.getId());
				if ( !(first.getId().equals(user.getId())) ) {
					first.addToFriends(user);
				}
			}
		}
		parseApi.getGroupByName(groupName);
	}
		
	
	/* (non-Javadoc)
	 * @see com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener#onGetUserOfGroupResults(java.util.List)
	 */
	@Override
	public void onGetUserOfGroupResults(List<User> users) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener#onGetGroupsForUserResults(java.util.List)
	 */
	@Override
	public void onGetGroupsForUserResults(List<Group> groups) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener#onGetGroupResult(java.util.List)
	 */
	@Override
	public void onGetGroupResult(List<Group> groups) {
		// Add users to group
		Group group = groups.get(0);
		for( User user : allUsers) {
			group.addUser(user);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener#onGetFriendsResult(java.util.List)
	 */
	@Override
	public void onGetFriendsResult(List<User> friends) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener#retrieveUser(com.cpcrew.instadine.models.User)
	 */
	@Override
	public void retrieveUser(User user) {
		// TODO Auto-generated method stub
		
	}
}
