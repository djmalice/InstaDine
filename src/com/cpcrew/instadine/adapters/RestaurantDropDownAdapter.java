package com.cpcrew.instadine.adapters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.Business;
import com.cpcrew.instadine.models.Restaurant;

public class RestaurantDropDownAdapter extends ArrayAdapter<Business> implements
		Filterable {
	private static final String LOG_TAG = "DEBUG";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyBAZ3Lnc15HxezxeoffyTP7RoG1m-x6rpc";
	private ArrayList<Business> resultList;
	
	public RestaurantDropDownAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.actionbar_search_item, parent, false);
		}
 
		TextView venueName = (TextView) convertView
				.findViewById(R.id.search_item_venue_name);
		TextView venueAddress = (TextView) convertView
				.findViewById(R.id.search_item_venue_address);
 
		Business venue = getItem(position);
		convertView.setTag(venue);
		 
			venueName.setText(" " + venue.getName());
			venueAddress.setText(venue.getCity());
			
 
		return convertView;
	}
	
	@Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Business getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                Log.d("debug", "Perform Filtering called");
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
        
        return filter;
    }
    
    private ArrayList<Business> autocomplete(String input) {
        ArrayList<Business> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=establishment&location=37.76999,-122.44696&radius=500");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
        	// Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<Business>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
            	Business b = Business.fromAutoCompleteJson(predsJsonArray.getJSONObject(i));
                resultList.add(b);
            }
            Log.d("debug", resultList.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
    
    
    
}
	

