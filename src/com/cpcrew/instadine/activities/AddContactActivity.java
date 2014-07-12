package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;
import android.widget.Toast;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.api.ParseGroupsApi;
import com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener;
import com.cpcrew.instadine.fragments.ContactsListFragment;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.User;
import com.cpcrew.instadine.utils.Constants;

public class AddContactActivity extends FragmentActivity implements ParseGroupsApiListener {
	
	private static String TAG = AddContactActivity.class.getSimpleName();
	private String groupName;
	private TextView tvAddContact;
	ArrayList<User> selectedUsers;
	ParseGroupsApi parseApi;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		groupName = getIntent().getStringExtra("group_name");
		setContentView(R.layout.activity_add_contact);
		tvAddContact = (TextView) findViewById(R.id.tvAddContact);
		selectedUsers = new ArrayList<User>();
		addTextWatcherToAddContact();
		parseApi = new ParseGroupsApi(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add_contact, menu);
		return true;
	}
	
	public void onCreateGroup(MenuItem item) {
		Toast.makeText(getApplicationContext(), "Method 2: Implement Create Group(). Requires Models", Toast.LENGTH_SHORT).show();
	}

	public void onOpenContacts(View v) {
		//User.pinAllInBackground("selectedUsers", selectedUsers);
		Intent intent = new Intent(this, ContactsListActivity.class);
		startActivityForResult(intent, Constants.REQUEST_CODE);
	}
	
	public void searchByFirstOrLastName(String name) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flContactsContainer, new ContactsListFragment());
		ft.commit();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // REQUEST_CODE is defined above
	  if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE) {
	     // Extract name value from result extras
	     ArrayList<Integer>userids = data.getIntegerArrayListExtra("selusers");
	     Log.d(TAG, "Num selected users " + userids.size());
	     // fetch the users for the userids
	     // display on the fragment
	     Toast.makeText(getApplicationContext(), "Method 3: display results", Toast.LENGTH_SHORT).show();
	  }
	} 
	
	
	public void addTextWatcherToAddContact() {
		tvAddContact.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				searchByFirstOrLastName("");
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
	
	public void fetchUserWithIds(ArrayList<String> userids) {
		for ( String id : userids) {
			parseApi.retrieveUser(id);
		}
	}

	/* (non-Javadoc)
	 * @see com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener#onallUsersResults(java.util.List)
	 */
	@Override
	public void onallUsersResults(List<User> users) {
		// TODO Auto-generated method stub
		
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
	public void onGetGroupResult(List<Group> group) {
		// TODO Auto-generated method stub
		
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
		if ( user != null )
			selectedUsers.add(user);
	}
	
	
}