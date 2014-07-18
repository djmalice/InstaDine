package com.cpcrew.instadine.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Business implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1118842776475281135L;
	private String id;
	private String name;
	private String phone;
	private String imageUrl;
	private String city;
	private double rating;
	private double lat;
	private double longi;
	
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
	
	// Decodes business json into business model object
	public static Business fromAutoCompleteJson(JSONObject jsonObject) {
		Business b = new Business();
        // Deserialize json into object fields
		try {
			String completeDescription = jsonObject.getString("description");
			String[] splits = completeDescription.split(",");
			b.name = splits[0];
			StringBuffer description = new StringBuffer("");
			for(int i=1;i<splits.length;i++){
				description.append(splits[i]);
			}
			Log.d("debug", "Description is:" + description);
			b.city=description.toString();
			b.id = jsonObject.getString("place_id");
        	/*b.name = jsonObject.getString("name");
        	b.city = jsonObject.getString("vicinity");
        	JSONObject Geometry = jsonObject.getJSONObject("geometry").getJSONObject("location");
        	b.lat= Geometry.getDouble("latitude");
        	b.longi = Geometry.getDouble("longitude");*/
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
		// Return new object
		return b;
	}
	
	public static Business fromDetailJson(JSONObject jsonObject) {
		Business b = new Business();
        // Deserialize json into object fields
		try {
			b.name = jsonObject.getString("name");
		} catch (JSONException e) {
            e.printStackTrace();
            b.name = null;
        }
		try {
			b.city=jsonObject.getString("vicinity");
		} catch (JSONException e) {
            e.printStackTrace();
            b.city = null;
        }
		try {
			b.id = jsonObject.getString("place_id");
		} catch (JSONException e) {
            e.printStackTrace();
            b.id = null;
        }
		JSONObject Geometry;
		try {
			Geometry = jsonObject.getJSONObject("geometry").getJSONObject("location");
			b.lat= Geometry.getDouble("lat");
			b.longi = Geometry.getDouble("lng");
		} catch (JSONException e) {
            e.printStackTrace();
            Geometry = null;
            return b;
        }

			
			// b.rating = jsonObject.getDouble("rating");
        	
        	
		try {
        	b.phone =jsonObject.getString("formatted_phone_number");
        	b.imageUrl = jsonObject.getString("icon");
        	
        } catch (JSONException e) {
            e.printStackTrace();
            b.phone = null;
            b.imageUrl = null;
            return b;
        }
		// Return new object
		return b;
	}
	
	
	
	
	// Decodes array of business json results into business model objects
    public static ArrayList<Business> fromJson(JSONArray jsonArray) {
        ArrayList<Business> businesses = new ArrayList<Business>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject businessJson = null;
            try {
            	businessJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Business business = Business.fromAutoCompleteJson(businessJson);
            if (business != null) {
            	businesses.add(business);
            }
        }

        return businesses;
    }
    
    @Override
    public String toString() {
    	return "Id: " + id + " Name: " + name + " City: " + city + " Lat:" + lat + " Longi: " + longi + " Phone: " + phone;
    }
}
