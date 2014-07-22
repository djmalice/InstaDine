package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cpcrew.instadine.InstaDineApplication;
import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.LoggedInUser;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	private Button loginButton;
	private Dialog progressDialog;
	
	/* used to display a FB profile picture
	 * 
	 * private ProfilePictureView userProfilePictureView;
	 * 
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		// Get access to our TextView
		TextView appTitle = (TextView) findViewById(R.id.app_title);
		TextView appTagLine1 = (TextView) findViewById(R.id.app_tagline1);
		TextView appTagLine2 = (TextView) findViewById(R.id.app_tagline2);
		// Create the TypeFace from the TTF asset
		Typeface QUIGLEYW = Typeface.createFromAsset(getAssets(), "fonts/QUIGLEYW.ttf");
		Typeface SaginawBold = Typeface.createFromAsset(getAssets(), "fonts/SaginawBold.ttf");
		// Assign the typeface to the view
		appTitle.setTypeface(QUIGLEYW);
		appTagLine1.setTypeface(SaginawBold);
		appTagLine2.setTypeface(SaginawBold);


		loginButton = (Button) findViewById(R.id.loginButton);
		
		//final LoginButton button = (LoginButton) findViewById(R.id.login_button);
		//button.setBackgroundResource(R.drawable.button_login);
		
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
			makeMeRequest();
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
		Session session = Session.getActiveSession();
	    if (session != null) {

	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	        }
	    } else {

	        session = new Session(this);
	        Session.setActiveSession(session);

	        session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	    }
		
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
					makeMeRequest();
					newMyFriendsRequest();
					
				} else {
					Log.d(InstaDineApplication.TAG,
							"User logged in through Facebook!");
					makeMeRequest();
					newMyFriendsRequest();
				}
			}
		});
	}

	private void showAppMainActivity() {	
		Intent intent = new Intent(this, GroupsListActivity.class);
		startActivity(intent);	
		
		//removed login screen from backstack
		finish();
		
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
	
	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							// Create a JSON object to hold the profile info
							JSONObject userProfile = new JSONObject();
							try {
								// Populate the JSON object
								userProfile.put("facebookId", user.getId());
								userProfile.put("name", user.getName());
								if ( user.getLocation() != null ) {
									if (user.getLocation().getProperty("name") != null) {
										userProfile.put("location", (String) user
											.getLocation().getProperty("name"));
									
									}
									else userProfile.put("location", "");
								}
								if (user.getProperty("gender") != null) {
									userProfile.put("gender",
											(String) user.getProperty("gender"));
								}
								if (user.getBirthday() != null) {
									userProfile.put("birthday",
											user.getBirthday());
								}
								if (user.getProperty("relationship_status") != null) {
									userProfile
											.put("relationship_status",
													(String) user
															.getProperty("relationship_status"));
								}
								
								// Save the user profile info in a user property
								ParseUser currentUser = ParseUser
										.getCurrentUser();
								currentUser.put("profile", userProfile);
								currentUser.put("facebookid", user.getId());
								//currentUser.put("username", user.getName().toString());
								currentUser.put("first" , user.getFirstName());
								currentUser.put("last", user.getLastName());
								currentUser.saveInBackground();

								
								
							} catch (JSONException e) {
								Log.d(InstaDineApplication.TAG,
										"Error parsing returned user data.");
							}

						} else if (response.getError() != null) {
							if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
									|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
								Log.d(InstaDineApplication.TAG,
										"The facebook session was invalidated.");
								// Log the user out
								ParseUser.logOut();
							} else {
								Log.d(InstaDineApplication.TAG,
										"Some other error: "
												+ response.getError()
														.getErrorMessage());
							}
						}
					}
				});
		request.executeAsync();

	}
	
	/* This block is used to display user profile information
	 * Not needed in our app, but leaving for sample usage, espcially FB profile pictureview
	 * 
	private void updateViewsWithProfileInfo() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser.get("profile") != null) {
			JSONObject userProfile = currentUser.getJSONObject("profile");
			try {
				if (userProfile.getString("facebookId") != null) {
					String facebookId = userProfile.get("facebookId")
							.toString();
					userProfilePictureView.setProfileId(facebookId);
				} else {
					// Show the default, blank user profile picture
					userProfilePictureView.setProfileId(null);
				}
				if (userProfile.getString("name") != null) {
					userNameView.setText(userProfile.getString("name"));
				} else {
					userNameView.setText("");
				}
				if (userProfile.getString("location") != null) {
					userLocationView.setText(userProfile.getString("location"));
				} else {
					userLocationView.setText("");
				}
				if (userProfile.getString("gender") != null) {
					userGenderView.setText(userProfile.getString("gender"));
				} else {
					userGenderView.setText("");
				}
				if (userProfile.getString("birthday") != null) {
					userDateOfBirthView.setText(userProfile
							.getString("birthday"));
				} else {
					userDateOfBirthView.setText("");
				}
				if (userProfile.getString("relationship_status") != null) {
					userRelationshipView.setText(userProfile
							.getString("relationship_status"));
				} else {
					userRelationshipView.setText("");
				}
			} catch (JSONException e) {
				Log.d(InstaDineApplication.TAG,
						"Error parsing saved user data.");
			}
		}
	}
	*/

}
