package com.cpcrew.instadine.activities;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.api.ParseGroupsApi;
import com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener;
import com.cpcrew.instadine.fragments.GroupsListFragment;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.LoggedInUser;
import com.cpcrew.instadine.models.User;
import com.facebook.Session;
import com.facebook.internal.Utility;
import com.parse.ParseUser;

public class GroupsListActivity extends FragmentActivity implements eu.erikw.PullToRefreshListView.OnRefreshListener,ParseGroupsApiListener {
	
	private Button logoutButton;
	
	GroupsListFragment mFragment;
	private ParseGroupsApi  parseApi;
	private static String TAG = GroupsListActivity.class.getSimpleName();
	HashMap<String, User> allUsers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups_list);	
		
		logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogoutButtonClicked();
			}
		});
		
		// keep logout button invisible for now
		logoutButton.setVisibility(View.GONE);
		
		parseApi = new ParseGroupsApi(this);
		mFragment = new GroupsListFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flContainer, mFragment);
		ft.commit();
		allUsers = new HashMap<String, User>();
		parseApi.getAllUsers();
		parseApi.getGroupsForUser(LoggedInUser.getcurrentUser());
		//testGetGroups();

	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onLogoutAction(MenuItem mi) {
		// Log the user out
		ParseUser.logOut();
		
		// Go to the login view
		startLoginActivity();
     }

	
	@Override
	public void onResume() {
		super.onResume();

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// Check if the user is currently logged
			// and show any cached content
			// updateViewsWithProfileInfo();
			// oncontinueButtonClicked();
		} else {
			// If the user is not logged in, go to the
			// activity showing the login view.
			startLoginActivity();
		}
	}

	
	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();
		
		// Go to the login view
		startLoginActivity();
	}
	
	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
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
		System.out.println("Number of users in Db " + users.size());
		for ( User user : users) {
			allUsers.put(user.getFacebookId(),user);
		}
		if ( LoggedInUser.getcurrentUser() != null) {
			List<String> friendsfb = LoggedInUser.getcurrentUser().getFacebookFriendsIds();
			for ( String id : friendsfb) {
				System.out.println("fB friend " + id);
				if (allUsers.containsKey(id)) {
					User user = allUsers.get(id);
					LoggedInUser.getcurrentUser().addToFriends(user);
				}				
			}
		}
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
		Log.d(TAG, "Retrieved user " + user.getUserName());
		if (user != null) {
			//LoggedInUser.setCurrentUser(user);//  doesnt work ParseUser change XXX
			parseApi.getGroupsForUser(user);
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		parseApi.getGroupsForUser(LoggedInUser.getcurrentUser());
	}
	
}
