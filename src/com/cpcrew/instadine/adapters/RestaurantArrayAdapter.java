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
import com.cpcrew.instadine.models.Rest;
import com.nostra13.universalimageloader.core.ImageLoader;


public class RestaurantArrayAdapter extends ArrayAdapter<Rest>{

	public static String TAG = RestaurantArrayAdapter.class.getSimpleName();
	
	public RestaurantArrayAdapter(Context context, ArrayList<Rest> restaurants) {
		super(context, 0, restaurants);
	}

	
    private static class ViewHolder {
    	TextView tvRestname;
    	TextView tvSelectionCount;
    	TextView tvCity;
    	TextView tvRating;
    	ImageView ivRest;

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
			viewHolder.tvCity = (TextView)convertView.findViewById(R.id.tvCity);
			viewHolder.tvRating = (TextView)convertView.findViewById(R.id.tvRating);
			viewHolder.ivRest = (ImageView)convertView.findViewById(R.id.ivRest);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.tvRestname.setText(thisRest.getName());
		viewHolder.tvSelectionCount.setText(String.valueOf(thisRest.getCount()) );
		
		String photoFile = thisRest.getImageUrl();
		ImageLoader imageLoader = ImageLoader.getInstance();
		if (photoFile != null && !(photoFile.equals(""))) {
			imageLoader.displayImage(photoFile, viewHolder.ivRest);
		} else {
			viewHolder.ivRest.setVisibility(View.GONE);
		}

		return convertView;
	}
	

}
