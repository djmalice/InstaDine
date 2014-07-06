package com.cpcrew.instadine.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cpcrew.instadine.R;

public class NewGroupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_new_group, menu);
		return true;
	}

	public void onNext(MenuItem item) {
		Intent intent = new Intent(this, AddContactActivity.class);
		startActivity(intent);
	}
}
