package com.cpcrew.instadine;

import android.app.Application;

import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.Restaurant;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class InstaDineApplication extends Application {

	public static final String TAG = "InstaDine";
	
	@Override
	public void onCreate() {
				
		super.onCreate();
		ParseObject.registerSubclass(Event.class);
		ParseObject.registerSubclass(Group.class);
		ParseObject.registerSubclass(Restaurant.class);
		//ParseObject.registerSubclass(User.class);
		
		//please initialize appropriate parse database!!!
		
		//Parse.initialize(this, "4j8uZgh6wEGO5GrOGWpgPEut9tCIFAvjdLWxeyJ2", "kllySxso8im9bcTw8XmwcjEEAlubXzdVo4GKeYF1");
		
		//RajiTest2 - Has dummy data for testing
		Parse.initialize(this, "AoeM8jNFfUwhBBxjJlH5wN4XMAGyiE184vdpMs6d", "5OhGH8TRTu9IXvT4puLzWHCVetnWmYSmj7aXgS0N");
		
		/*// Test creation of object
      	ParseObject testObject = new ParseObject("TestObject");
      	testObject.put("Tintim", "Kitkat");
      	testObject.saveInBackground();*/

    	// cpcrewcorp account - MayanTest App
    	//Parse.initialize(this, "2KdnQ3PisBoJWPavuDKHBxXnQEHWMlyESyXYBfyV", "re2zJPO0piIISY7qL07dNClFC2Iz91u2Gcw4Abfg");

    	// Set your Facebook App Id in strings.xml
    	ParseFacebookUtils.initialize(getString(R.string.app_id)); 
		
    	// this is used for push notification - an installation object is created in database, and is used by push
    	ParseInstallation.getCurrentInstallation().saveInBackground();
		
	}
	
}
