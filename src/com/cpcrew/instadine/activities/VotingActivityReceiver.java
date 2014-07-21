package com.cpcrew.instadine.activities;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.LoggedInUser;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class VotingActivityReceiver extends BroadcastReceiver {
   private static final String TAG = "VotingActivityReceiver";
   private static final int REQUEST_CODE = 11;
   public static final String intentAction = "SEND_PUSH";
   public static final int NOTIFICATION_ID = 45;
   private String organizer;
   

   @Override
   public void onReceive(Context context, Intent intent) {
       if (intent == null) {
           Log.d(TAG, "Receiver intent null");
       } else {
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
       if (action.equals(intentAction))
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
         	   if (key.equals("customdata")) {    
         	 	// Handle push notification by invoking activity directly
         		// launchSomeActivity(context, json.getString(key));
         		// OR trigger a broadcast to activity
         		triggerBroadcastToActivity(context, json.getString(key));
         		// OR create a local notification
         		// createNotification(context);
         	    } else if (key.equals("currentuser")) {
         	    	organizer = json.getString(key);
         	    }
                    Log.d(TAG, "..." + key + " => " + json.getString(key));
               }
       	    } catch (JSONException ex) {
       		Log.d(TAG, "JSON failed!");
       	    }
       }
   }
   
   
   // Create a local dashboard notification to tell user about the event
   private void createNotification(Context context) {
       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(
          		R.drawable.ic_launcher).setContentTitle("Local InstaDine Notification");
       NotificationManager mNotificationManager = (NotificationManager) context
          		.getSystemService(Context.NOTIFICATION_SERVICE);
       mNotificationManager.notify(45, mBuilder.build());
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
   private void triggerBroadcastToActivity(Context context, String datavalue) {
	
       //LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(intentAction));
		Intent votingIntent = new Intent(context, VotingActivity.class);
		votingIntent.putExtra("group_id", datavalue);
		//Intent cancelIntent = new Intent(context, ProxyActivity.class);

		
		//LoggedInUser.getcurrentUser().getFirstName() returns incorrect user
		PendingIntent pVotingIntent = PendingIntent.getActivity(context, REQUEST_CODE, votingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pIntentCancel = PendingIntent.getActivity(context, REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Instadine with " + organizer + "?")
				.setContentText("Please respond").setContentIntent(pVotingIntent)
				 .addAction(R.drawable.ic_yes, "Yes", pVotingIntent)
				 .addAction(R.drawable.ic_no, "No", pIntentCancel)
				 .setTicker("Instadine request from " + organizer + "!")
				 .setAutoCancel(true);
	
		NotificationManager mNotificationManager = 
	            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
       
   }
}
