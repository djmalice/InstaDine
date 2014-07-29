package com.cpcrew.instadine.activities;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.LoggedInUser;
import com.cpcrew.instadine.models.User;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class VotingActivityReceiver extends BroadcastReceiver {
   private static final String TAG = "VotingActivityReceiver";
   private static final int REQUEST_CODE = 11;
   public static final String intentAction = "SEND_PUSH";
   public static final String intentPushNewRestaurant = "SEND_REST";
   public static final String intentPushUpdateVotes = "SEND_VOTES";
   public static final int NOTIFICATION_ID = 45;

   private String organizer;
   private String groupName;
   private String groupId;
   private String restName;
   //private ArrayList<String> firstNames = new ArrayList<String>();
   private JSONArray firstNamesJSON = new JSONArray();
   private ArrayList<String> firstNames;

   @Override
   public void onReceive(Context context, Intent intent) {

       if (intent == null) {
           Log.d(TAG, "Receiver intent null");
       } else if (intent.getIntExtra("notificationId", 0) == 77) {
    	   
           int notificationId = intent.getIntExtra("notificationId", 0);
           NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
           manager.cancel(notificationId);
           //Toast.makeText(context, "onReceive VotingActivity invoked for ID " + notificationId, Toast.LENGTH_LONG).show();
       }
       else {
           
    	   	// Parse push message and handle accordingly
    	   	processPush(context, intent);
       		}
      }

   
   /*
    * The Intent object which is passed to the receiver contains an extras Bundle with two useful mappings. 
    * The "com.parse.Channel" key points to a string representing the channel that the message was sent on. 
    * The "com.parse.Data" key points to a string representing the JSON-encoded value of the "data" 
    * dictionary that was set in the push notification.
    */
   private void processPush(Context context, Intent intent) {
       String action = intent.getAction();
       Log.d(TAG, "got action " + action );
       if (action.equals(intentAction) || action.equals(intentPushNewRestaurant) || action.equals(intentPushUpdateVotes))
       {
           String channel = intent.getExtras().getString("com.parse.Channel");
           try {       	
               JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
               Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
               // Iterate the parse keys if needed
               Iterator<String> itr = json.keys();
               while (itr.hasNext()) {
                   String key = (String) itr.next();
	         	   // Extract custom push data
                   if (key.equals("restname")) {
                	   restName = json.getString(key);
                   } else if (key.equals("currentuser")) {
	         	    	organizer = json.getString(key);
	         	   	} else if (key.equals("groupid")) {    
	         	   		groupId = json.getString(key);
	         	    } else if (key.equals("groupname")) {
	         	    	groupName = json.getString(key);
	         	    } else if (key.equals("otherusers")) { 
	         	    	firstNamesJSON = json.getJSONArray(key);
	         	    	Log.d(TAG, "..." + key + " => " + json.getJSONArray(key));
	         	    	
	         	    	firstNames = new ArrayList<String>();
	         	    	
	        			for (int x=0; x < firstNamesJSON.length(); x++) {
	        				//firstNames.set(x, firstNamesJSON.getString(x));
	        				firstNames.add(firstNamesJSON.getString(x));
	        			} if (action.equals(intentAction)) {
	        				triggerBroadcastToActivity(context);
	        			} else if (action.equals(intentPushNewRestaurant)) { 
	        				createNotification(context); 
	        			} else if (action.equals(intentPushUpdateVotes)) { 
	        				createNotificationUpdateVotes(context); 
	        			}
	         	    } else 
	         	    	Log.d(TAG, "..." + key + " => " + json.getString(key));
               }
       	    } catch (JSONException ex) {
       		Log.d(TAG, "JSON failed!");
       	    }
       }
   }
   
   private void createNotificationUpdateVotes(Context context) {
	   
	   Intent votingIntent = new Intent(context, VotingActivity.class);
	   votingIntent.putExtra("group_id", groupId);
	   
	   PendingIntent pVotingIntent = PendingIntent.getActivity(context, REQUEST_CODE, votingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(organizer + " just voted!")
				.setContentText("Voting in progress with " + StringUtils.join(firstNames, ", "))
				.setContentIntent(pVotingIntent)
				 .setTicker(organizer + " just voted!")
				 .setProgress(0, 0, true)
				 .setAutoCancel(true);
	
		NotificationManager mNotificationManager = 
	            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
    		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
       }
   
   
   // Create a local dashboard notification to tell user about the event
   private void createNotification(Context context) {
	   
	   Intent votingIntent = new Intent(context, VotingActivity.class);
	   votingIntent.putExtra("group_id", groupId);
	   
	   PendingIntent pVotingIntent = PendingIntent.getActivity(context, REQUEST_CODE, votingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(organizer + " added " + restName + " in " + groupName)
				.setContentText("Voting in progress with " + StringUtils.join(firstNames, ", "))
				.setContentIntent(pVotingIntent)
				 .setTicker(organizer + " just added " + restName + "!")
				 .setProgress(0, 0, true)
				 .setAutoCancel(true);
	
		NotificationManager mNotificationManager = 
	            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
   }
   
   // Handle push notification by invoking activity directly
   private void launchSomeActivity(Context context, String datavalue) {
       
	   /* We don't need to launch any fragment/activity besides VotingActivity
	    * 
	   
	   Intent pupInt = new Intent(context, ShowPopUp.class);
       pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
       pupInt.putExtra("customdata", datavalue);
       context.getApplicationContext().startActivity(pupInt);
       *
       */
   }
   
   // Handle push notification by sending a local broadcast 
   // to which the activity subscribes to
   private void triggerBroadcastToActivity(Context context) {
	
       //LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(intentAction));
		Intent votingIntent = new Intent(context, VotingActivity.class);
		votingIntent.putExtra("group_id", groupId);

		Intent cancelIntent = new Intent(context, VotingActivityReceiver.class);
		cancelIntent.putExtra("notificationId", 77);
		
		//LoggedInUser.getcurrentUser().getFirstName() returns incorrect user
		PendingIntent pVotingIntent = PendingIntent.getActivity(context, REQUEST_CODE, votingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pIntentCancel = PendingIntent.getActivity(context, 0, cancelIntent, 0);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Instadine with " + groupName + "!")
				.setContentText("Voting in progress with " + StringUtils.join(firstNames, ", "))
				.setContentIntent(pVotingIntent)
				 .addAction(R.drawable.ic_yes, "Yes", pVotingIntent)
				 .addAction(R.drawable.ic_no, "No", pIntentCancel)
				 .setTicker("Instadine request from " + organizer + "!")
				 .setProgress(0, 0, true)
				 .setAutoCancel(true);
	
		NotificationManager mNotificationManager = 
	            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
       
   }
}
