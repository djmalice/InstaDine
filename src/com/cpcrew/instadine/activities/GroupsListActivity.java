package com.cpcrew.instadine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.fragments.GroupsListFragment;

public class GroupsListActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups_list);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flContainer, new GroupsListFragment());
		ft.commit();

	}
	
	public void onNewGroup(View v) {
		Intent intent = new Intent(this,NewGroupActivity.class);
		startActivity(intent);
	}
}
