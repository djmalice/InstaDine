package com.cpcrew.instadine.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.fragments.ContactsListFragment;

public class AddContactActivity extends FragmentActivity {
	
	private String groupName;
	private TextView tvAddContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		groupName = getIntent().getStringExtra("group_name");
		setContentView(R.layout.activity_add_contact);
		tvAddContact = (TextView) findViewById(R.id.tvAddContact);
		addTextWatcherToAddContact();
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
		Toast.makeText(getApplicationContext(), "Method 3: Implement Open Contacts List Activity", Toast.LENGTH_SHORT).show();
	}
	
	public void searchByFirstOrLastName(String name) {
		// TODO Open the contactListFragment
		Toast.makeText(getApplicationContext(), "Method 4: Implement - search contacts from DB",Toast.LENGTH_SHORT).show();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flContactsContainer, new ContactsListFragment());
		ft.commit();
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
}
