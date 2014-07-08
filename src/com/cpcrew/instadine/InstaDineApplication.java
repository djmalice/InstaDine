package com.cpcrew.instadine;

import android.app.Application;

import com.cpcrew.instadine.models.Event;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.Restaurant;
import com.cpcrew.instadine.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class InstaDineApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ParseObject.registerSubclass(Event.class);
		ParseObject.registerSubclass(Group.class);
		ParseObject.registerSubclass(Restaurant.class);
		ParseObject.registerSubclass(User.class);
		Parse.initialize(this, "4j8uZgh6wEGO5GrOGWpgPEut9tCIFAvjdLWxeyJ2", "kllySxso8im9bcTw8XmwcjEEAlubXzdVo4GKeYF1");
		
		/*// Test creation of object
      	ParseObject testObject = new ParseObject("TestObject");
      	testObject.put("Tintim", "Kitkat");
      	testObject.saveInBackground();*/
		
	}
}
