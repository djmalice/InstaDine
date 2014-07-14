package com.cpcrew.instadine;

import android.app.Application;

import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.Restaurant;
import com.cpcrew.instadine.models.User;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

public class InstaDineApplication extends Application {

	public static final String TAG = "InstaDine";
	
	@Override
	public void onCreate() {
				
		super.onCreate();
		ParseObject.registerSubclass(Event.class);
		ParseObject.registerSubclass(Group.class);
		ParseObject.registerSubclass(Restaurant.class);
		ParseObject.registerSubclass(User.class);
		
		//please initialize appropriate parse database!!!
		
		//Parse.initialize(this, "4j8uZgh6wEGO5GrOGWpgPEut9tCIFAvjdLWxeyJ2", "kllySxso8im9bcTw8XmwcjEEAlubXzdVo4GKeYF1");
		
		//RajiTest - Has dummy data for testing
		//Parse.initialize(this, "ddKpbQ6IZhdY3fDebFZnzuSlKx8blZ91ug4Z3L7w", "XLnBfQLNuDxhzqNyJymRU3M75hskYNGptsI2jpjX");
		
		/*// Test creation of object
      	ParseObject testObject = new ParseObject("TestObject");
      	testObject.put("Tintim", "Kitkat");
      	testObject.saveInBackground();*/

    	// cpcrewcorp account - MayanTest App
    	Parse.initialize(this, "2KdnQ3PisBoJWPavuDKHBxXnQEHWMlyESyXYBfyV",
    			"re2zJPO0piIISY7qL07dNClFC2Iz91u2Gcw4Abfg");

    	// Set your Facebook App Id in strings.xml
    	ParseFacebookUtils.initialize(getString(R.string.app_id)); 
		
	}
	
}
