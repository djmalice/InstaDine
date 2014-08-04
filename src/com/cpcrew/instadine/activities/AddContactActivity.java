package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.api.ParseGroupsApi;
import com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener;
import com.cpcrew.instadine.fragments.ContactsListFragment;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.LoggedInUser;
import com.cpcrew.instadine.models.User;
import com.cpcrew.instadine.utils.Constants;

public class AddContactActivity extends FragmentActivity implements
		ParseGroupsApiListener {

	private static String TAG = AddContactActivity.class.getSimpleName();
	private String groupName;
	private AutoCompleteTextView tvAddContact;
	ArrayList<User> selectedUsers;
	ArrayList<String> selectedUserids; // for transferring between
										// activities/fragments
	HashSet<String> selectedSet;
	ParseGroupsApi parseApi;
	ContactsListFragment mFragment;
	ArrayAdapter<String> autoCompleteAdapter;
	ArrayList<String> autocompleteList;
	boolean isActivityResult = false;
	Map<String, User> friendsMap;
	ImageButton userButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get from previous activity
		groupName = getIntent().getStringExtra("group_name");
		setContentView(R.layout.activity_add_contact);
		tvAddContact = (AutoCompleteTextView) findViewById(R.id.tvAddContact);
		addTextWatcherToAddContact();

		// initialize
		parseApi = new ParseGroupsApi(this);
		selectedUsers = new ArrayList<User>();
		selectedUserids = new ArrayList<String>();
		selectedSet = new HashSet<String>();

		showFragment();
		showSelectedContacts();
		userButton = (ImageButton)findViewById(R.id.btnOpenContacts);
		tada(userButton).start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add_contact, menu);
		return true;
	}

	// Create a group
	public void onCreateGroup(MenuItem item) {
		if (selectedUsers != null && selectedUsers.size() > 0) {
			selectedUsers.add(LoggedInUser.getcurrentUser());
			ParseGroupsApi.createGroup(groupName, selectedUsers,
					LoggedInUser.getcurrentUser());
		}
		// Now go back to Groups list screen
		Intent intent = new Intent(this, GroupsListActivity.class);
		startActivity(intent);
	}

	// Open contacts list
	public void onOpenContacts(View v) {
		// CacheApi.cacheSelectedUsers(selectedUsers);

		Intent intent = new Intent(this, ContactsListActivity.class);
		intent.putStringArrayListExtra("selectedusers", selectedUserids);
		startActivityForResult(intent, Constants.REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Coming from the next screen (ContactsList selection )
		// REQUEST_CODE is defined above
		if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE) {
			// Extract name value from result extras
			isActivityResult = true;
			selectedUserids = data.getStringArrayListExtra("selusers");
			Log.d(TAG, "Num selected users back from ContactList "
					+ selectedUserids.size());
			showSelectedContacts();
		}
	}

	public void showSelectedContacts() {
		User currentUser = LoggedInUser.getcurrentUser();
		if (currentUser != null) {
			parseApi.getFriendsOfUser(currentUser);
		}
	}

	public void addTextWatcherToAddContact() {
		tvAddContact.addTextChangedListener(new TextWatcher() {

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
				
			}
		});
	}

	public void showFragment() {
		mFragment = new ContactsListFragment();
		Bundle args = new Bundle();
		args.putStringArrayList("selectedusers", selectedUserids);
		args.putString("parent_activity", Constants.GROUP_ADD_CONTACT);
		mFragment.setArguments(args);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flContactsContainer, mFragment);
		ft.commit();
	}

	@Override
	public void onallUsersResults(List<User> users) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetUserOfGroupResults(List<User> users) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetGroupsForUserResults(List<Group> groups) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetGroupResult(List<Group> group) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetFriendsResult(List<User> friends) {
		if (isActivityResult) {
			// Translate into user objects
			if (selectedUserids != null) {
				HashSet<String> set = new HashSet<String>(selectedUserids);
				selectedSet.clear();
				selectedSet.addAll(set);
				selectedUsers.clear();
			}
			for (User user : friends) {
				if (selectedSet.contains(user.getId()))
					selectedUsers.add(user);
			}

			// show the results in fragment
			mFragment.setContactAdapter(selectedUsers, selectedSet);
			isActivityResult = false;
		} else {
			// Search Autocomplete
			// Maybe first name will be screen Name instead
			friendsMap = new HashMap<String, User>();
			autocompleteList = new ArrayList<String>();
			for (User user : friends) {
				String name = user.getFirstName() + " " + user.getLastName();

				friendsMap.put(name, user);
				autocompleteList.add(name);
			}
			autoCompleteAdapter = new ArrayAdapter<String>(
					AddContactActivity.this,
					android.R.layout.simple_dropdown_item_1line,
					autocompleteList);
			tvAddContact.setAdapter(autoCompleteAdapter);

			// ON selection of item
			tvAddContact.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> adapterView, View view,
						int pos, long arg3) {
					String name = (String) adapterView.getItemAtPosition(pos);
					User user = friendsMap.get(name);
					selectedUsers.add(user);
					selectedUserids.add(user.getId());
					selectedSet.add(user.getId());
					mFragment.setContactAdapter(selectedUsers, selectedSet);

				}
			});
		}

	}

	@Override
	public void retrieveUser(User user) {
		// TODO Auto-generated method stub

	}

	public static ObjectAnimator tada(View view) {
		return tada(view, 2f);
	}

	public static ObjectAnimator tada(View view, float shakeFactor) {

		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(
				View.SCALE_X, Keyframe.ofFloat(0f, 1f),
				Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
				Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
				Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
				Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
				Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

		PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(
				View.SCALE_Y, Keyframe.ofFloat(0f, 1f),
				Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
				Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
				Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
				Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
				Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

		PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(
				View.ROTATION, Keyframe.ofFloat(0f, 0f),
				Keyframe.ofFloat(.1f, -3f * shakeFactor),
				Keyframe.ofFloat(.2f, -3f * shakeFactor),
				Keyframe.ofFloat(.3f, 3f * shakeFactor),
				Keyframe.ofFloat(.4f, -3f * shakeFactor),
				Keyframe.ofFloat(.5f, 3f * shakeFactor),
				Keyframe.ofFloat(.6f, -3f * shakeFactor),
				Keyframe.ofFloat(.7f, 3f * shakeFactor),
				Keyframe.ofFloat(.8f, -3f * shakeFactor),
				Keyframe.ofFloat(.9f, 3f * shakeFactor),
				Keyframe.ofFloat(1f, 0));

		return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX,
				pvhScaleY, pvhRotate).setDuration(1000);
	}

}