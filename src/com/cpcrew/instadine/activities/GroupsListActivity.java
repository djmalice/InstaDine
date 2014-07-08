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
		
		/* Get all users in table



        // Define the class we would like to query
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        // Define our query conditions
        // query.whereEqualTo("lastname", ParseUser.getCurrentUser());
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<User>() {
                public void done(List<User> userList, ParseException e) {
                        if (e == null) {
                                // Access the array of results here
                                // String firstItemId = itemList.get(0).getObjectId();
                                // Toast.makeText(TodoItemsActivity.this, firstItemId,
                                // Toast.LENGTH_SHORT).show();
                                Log.d("debug", "size of response: " + userList.size());
                                allUsers.addAll(userList);

                        } else {
                                Log.d("item", "Error: " + e.getMessage());
                        }
                }
        });
        */
		
		
		
		

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flContainer, new GroupsListFragment());
		ft.commit();

	}
	
	public void onNewGroup(View v) {
		Intent intent = new Intent(this,NewGroupActivity.class);
		startActivity(intent);
	}
}
