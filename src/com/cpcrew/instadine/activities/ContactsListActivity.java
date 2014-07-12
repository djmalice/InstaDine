package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.api.ParseGroupsApi;
import com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener;
import com.cpcrew.instadine.fragments.ContactsListFragment;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.LoggedInUser;
import com.cpcrew.instadine.models.User;
import com.cpcrew.instadine.utils.Constants;

public class ContactsListActivity extends FragmentActivity implements ParseGroupsApiListener {
	
	ContactsListFragment mFragment;
	private ParseGroupsApi  parseGroupsApiListener;
	private static String TAG = ContactsListActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_list);	
		parseGroupsApiListener = new ParseGroupsApi(this);
		mFragment = new ContactsListFragment();
		Bundle args = new Bundle();
        args.putStringArrayList("selectedusers", null);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flContainer, mFragment);
		mFragment.setArguments(args);
		ft.commit();
		showContacts();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_contact_list, menu);
		return true;
	}
	
	public void onDoneWithSelection(MenuItem item) {
		// send the selection info
		ArrayList<Integer> selUsers = mFragment.getCheckedContactsIds();
		Intent intent = new Intent(this,AddContactActivity.class);
		intent.putIntegerArrayListExtra("selusers", selUsers);
		startActivity(intent);
	}
	
	public void showContacts() {
		User currentUser = LoggedInUser.getcurrentUser();
		if ( currentUser != null )
			parseGroupsApiListener.getFriendsOfUser(LoggedInUser.getcurrentUser());
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
	
		
	}

	@Override
	public void onGetGroupResult(List<Group> group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetFriendsResult(List<User> friends) {
		mFragment.setContactAdapter(friends);
		
	}

	@Override
	public void retrieveUser(User user) {		
	}
}
