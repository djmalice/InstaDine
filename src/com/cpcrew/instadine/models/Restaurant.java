package com.cpcrew.instadine.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Restaurant")
public class Restaurant extends ParseObject {
	
		public Restaurant() {
			super();
		}
		public void setId(int id ) {
			put("rid",id);
		}
		
		public int getId() {
			return getInt("rid");
		}
		
		public void setRestaurantName(String restaurantName) {
			put("rname" , restaurantName);
		}
		
		public String getRestaurantName() {
			return getString("rname");
		}
		
		public ParseFile getPhotoFile() {
	        return getParseFile("rimage");
	    }
	 
	    public void setPhotoFile(ParseFile file) {
	        put("rimage", file);
	    }
		
		public void setCuisine(String cuisine) {
			put("rcuisine", cuisine);
		}
		
		public String getCuisine() {
			return getString("rcuisine");
		}
		
		public void setRatings(int ratings) {
			put("rratings", ratings);
		}
		
		public int getRatings() {
			return getInt("rratings");
		}
		
		public void setPrice(int price) {
			put("rprice", price);
		}
		
		public int getPrice() {
			return getInt("rprice");
		}
}
