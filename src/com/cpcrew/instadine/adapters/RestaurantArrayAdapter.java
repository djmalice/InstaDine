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
    	ImageView star1;
    	ImageView star2;
    	ImageView star3;
    	ImageView star4;
    	ImageView star5;
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
			viewHolder.star1 = (ImageView)convertView.findViewById(R.id.ivStar1);
			viewHolder.star2 = (ImageView)convertView.findViewById(R.id.ivStar2);
			viewHolder.star3 = (ImageView)convertView.findViewById(R.id.ivStar3);
			viewHolder.star4 = (ImageView)convertView.findViewById(R.id.ivStar4);
			viewHolder.star5 = (ImageView)convertView.findViewById(R.id.ivStar5);
			viewHolder.ivRest = (ImageView)convertView.findViewById(R.id.ivRest);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.tvRestname.setText(thisRest.getName());
		viewHolder.tvSelectionCount.setText(String.valueOf(thisRest.getCount()) );
		if (thisRest.getCity() != null )
			viewHolder.tvCity.setText(thisRest.getCity());
		placeStars(viewHolder, thisRest.getRating());
		String photoFile = thisRest.getImageUrl();
		ImageLoader imageLoader = ImageLoader.getInstance();
		if (photoFile != null && !(photoFile.equals(""))) {
			imageLoader.displayImage(photoFile, viewHolder.ivRest);
		} 
//		else {
//			viewHolder.ivRest.setVisibility(View.GONE);
//		}

		return convertView;
	}
	
	public void placeStars(ViewHolder viewHolder, double rating) {
		float roundedRating = Math.round(rating);
		if (roundedRating >= 1)
			viewHolder.star1.setImageResource(R.drawable.apptheme_rate_star_small_on_holo_light);
		if (roundedRating >= 2)
			viewHolder.star2.setImageResource(R.drawable.apptheme_rate_star_small_on_holo_light);
		if (roundedRating >= 3)
			viewHolder.star3.setImageResource(R.drawable.apptheme_rate_star_small_on_holo_light);
		if (roundedRating >= 4)
			viewHolder.star4.setImageResource(R.drawable.apptheme_rate_star_small_on_holo_light);
		if (roundedRating >= 5)
			viewHolder.star5.setImageResource(R.drawable.apptheme_rate_star_small_on_holo_light);		
	}
	

}
