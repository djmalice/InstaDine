package com.cpcrew.instadine.fragments;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.activities.VotingActivity;
import com.cpcrew.instadine.adapters.RestaurantArrayAdapter;
import com.cpcrew.instadine.models.Rest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DeciderViewFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_decider, container,
				false);
		// Rest rest = (Rest) getArguments().getSerializable("votingRest");
		Rest rest = ((VotingActivity) getActivity())
				.getHighestVotedRestaraunt(); // better solution ???
		updateRestView(view, rest);
		return view;
	}

	private void updateRestView(View view, Rest rest) {

		Typeface SaginawBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SaginawBold.ttf");
		
		// Message ( TODO check if voting in progress or complete )
		TextView tvMessage = (TextView)view.findViewById(R.id.tvMessage);
		tvMessage.setTypeface(SaginawBold);
		tvMessage.setText("You are going to \n" + rest.getName() + "\n with your group");
		
		
		// Rest image Big
//		ImageView ivRest = (ImageView) view.findViewById(R.id.ivRestImage);
//		String photoFile = rest.getImageUrl();
//		ImageLoader imageLoader = ImageLoader.getInstance();
//		if (photoFile != null && !(photoFile.equals(""))) {
//			imageLoader.displayImage(photoFile, ivRest);
//		}
		
		// Rest list View Adapter
		ArrayList<Rest> restaurants = new ArrayList<Rest>();
		restaurants.add(rest);
		RestaurantArrayAdapter restAdapter = new RestaurantArrayAdapter(
				getActivity(), restaurants);
		ListView lvRestaurants = (ListView) view
				.findViewById(R.id.lvRestaurant);
		lvRestaurants.setAdapter(restAdapter);
	}

}
