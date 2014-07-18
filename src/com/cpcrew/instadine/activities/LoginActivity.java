package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.cpcrew.instadine.InstaDineApplication;
import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.LoggedInUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	private Button loginButton;
	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});
		 
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		/* Add code to print out the key hash
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.cpcrew.instadine.activities", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }*/
		
		// Check if there is a currently logged in user
		// and they are linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
			loginButton.setVisibility(View.GONE);
			newMyFriendsRequest();
		} else if (currentUser != null) {
			loginButton.setVisibility(View.FOCUS_FORWARD);
		}
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);

	}

	private void onLoginButtonClicked() {
		LoginActivity.this.progressDialog = ProgressDialog.show(
				LoginActivity.this, "", "Logging in...", true);
		List<String> permissions = Arrays.asList("public_profile", "user_about_me",
				"user_relationships", "user_birthday", "user_location", "user_friends");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				LoginActivity.this.progressDialog.dismiss();
				if (user == null) {
					
					// debug comment to return keyhash to put into developer settings
					Log.d(InstaDineApplication.TAG, err.getLocalizedMessage());
					Log.d(InstaDineApplication.TAG,
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d(InstaDineApplication.TAG,
							"User signed up and logged in through Facebook!");
					newMyFriendsRequest();
				} else {
					Log.d(InstaDineApplication.TAG,
							"User logged in through Facebook!");
					newMyFriendsRequest();
				}
			}
		});
	}

	private void showAppMainActivity() {	
		Intent intent = new Intent(this, GroupsListActivity.class);
		startActivity(intent);		
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
				            	 System.out.println(user.getId());
				            	 // What will I do with a name that is not present in the profile !!! Ridiculous
				                //friendsList.add(user.getName());
				            	 friendsList.add(user.getId());
				                //wToast.makeText(getApplicationContext(), friendsList.toString(), Toast.LENGTH_SHORT).show();
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
			             
			             // load this after above async event completes
			             showAppMainActivity();
			    }
			});
		request.executeAsync();
	}

}
