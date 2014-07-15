package com.cpcrew.instadine.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.api.ParseGroupsApi;
import com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener;
import com.cpcrew.instadine.fragments.GroupsListFragment;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.LoggedInUser;
import com.cpcrew.instadine.models.User;

public class GroupsListActivity extends FragmentActivity implements ParseGroupsApiListener {
	
	GroupsListFragment mFragment;
	private ParseGroupsApi  parseApi;
	private static String TAG = GroupsListActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups_list);	
		parseApi = new ParseGroupsApi(this);
		mFragment = new GroupsListFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flContainer, mFragment);
		ft.commit();
		testGetGroups();

	}

	
	public void testGetGroups() {
		// Get current User
		// TODO lets hardcode since login interface missing
		parseApi.retrieveUser("ftATYWIhjA");
	}
	
	public void onNewGroup(View v) {
		Intent intent = new Intent(this,NewGroupActivity.class);
		startActivity(intent);
	}
	
	public void showEvent(String groupId) {
		// Open the existing event if present
		// else create a new event.
		Intent intent = new Intent(this, VotingActivity.class);
		intent.putExtra("group_id", groupId);
		startActivity(intent);
	}
	
	@Override
	public void onallUsersResults(List<User> users) {
		
	}

	@Override
	public void onGetUserOfGroupResults(List<User> users) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetGroupsForUserResults(List<Group> groups) {
		// Update the listView
		Log.d(TAG, " returned to onGetGroupsForUserResults");
		mFragment.setGroupsAdapter(groups);
		
	}

	@Override
	public void onGetGroupResult(List<Group> group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetFriendsResult(List<User> friends) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveUser(User user) {
		// Now get the groups the user belongs to
		Log.d(TAG, "Retrieved user " + user.getFirstName());
		if (user != null) {
			LoggedInUser.setCurrentUser(user);
			parseApi.getGroupsForUser(user);
		}

	}
	
}
