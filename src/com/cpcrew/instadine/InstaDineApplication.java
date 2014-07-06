package com.cpcrew.instadine;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class InstaDineApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Parse.initialize(this, "4j8uZgh6wEGO5GrOGWpgPEut9tCIFAvjdLWxeyJ2", "kllySxso8im9bcTw8XmwcjEEAlubXzdVo4GKeYF1");
		
		/*// Test creation of object
      	ParseObject testObject = new ParseObject("TestObject");
      	testObject.put("Tintim", "Kitkat");
      	testObject.saveInBackground();*/
		
	}
}
