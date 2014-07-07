package com.cpcrew.instadine.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.cpcrew.instadine.R;

public class NewGroupActivity extends Activity {

	private EditText etGroupName;
	private MenuItem miNext;
	private boolean isNextEnabled = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
		etGroupName = (EditText) findViewById(R.id.etGroupName);
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
				if ( !isNextEnabled)
					enableNextButton();
				
			}			
		});
	}
	
	private void disableNextButton() {
		// TODO change the colors appropriate to this theme
		miNext.getIcon().setColorFilter(0x4500aced, PorterDuff.Mode.MULTIPLY);
		miNext.setEnabled(false);
		invalidateOptionsMenu();
		isNextEnabled = false;
	}
	
	private void enableNextButton() {
		miNext.getIcon().clearColorFilter();
		miNext.getIcon().setColorFilter(0xFF00aced, PorterDuff.Mode.MULTIPLY);
		miNext.setEnabled(true);
		isNextEnabled = true;
	}
}
