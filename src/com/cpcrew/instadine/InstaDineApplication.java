package com.cpcrew.instadine;

import android.app.Application;

import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.Restaurant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class InstaDineApplication extends Application {

	public static final String TAG = "InstaDine";
	
	@Override
	public void onCreate() {
				
		super.onCreate();
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
        		cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(defaultOptions)
            .build();
        ImageLoader.getInstance().init(config);
        
		ParseObject.registerSubclass(Event.class);
		ParseObject.registerSubclass(Group.class);
		ParseObject.registerSubclass(Restaurant.class);
		//ParseObject.registerSubclass(User.class);
		
		//please initialize appropriate parse database!!!
		 
		// Demo app
		// Parse.initialize(this,"4HAN0kCTzjwij71b32MSXK3OXzqzDYg2iSxhMZ3u","SFKkmKBIsgz6bY7a5okGbQHZeg3vGNwaRmg8ZWCH");
		
		//RajiTest2 - Has dummy data for testing
		// Parse.initialize(this, "AoeM8jNFfUwhBBxjJlH5wN4XMAGyiE184vdpMs6d", "5OhGH8TRTu9IXvT4puLzWHCVetnWmYSmj7aXgS0N");
		
		/*// Test creation of object
      	ParseObject testObject = new ParseObject("TestObject");
      	testObject.put("Tintim", "Kitkat");
      	testObject.saveInBackground();*/

    	// cpcrewcorp account - MayanTest App
    	//Parse.initialize(this, "2KdnQ3PisBoJWPavuDKHBxXnQEHWMlyESyXYBfyV", "re2zJPO0piIISY7qL07dNClFC2Iz91u2Gcw4Abfg");
		
		// mayanPushTest app
		Parse.initialize(this, "pVwaIBnXu5LJdwL5pdXNE8XecjgfTaSXhBi1UDwQ", "NzJfvHzrOIlVKSB4EgbLJLho8eRXjZXYQFab69CP");

    	// Set your Facebook App Id in strings.xml
    	ParseFacebookUtils.initialize(getString(R.string.app_id)); 
		
    	// this is used for push notification - an installation object is created in database, and is used by push
    	ParseInstallation.getCurrentInstallation().saveInBackground();
		
	}
	
}
