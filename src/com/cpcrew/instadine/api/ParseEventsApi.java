package com.cpcrew.instadine.api;

import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.parse.FindCallback;
import com.parse.ParseException;

public class ParseEventsApi {
	
	/** Declaring the interface, to invoke a callback function in the implementing activity class */
	ParseEventApiListener mParseApiListner;
	public ParseEventsApi(Activity activity) {
		mParseApiListner = (ParseEventApiListener) activity;
	}


    /** An interface to be implemented in the hosting activity for "OK" button click listener */
    public interface ParseEventApiListener {
       
    }
    
	public static void getEventsForGroup(Group group) {
		
		group.getEventRelation().getQuery().findInBackground(new FindCallback<Event>() {
		    public void done(List<Event> events, ParseException e) {
		    	if ( e == null )
		    		Log.d("debug","Number of events for this group: " + events.size());
		    	else
		    		Log.d("debug",e.getMessage());
		    }

		});

	}
	
	

}
