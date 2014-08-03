/**
 * 
 */
package com.cpcrew.instadine.models;

import java.util.HashMap;
import java.util.HashSet;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
/**
 * @author raji
 *
 */
public class Rest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7204903652287679308L;
	//private Business restaurant;
	
	private int count = 0;
	private HashSet<String> userids; // objectIds of users that selected this restaurant, no duplicates
	private String id;
	private String name;
	private String phone;
	private String imageUrl;
	private String city;
	private double rating;
	private double lat;
	private double longi;
	
	
	
	
	
	
	
	
	
	
	public String getRestId() {
		return id;
	}
	public void setRestId(String id) {
		this.id = id;
	}
	public int getCount() {
		if ( userids == null ) return 0;
		else return userids.size();
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public void addUser(String userid) {
		if(userids == null)
			userids = new HashSet<String>();
		userids.add(userid);
	}
	
	public void removeUser(String userid) {
		userids.remove(userid);
	}
	
	public HashSet<String> getUsers() {
		return userids;
	}
	
//	public Business getRestaurant() {
//		return null;
//	}
//	public void setRestaurant(Business restaurant) {
//		//this.restaurant = restaurant;
//	}
	
	
	public String getId() {
		return id;
	}

	public String getCity() {
		return city;
	}

	public double getLat() {
		return lat;
	}

	public double getLongi() {
		return longi;
	}

	public double getRating() {
		return rating;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public String getImageUrl() {
		return this.imageUrl;
	}
	
	// Decodes Restaurant json into business model object
	public static Rest fromAutoCompleteJson(JSONObject jsonObject) {
		Rest r = new Rest();
		// Deserialize json into object fields
		try {
			String completeDescription = jsonObject.getString("description");
			String[] splits = completeDescription.split(",");
			r.name = splits[0];
			StringBuffer description = new StringBuffer("");
			for (int i = 1; i < splits.length; i++) {
				description.append(splits[i]);
			}
			Log.d("debug", "Description is:" + description);
			r.city = description.toString();
			r.id = jsonObject.getString("place_id");
			/*
			 * b.name = jsonObject.getString("name"); b.city =
			 * jsonObject.getString("vicinity"); JSONObject Geometry =
			 * jsonObject.getJSONObject("geometry").getJSONObject("location");
			 * b.lat= Geometry.getDouble("latitude"); b.longi =
			 * Geometry.getDouble("longitude");
			 */
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// Return new object
		return r;
	}
	
	public static Rest fromDetailJson(JSONObject jsonObject) {
		Rest r = new Rest();
        // Deserialize json into object fields
		try {
			r.name = jsonObject.getString("name");
		} catch (JSONException e) {
            e.printStackTrace();
            r.name = null;
        }
		try {
			r.city=jsonObject.getString("vicinity");
		} catch (JSONException e) {
            e.printStackTrace();
            r.city = null;
        }
		try {
			r.id = jsonObject.getString("place_id");
		} catch (JSONException e) {
            e.printStackTrace();
            r.id = null;
        }
		JSONObject Geometry;
		try {
			Geometry = jsonObject.getJSONObject("geometry").getJSONObject("location");
			r.lat= Geometry.getDouble("lat");
			r.longi = Geometry.getDouble("lng");
		} catch (JSONException e) {
            e.printStackTrace();
            Geometry = null;
            return r;
        }

		try {	
			r.rating = jsonObject.getDouble("rating");
		} catch(JSONException e){
			e.printStackTrace();
			r.rating = 0;
		}
		
        	
		try {
        	r.phone =jsonObject.getString("formatted_phone_number");
        	
        	
        } catch (JSONException e) {
            e.printStackTrace();
            r.phone = null;
            r.imageUrl = null;
            return r;
        }
		
		
		try {
			JSONArray photos = jsonObject.getJSONArray("photos");
			String photo_reference = photos.getJSONObject(0).getString("photo_reference");
			r.imageUrl = com.cpcrew.instadine.utils.Constants.GOOGLE_PHOTO_SEARCH + "&photoreference=" + 
							photo_reference + "&key=" + 
							com.cpcrew.instadine.utils.Constants.GOOGLE_PLACES_API_KEY;
		} catch (JSONException e){
			r.imageUrl = null;
		}
		
		// Return new object
		return r;
	}
	
	// Decodes array of business json results into business model objects
    public static ArrayList<Rest> fromJson(JSONArray jsonArray) {
        ArrayList<Rest> rests = new ArrayList<Rest>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject restJson = null;
            try {
            	restJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Rest rest = Rest.fromAutoCompleteJson(restJson);
            if (rest != null) {
            	rests.add(rest);
            }
        }

        return rests;
    }
	
    public void inflateRestaurantDetails( Rest r) {
		name = r.getName();
		city = r.getCity();
		rating = r.getRating();
		imageUrl = r.getImageUrl();
		id = r.getId();
		phone = r.getPhone();

	}
	
	
	
	// To show the user images
	private HashMap<String, String> groupUserFacebookIds;
	
	public HashMap<String, String> getGroupUserFacebookIds() {
		return groupUserFacebookIds;
	}
	
	public void addGroupUser(String userid, String facebookId) {
		if(groupUserFacebookIds == null)
			groupUserFacebookIds = new HashMap<String, String>();
		groupUserFacebookIds.put(userid, facebookId);
	}
	
	@Override
    public String toString() {
    	return "Id: " + id + " Name: " + name + " City: " + city + " Phone: " + phone + "Userids:" + groupUserFacebookIds;
    }
	
}
