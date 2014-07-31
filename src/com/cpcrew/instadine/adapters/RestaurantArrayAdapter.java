package com.cpcrew.instadine.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.Rest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RestaurantArrayAdapter extends ArrayAdapter<Rest> {

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
		TextView tvSelectionCountLabel;
		GridView gvUsers;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Rest thisRest = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflator = LayoutInflater.from(getContext());
			convertView = inflator.inflate(R.layout.restaurant_item, parent,
					false);
			
			// user Images
			viewHolder.gvUsers = (GridView)convertView.findViewById(R.id.gvUsers);
			assert(viewHolder.gvUsers != null);
			
			
			
			viewHolder.tvRestname = (TextView) convertView
					.findViewById(R.id.tvRestname);
			viewHolder.tvSelectionCount = (TextView) convertView
					.findViewById(R.id.tvSelectionCount);
			viewHolder.tvSelectionCountLabel = (TextView) convertView
					.findViewById(R.id.tvSelectionCountLabel);
			viewHolder.tvCity = (TextView) convertView
					.findViewById(R.id.tvCity);
			viewHolder.star1 = (ImageView) convertView
					.findViewById(R.id.ivStar1);
			viewHolder.star2 = (ImageView) convertView
					.findViewById(R.id.ivStar2);
			viewHolder.star3 = (ImageView) convertView
					.findViewById(R.id.ivStar3);
			viewHolder.star4 = (ImageView) convertView
					.findViewById(R.id.ivStar4);
			viewHolder.star5 = (ImageView) convertView
					.findViewById(R.id.ivStar5);
			viewHolder.ivRest = (ImageView) convertView
					.findViewById(R.id.ivRest);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		HashMap<String, String> fbidmap = thisRest.getGroupUserFacebookIds();
		
		viewHolder.gvUsers.setAdapter(new ImageAdapter(getContext(), new ArrayList(fbidmap.values())));
		viewHolder.tvRestname.setText(thisRest.getName());
		viewHolder.tvSelectionCount
				.setText(String.valueOf(thisRest.getCount()));
		if (thisRest.getCity() != null)
			viewHolder.tvCity.setText(thisRest.getCity());
		placeStars(viewHolder, thisRest.getRating());
		String photoFile = thisRest.getImageUrl();
		ImageLoader imageLoader = ImageLoader.getInstance();
		if (photoFile != null && !(photoFile.equals(""))) {
			imageLoader.displayImage(photoFile, viewHolder.ivRest);
		}
		// else {
		// viewHolder.ivRest.setVisibility(View.GONE);
		// }
		
		if (this.getCount() > 1) viewHolder.tvSelectionCountLabel.setText("votes");
		else viewHolder.tvSelectionCountLabel.setText("vote");
		return convertView;
	}

	public void placeStars(ViewHolder viewHolder, double rating) {
		float roundedRating = Math.round(rating);
		if (roundedRating >= 1)
			viewHolder.star1
					.setImageResource(R.drawable.apptheme_rate_star_small_on_holo_light);
		if (roundedRating >= 2)
			viewHolder.star2
					.setImageResource(R.drawable.apptheme_rate_star_small_on_holo_light);
		if (roundedRating >= 3)
			viewHolder.star3
					.setImageResource(R.drawable.apptheme_rate_star_small_on_holo_light);
		if (roundedRating >= 4)
			viewHolder.star4
					.setImageResource(R.drawable.apptheme_rate_star_small_on_holo_light);
		if (roundedRating >= 5)
			viewHolder.star5
					.setImageResource(R.drawable.apptheme_rate_star_small_on_holo_light);
	}

	private Interpolator accelerator = new AccelerateInterpolator();
    private Interpolator decelerator = new DecelerateInterpolator();
    
	public void flipit(View v) {
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(v,
				"rotationY", 0f, 90f);
		visToInvis.setDuration(500);
		visToInvis.setInterpolator(accelerator);
		final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(v,
				"rotationY", -90f, 0f);
		invisToVis.setDuration(500);
		invisToVis.setInterpolator(decelerator);
		visToInvis.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator anim) {
				//visibleList.setVisibility(View.GONE);
				invisToVis.start();
				//invisibleList.setVisibility(View.VISIBLE);
			}
		});
		visToInvis.start();
	}

}
