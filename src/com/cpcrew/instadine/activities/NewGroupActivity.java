package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.api.ParseGroupsApi;
import com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.User;

public class NewGroupActivity extends Activity implements ParseGroupsApiListener {

	private static String TAG = NewGroupActivity.class.getSimpleName();
	private EditText etGroupName;
	private MenuItem miNext;
	private boolean isNextEnabled = false;
	List<User> users;
	ParseGroupsApi parseApi;
	private Group testGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
		etGroupName = (EditText) findViewById(R.id.etGroupName);
		parseApi = new ParseGroupsApi(this);
		addTextWatcherToGroupName();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_new_group, menu);
		miNext = menu.findItem(R.id.action_next);
		disableNextButton();
		return true;
	}

	/**
	 * OnClick to go to the Next activity to add group participants
	 * 
	 * @param item
	 */
	public void onNext(MenuItem item) {
		// GroupName
		String groupName = etGroupName.getText().toString();
		Intent intent = new Intent(this, AddContactActivity.class);
		intent.putExtra("group_name", groupName);
		startActivity(intent);
	}

	public void addTextWatcherToGroupName() {
		etGroupName.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!isNextEnabled)
					enableNextButton();

			}
		});
	}

	private void disableNextButton() {
		// TODO change the colors appropriate to this theme
		miNext.getIcon().setColorFilter(0x4500aced, PorterDuff.Mode.MULTIPLY);
		miNext.setEnabled(false);
		// invalidateOptionsMenu();
		isNextEnabled = false;
	}

	private void enableNextButton() {
		miNext.getIcon().clearColorFilter();
		miNext.getIcon().setColorFilter(0xFF00aced, PorterDuff.Mode.MULTIPLY);
		miNext.setEnabled(true);
		isNextEnabled = true;
	}
	
	
	public void expDB() {
		//ParseApi.createGroup("TEST3", users);
		parseApi.getGroupByName("TEST3");
	}


	@Override
	public void onallUsersResults(List<User> userList) {
		Log.d( TAG, "onallUsersResults: Number of users : " + userList.size() );
		users = new ArrayList<User>(userList);
		for (User user : users) {
			Log.d(TAG,
					user.getUserName() + "added to group "
							+ testGroup.getGroupName());
			testGroup.addUser(user);
		}
		
	}

	@Override
	public void onGetUserOfGroupResults(List<User> users) {
		Log.d(TAG ," Number of users in the group " + users.size()) ;
//		for (User user : users) {
//			Log.d(TAG, user.getFirstName());
//			testGroup.addUser(user);
//		}
//		for ( int i = 0; i < users.size(); ++i ) {
//			User currentUser = users.get(i);
//			for ( User user: users) {
//				if ( user != currentUser )
//					currentUser.addToFriends(user);
//			}
//		}
		
		
	}

	@Override
	public void onGetGroupsForUserResults(List<Group> groups) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetGroupResult(List<Group> groups) {
		// Fetch the users in the group
		if (groups != null && groups.size() > 0) {
			testGroup = groups.get(0);
			Log.d(TAG, "Group Name" + testGroup.getGroupName());
			parseApi.getAllUsers();
			//parseApi.getUsersOfGroup(testGroup);
		}

	}

	@Override
	public void onGetFriendsResult(List<User> friends) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveUser(User user) {
		// TODO Auto-generated method stub
		
	}
}
