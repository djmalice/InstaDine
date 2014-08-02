package com.cpcrew.instadine.activities;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.api.ParseEventsApi;
import com.cpcrew.instadine.api.ParseEventsApi.ParseEventApiListener;
import com.cpcrew.instadine.api.ParseGroupsApi;
import com.cpcrew.instadine.api.ParseGroupsApi.ParseGroupsApiListener;
import com.cpcrew.instadine.fragments.DeciderViewFragment;
import com.cpcrew.instadine.fragments.RestarauntListFragment;
import com.cpcrew.instadine.fragments.RestarauntListFragment.RefreshListener;
import com.cpcrew.instadine.models.Business;
import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.LoggedInUser;
import com.cpcrew.instadine.models.Rest;
import com.cpcrew.instadine.models.Restaurant;
import com.cpcrew.instadine.models.User;
import com.cpcrew.instadine.utils.Utils;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

public class VotingActivity extends FragmentActivity implements
		ParseEventApiListener, CalendarDatePickerDialog.OnDateSetListener,
		RadialTimePickerDialog.OnTimeSetListener, ParseGroupsApiListener, RefreshListener {

	private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
	private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
	private static String TAG = VotingActivity.class.getSimpleName();

	Business userChoice;
	private HashMap<String, Rest> Restaurants;

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(getApplicationContext(),
					"onReceive VotingActivity invoked!", Toast.LENGTH_LONG)
					.show();
		}
	};

	RelativeLayout eventTimeView;
	RelativeLayout eventDateView;
	RelativeLayout expiryTimeView;
	RelativeLayout expiryDateView;
	TextView tvEventTime;
	TextView tvEventDate;
	TextView tvExpiryTime;
	TextView tvExpiryDate;

	EditText etLocation;
	private String timeOfEvent;
	private String dateOfEvent;
	private String timeOfExpiry;
	private String dateOfExpiry;

	private String selectedRestaurant = null;

	private boolean eventSelected = false;
	private boolean expirySelected = false;
	private boolean isEnabledSlidingPanel = false;

	private ParseEventsApi parseEventApi;
	private ParseGroupsApi parseGroupsApi;
	private Event currentEvent;
	private Group currentGroup;
	private String groupId = null;
	private ArrayList<User> usersOfGroup;
	private HashMap<String, String> groupUsersFacebookIds;
	
	private RestarauntListFragment restFragment;
	
	// SlidingUp Panel
	private SlidingUpPanelLayout mLayout;
	private boolean panelCollapsed = true;

	public static final int NOTIFICATION_ID = 45;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		NotificationManager mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(NOTIFICATION_ID);

		if (groupId == null)
			groupId = getIntent().getStringExtra("group_id");
		setContentView(R.layout.activity_voting);

		parseEventApi = new ParseEventsApi(this);
		parseGroupsApi = new ParseGroupsApi(this);

		restFragment = new RestarauntListFragment();
		// Fetch the Event
		findEvent(groupId);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flRestContainer, restFragment);
		ft.commit();

		
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_panel);
        mLayout.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                //setActionBarTranslation(mLayout.getCurrentParalaxOffset());
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");

            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");

            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });

		// Specify an Activity to handle all pushes by default
		PushService.setDefaultPushCallback(this, VotingActivity.class);
	}

	// pushNotifications onPause routine
	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mBroadcastReceiver);
	}

	// had created this picker before we decided to use a meal button
	// (breakfast, lunch, dinner)
	// leaving code here for now...
	public void createTimePicker(View v) {

		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getTag().equals("event")) {
					eventSelected = true;
					expirySelected = false;
				} else if (v.getTag().equals("expiry")) {
					expirySelected = true;
					eventSelected = false;
				}
				FragmentManager fm = getSupportFragmentManager();
				DateTime now = DateTime.now();
				RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
						.newInstance(VotingActivity.this, now.getHourOfDay(),
								now.getMinuteOfHour(),
								DateFormat.is24HourFormat(VotingActivity.this));
				timePickerDialog.setThemeDark(true);
				timePickerDialog.show(fm, FRAG_TAG_TIME_PICKER);
			}
		});
	}

	@Override
	public void onTimeSet(RadialTimePickerDialog dialog, int hourOfDay,
			int minute) {
		if (hourOfDay == 0) {
			hourOfDay = 12;
		}
		String eventTime = null;
		if (hourOfDay > 12) {
			hourOfDay = hourOfDay - 12;

			// There should be a space before PM/AM , is a problem for parsing
			// date.
			eventTime = (minute < 10) ? hourOfDay + ":0" + minute + "PM"
					: hourOfDay + ":" + minute + "PM";
		} else {
			eventTime = (minute < 10) ? hourOfDay + ":0" + minute + "AM"
					: hourOfDay + ":" + minute + "AM";
		}
		// Based on expiryTime selected or EventTime update the data accordingly
		if (eventSelected) {
			this.timeOfEvent = eventTime;
			tvEventTime.setText(eventTime);
		} else if (expirySelected) {
			timeOfExpiry = eventTime;
			tvExpiryTime.setText(eventTime);
		}
		eventSelected = false;
		expirySelected = false;

	}

	public void createDatePicker(View v) {

		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getTag().equals("event")) {
					eventSelected = true;
					expirySelected = false;
				} else if (v.getTag().equals("expiry")) {
					expirySelected = true;
					eventSelected = false;
				}

				FragmentManager fm = getSupportFragmentManager();
				DateTime now = DateTime.now();
				CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
						.newInstance(VotingActivity.this, now.getYear(),
								now.getMonthOfYear() - 1, now.getDayOfMonth());
				calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
			}
		});
	}

	@Override
	public void onDateSet(CalendarDatePickerDialog dialog, int year,
			int monthOfYear, int dayOfMonth) {
		++monthOfYear;
		if (eventSelected) {
			dateOfEvent = monthOfYear + "/" + dayOfMonth + "/" + year;
			tvEventDate.setText(Utils.toDisplayDate(dateOfEvent));
		} else if (expirySelected) {
			dateOfExpiry = monthOfYear + "/" + dayOfMonth + "/" + year;
			tvExpiryDate.setText(Utils.toDisplayDate(dateOfExpiry));
		}
		eventSelected = false;
		expirySelected = false;
	}

	@Override
	public void onResume() {
		// Example of reattaching to the fragment
		super.onResume();

		// pushNotifications onResume routine, calls the custom defined
		// VotingActivityReceiver
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mBroadcastReceiver,
				new IntentFilter(VotingActivityReceiver.intentAction));

		CalendarDatePickerDialog calendarDatePickerDialog = (CalendarDatePickerDialog) getSupportFragmentManager()
				.findFragmentByTag(FRAG_TAG_DATE_PICKER);
		if (calendarDatePickerDialog != null) {
			calendarDatePickerDialog.setOnDateSetListener(this);
		}

		RadialTimePickerDialog rtpd = (RadialTimePickerDialog) getSupportFragmentManager()
				.findFragmentByTag(FRAG_TAG_TIME_PICKER);
		if (rtpd != null) {
			rtpd.setOnTimeSetListener(this);
		}
	}

	public void getViews() {
		etLocation = (EditText) findViewById(R.id.etLocation);

		eventTimeView = (RelativeLayout) findViewById(R.id.rlEventTime);
		eventDateView = (RelativeLayout) findViewById(R.id.rlEventDate);
		expiryTimeView = (RelativeLayout) findViewById(R.id.rlExpiryTime);
		expiryDateView = (RelativeLayout) findViewById(R.id.rlExpiryDate);
	}

	public void loadEvent() {

		// Read from the currentEvent
		etLocation.setText(currentEvent.getLocation());
		tvEventDate.setText(Utils.toDisplayDate(currentEvent.getDate()));
		tvEventTime.setText(currentEvent.getTime());

		// load the restaurant id and count
		List<String> prevSelections = currentEvent.getSelection();
		if (prevSelections != null) {
			for (int i = 0; i < prevSelections.size(); ++i) {

				String restId = Event.getSelectionRest(prevSelections.get(i));
				String userId = Event.getSelectionUser(prevSelections.get(i));
				restFragment.addRestaurant(restId, userId);
				// TODO Not sure if I need to have the previous Selection
				// information
				// since we do not allow more than one selection.
				// Add my previous selections
				if (userId.equals(LoggedInUser.getcurrentUser().getId())) { // comes
																			// after
																			// addRestaurant(..)
					restFragment.addMySelection(restId);
					restFragment.addMyPrevSelection(restId);
				}
			}
			// restaurants is populated with Business info.
			restFragment.populateBusinessInfo();

		}

		// Show in the listView

	}

	public void updateDeciderView() {
		// Decider Message
		String deciderMessage = null;
		String expiryTime = "on " + currentEvent.getExpiryDate() + " "
				+ currentEvent.getExpiryTime();
		if (currentEvent.getDate() != null
				&& currentEvent.getExpiryDate() != null) {
			if (Utils.isTimeGreaterThanNow(currentEvent.getExpiryDate() + " "
					+ currentEvent.getExpiryTime())) {
				// Has Expired
				deciderMessage = "Voting is complete. "
						+ currentGroup.getGroupName() + " is going to "
						+ restFragment.highestVotedRestaraunt().getName() + " on "
						+ currentEvent.getDate() + " " + currentEvent.getTime();
			} else {
				// Still voting going on.
				deciderMessage = currentGroup.getGroupName()
						+ " is still voting. See results when voting expires "
						+ expiryTime;
			}
		} else {
			// No event date and expiry date
			deciderMessage = currentGroup.getGroupName()
					+ " is deciding a Restaraunt ";
		}
		// Event Message to the group
		TextView tvEventSummary = (TextView) findViewById(R.id.tvEventSummary);
		tvEventSummary.setText(deciderMessage);
	}

	public void onDone(View v) {
		// Toast.makeText(this,"Sending out invitations to " +
		// currentGroup.getGroupName() , Toast.LENGTH_SHORT).show();
		
		final Button btnDone = (Button) findViewById(R.id.btnDone);
		if (panelCollapsed) {
		if (currentEvent == null) {
			// updateDeciderView(); //Data is still not in the database. Cannot
			// be done.
			parseEventApi.createEvent(currentGroup, dateOfEvent, timeOfEvent,
					dateOfExpiry, timeOfExpiry,
					etLocation.getText().toString(), LoggedInUser
							.getcurrentUser().getId(), restFragment
							.newSelections());
		} else {
			parseEventApi.updateEvent(currentEvent, LoggedInUser
					.getcurrentUser().getId(), restFragment.newSelections());
		}
		// Get the latest values from the ParseInstallation object.
		ParseInstallation.getCurrentInstallation().refreshInBackground(
				new RefreshCallback() {

					@Override
					public void done(ParseObject object, ParseException e) {
						if (e == null) {
							pushToVotingActivity();
						}
						if (  isEnabledSlidingPanel) {
							// if (isPanelCollapsed) {
							btnDone.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_action_drop_list, 0, 0, 0);
							btnDone.setText(R.string.undoneAction);
//							Rest rest = restFragment.highestVotedRestaraunt();
//							Bundle args = new Bundle();
//							args.putSerializable("votedRest", rest);
//							args.putSerializable("groupmap", rest.getGroupUserFacebookIds());
							DeciderViewFragment deciderFragment = new DeciderViewFragment();
//							deciderFragment.setArguments(args);
							FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
							ft.replace(R.id.flDeciderContainer, deciderFragment);
							ft.commit();
							mLayout.expandPanel();
							panelCollapsed = false;
						} else {
							finish();
						}
					}
				});
		} else {
			if (isEnabledSlidingPanel) {
				// Return to main activity
				// Refresh main activity
				refreshEvent();
				btnDone.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.ic_yes, 0, 0, 0);
				btnDone.setText(R.string.doneAction);
				mLayout.collapsePanel();
				panelCollapsed = true;
			}
		}
	}
	
	public Rest getHighestVotedRestaraunt() {
		return restFragment.highestVotedRestaraunt();
	}
	public void pushToVotingActivity() {

		JSONObject obj;
		try {
			obj = new JSONObject();
			// obj.put("alert", "All users of " + currentGroup.getGroupName() +
			// " are recieving this notification!");
			// obj.put("alert", "New Instadine request from " +
			// LoggedInUser.getcurrentUser().getFirstName() +"!");
			// obj.put("alert","");
			// obj.put("title", "New event invite!");
			if ((restFragment.getCount() > 1) && (selectedRestaurant != null)) {
				obj.put("action",
						VotingActivityReceiver.intentPushNewRestaurant);
				obj.put("restname", selectedRestaurant);
				// Toast.makeText(this, selectedRestaurant,
				// Toast.LENGTH_SHORT).show();
				selectedRestaurant = null;
			} else if ((restFragment.getCount() > 1)
					&& (selectedRestaurant == null)) {
				obj.put("action", VotingActivityReceiver.intentPushUpdateVotes);
				// Toast.makeText(this, selectedRestaurant,
				// Toast.LENGTH_SHORT).show();
			} else if (restFragment.getCount() == 1) {
				obj.put("action", VotingActivityReceiver.intentAction);
				obj.put("restname", selectedRestaurant);
			}

			obj.put("currentuser", LoggedInUser.getcurrentUser().getFirstName());
			obj.put("groupid", groupId);

			/*
			 * Every Parse application installed on a device registered for push
			 * notifications has an associated Installation object. The
			 * Installation object is where you store all the data needed to
			 * target push notifications.
			 * 
			 * Installation objects are available through the ParseInstallation
			 * class, a subclass of ParseObject. It uses the same API for
			 * storing and retrieving data.
			 */

			/*
			 * You can even create relationships between your Installation
			 * objects and other classes saved on Parse. To associate an
			 * Installation with a particular user, for example, you can simply
			 * store the current user on the ParseInstallation.
			 */

			// ParseInstallation.getCurrentInstallation().put("groupname",
			// currentGroup.getGroupName());
			// ParseInstallation.getCurrentInstallation().put("currentuser",
			// LoggedInUser.getcurrentUser().getFirstName());
			ArrayList<String> firstNames = new ArrayList<String>();
			for (User u : usersOfGroup) {
				firstNames.add(u.getFirstName());
			}
			
			/*JSONArray firstNamesJSON = new JSONArray();
			for(User u:usersOfGroup){
				firstNamesJSON.put(u.getFirstName());
			}*/
			
			ParseInstallation.getCurrentInstallation().put("usersofgroup", firstNames);
			ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {
						//Toast toast = Toast.makeText(getApplicationContext(), "ParseInstallation Saved!", Toast.LENGTH_SHORT);
						//toast.show();
					} else {
						e.printStackTrace();

						//Toast toast = Toast.makeText(getApplicationContext(), "ParseInstallation Save Failed :(" , Toast.LENGTH_SHORT);
						//toast.show();
					}
				}
			});
			
			
			
			/*
			 * Once you have your data stored on your Installation objects, 
			 * you can use a ParseQuery to target a subset of these devices. 
			 * 
			 * Installation queries work just like any other Parse query, but we
			 * use the special static method ParseInstallation.getQuery() to
			 * create it.
			 */

			ParsePush push = new ParsePush();
			ParseQuery query = ParseInstallation.getQuery();

			// Send push notification to query - in this case, only the current
			// group's users will be notified
			// query.whereEqualTo("currentuser",usersOfGroup. );

			firstNames.remove(LoggedInUser.getcurrentUser().getFirstName());

			query.whereContainedIn("currentuser", firstNames);

			obj.put("groupname", currentGroup.getGroupName());

			JSONArray firstNamesJSON = null;
			try {
				firstNamesJSON = new JSONArray();
				for (int x = 0; x < firstNames.size(); x++) {
					firstNamesJSON.put(x, firstNames.get(x));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			obj.put("otherusers", firstNamesJSON);

			push.setQuery(query);
			push.setData(obj);
			push.sendInBackground();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void onNo(View v) {
		parseEventApi.updateEvent(currentEvent, LoggedInUser.getcurrentUser());
	}

	public void findEvent(String groupId) {
		// First get the group
		// TODO combine group finding and event in one query
		parseEventApi.getGroupById(groupId);
	}

	@Override
	public void onGetGroupByIdResult(Group group) {
		// Now get the event associated with the group

		if (group != null) {
			currentGroup = group;
			parseGroupsApi.getUsersOfGroup(currentGroup);
			// change the title of the View
			setTitle(currentGroup.getGroupName());
			getActionBar().setSubtitle(currentGroup.getDesc());
		}
	}

	@Override
	public void onGetEventsForGroupResult(List<Event> events) {

		if (events != null && events.size() > 0) {
			currentEvent = events.get(0);
			existEventView();
			loadEvent(); // Set Existing View in loadEvent
		} else {
			// New Event View
			newEventView();

		}
	}

	public void newEventView() {
		getViews();
		LinearLayout ll = (LinearLayout) findViewById(R.id.llexpiryDT);
		ll.setVisibility(View.VISIBLE);

		// Event Time

		tvEventTime = (TextView) eventTimeView.findViewById(R.id.tvContent);
		tvEventTime.setText("");
		tvEventTime.setTag("event");
		TextView tvEventTimeLabel = (TextView) eventTimeView
				.findViewById(R.id.tvContentLabel);
		tvEventTimeLabel.setText("Set Time");
		tvEventTimeLabel.setTag("event");
		createTimePicker(tvEventTimeLabel);

		// Event Date

		tvEventDate = (TextView) eventDateView.findViewById(R.id.tvContent);
		tvEventDate.setText("");
		tvEventDate.setTag("event");
		TextView tvEventDateLabel = (TextView) eventDateView
				.findViewById(R.id.tvContentLabel);
		tvEventDateLabel.setText("Set Date");
		tvEventDateLabel.setTag("event");
		createDatePicker(tvEventDateLabel);

		// Expiry Time
		tvExpiryTime = (TextView) expiryTimeView.findViewById(R.id.tvContent);
		tvExpiryTime.setText("");
		tvExpiryTime.setTag("expiry");
		TextView tvExpiryTimeLabel = (TextView) expiryTimeView
				.findViewById(R.id.tvContentLabel);
		tvExpiryTimeLabel.setText("Expiry Time");
		tvExpiryTimeLabel.setTag("expiry");
		createTimePicker(tvExpiryTimeLabel);

		// Expiry Date
		tvExpiryDate = (TextView) expiryDateView.findViewById(R.id.tvContent);
		tvExpiryDate.setText("");
		tvExpiryDate.setTag("expiry");
		TextView tvExpiryDateLabel = (TextView) expiryDateView
				.findViewById(R.id.tvContentLabel);
		tvExpiryDateLabel.setText("Expiry Date");
		tvExpiryDateLabel.setTag("expiry");
		createDatePicker(tvExpiryDateLabel);
	}

	public void existEventView() {
		getViews();

		eventTimeView.setEnabled(false);
		eventDateView.setEnabled(false);

		// Event Time
		tvEventTime = (TextView) eventTimeView.findViewById(R.id.tvContent);
		tvEventTime.setText("");
		tvEventTime.setTag("event");
		TextView tvEventTimeLabel = (TextView) eventTimeView
				.findViewById(R.id.tvContentLabel);
		tvEventTimeLabel.setText("Time");
		tvEventTimeLabel.setTag("event");

		// Event Date
		tvEventDate = (TextView) eventDateView.findViewById(R.id.tvContent);
		tvEventDate.setText("");
		tvEventDate.setTag("event");
		TextView tvEventDateLabel = (TextView) eventDateView
				.findViewById(R.id.tvContentLabel);
		tvEventDateLabel.setText("Date");
		tvEventDateLabel.setTag("event");

		etLocation.setFocusable(false);
		etLocation.setEnabled(false);
		// etLocation.setBackgroundResource("#00000000");
	}
	
	public void refreshEvent() {
		if ( groupId != null ) {
			findEvent(groupId);
		}
	}

	@Override
	public void onGetRestaurantsForEventResult(List<Restaurant> restaurants) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetEventByIdResult(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onallUsersResults(List<User> users) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetUserOfGroupResults(List<User> users) {
		// TODO Auto-generated method stub
		if (usersOfGroup == null) {
			usersOfGroup = new ArrayList(users);
		} else {
			usersOfGroup.clear();
			usersOfGroup.addAll(users);
		}
		if (groupUsersFacebookIds != null) groupUsersFacebookIds.clear();
		else groupUsersFacebookIds = new HashMap<String, String>();
		for (User user : usersOfGroup) {
			groupUsersFacebookIds.put(user.getId(), user.getFacebookId());
		}
		restFragment.setFacebookIds(groupUsersFacebookIds);
		Log.d("debug", "usersOfGroup size: " + usersOfGroup.size());
		
		// Get the Events
		parseEventApi.getEventsForGroup(currentGroup);

	}

	@Override
	public void onGetGroupsForUserResults(List<Group> groups) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetGroupResult(List<Group> group) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetFriendsResult(List<User> friends) {
		// TODO Auto-generated method stub

	}

	@Override
	public void retrieveUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParentRefresh() {
		Log.d(TAG, "Refreshing the Event");
		refreshEvent();
		
	}
}
