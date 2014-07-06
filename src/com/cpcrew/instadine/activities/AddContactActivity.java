package com.cpcrew.instadine.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cpcrew.instadine.R;

public class AddContactActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add_contact, menu);
		return true;
	}
	
	public void onCreateGroup(MenuItem item) {
		Toast.makeText(getApplicationContext(), "Implement Create Group(). Requires Models", Toast.LENGTH_SHORT).show();
	}

	public void onOpenContacts(View v) {
		Toast.makeText(getApplicationContext(), "Implement Open Contacst List", Toast.LENGTH_SHORT).show();
	}
}
