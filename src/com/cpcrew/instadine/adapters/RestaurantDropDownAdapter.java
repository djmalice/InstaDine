package com.cpcrew.instadine.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cpcrew.instadine.R;

public class RestaurantDropDownAdapter extends ArrayAdapter<String> implements
		Filterable {
	private ArrayList<String> resultList;
	
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
 
		String venue = getItem(position);
		convertView.setTag(venue);
		 
			venueName.setText("name");
			venueAddress.setText("address");
			
 
		return convertView;
	}
	
	@Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    //resultList = autocomplete(constraint.toString());

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
}
	

