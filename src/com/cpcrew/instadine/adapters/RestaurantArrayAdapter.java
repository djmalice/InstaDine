package com.cpcrew.instadine.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.Rest;
import com.cpcrew.instadine.models.Restaurant;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;


public class RestaurantArrayAdapter extends ArrayAdapter<Rest>{

	public static String TAG = RestaurantArrayAdapter.class.getSimpleName();
	
	public RestaurantArrayAdapter(Context context, ArrayList<Rest> restaurants) {
		super(context, 0, restaurants);
	}

	
    private static class ViewHolder {
    	TextView tvRestname;
    	TextView tvSelectionCount;

    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Rest thisRest = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflator = LayoutInflater.from(getContext());
			convertView = inflator.inflate(R.layout.restaurant_item, parent, false);
			viewHolder.tvRestname = (TextView)convertView.findViewById(R.id.tvRestname);
			viewHolder.tvSelectionCount = (TextView)convertView.findViewById(R.id.tvSelectionCount);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.tvRestname.setText(thisRest.getRestName());
		viewHolder.tvSelectionCount.setText(String.valueOf(thisRest.getCount()) );
		return convertView;
	}
	

}
