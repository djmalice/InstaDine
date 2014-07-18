package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.LoggedInUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class UserDetailsActivity extends Activity {

	private Button logoutButton;
	private Button continueButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_user_details);

		logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogoutButtonClicked();
			}
		});
			continueButton = (Button) findViewById(R.id.continueButton);
			continueButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				oncontinueButtonClicked();
			}			
		});
			
		//Toast.makeText(getApplicationContext(), "UserDetailsActivity, runs code for friend extraction.  " +
	    //			"Contains useful Facebook sample code.  Now showing all friends...." , Toast.LENGTH_LONG).show();

		// Fetch Facebook user info if the session is active
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			newMyFriendsRequest();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// Check if the user is currently logged
			// and show any cached content
			// updateViewsWithProfileInfo();
			// oncontinueButtonClicked();
		} else {
			// If the user is not logged in, go to the
			// activity showing the login view.
			startLoginActivity();
		}
	}

	private void newMyFriendsRequest() {
		Request request = Request.newMyFriendsRequest(ParseFacebookUtils.getSession(), 
				new Request.GraphUserListCallback() {
				    @Override
				    public void onCompleted(List<GraphUser> users, Response response) {
				         Log.d("DEBUG", "Request Completed! " + response.toString() + "Total number of friends: " + users.size());
				      
			             if (users != null) {
				             List<String> friendsList = new ArrayList<String>();
				             for (GraphUser user : users) {		
				            	 friendsList.add(user.getId());				          
				             }
				             ParseUser currentUser = ParseUser
										.getCurrentUser();
				             
				             	// friends array key specified in existing _user class
								currentUser.put("friendsfb", friendsList);
								currentUser.saveInBackground();
								LoggedInUser.setCurrentUser(currentUser);
								
//								 ParseQuery friendQuery = ParseQuery.getQuery();
//							     friendQuery.whereContainedIn("fbId", friendsList);
//
//							      // findObjects will return a list of ParseUsers that are friends with
//							      // the current user
//							      try {
//									List<ParseObject> friendUsers = friendQuery.find();
//									if ( friendUsers == null )
//										System.out.println("Friedn users null");
//									else
//										System.out.println("friend " + friendUsers.size());
//								} catch (ParseException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
								
								/*
								 * friends array in class format
								 *
								ParseObject allFriends = new ParseObject("AllFriends");
								allFriends.put("friends_list", friendsList);
								allFriends.saveInBackground();
								*
								*/								
				         }
			    }
			});
		request.executeAsync();
	}
	
	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();
		
		// Go to the login view
		startLoginActivity();
	}
	
	// continue the app flow
	private void oncontinueButtonClicked() {
		Intent intent = new Intent(this, GroupsListActivity.class);
		startActivity(intent);
	}

	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
