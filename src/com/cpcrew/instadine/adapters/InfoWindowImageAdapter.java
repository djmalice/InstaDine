package com.cpcrew.instadine.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class InfoWindowImageAdapter extends ArrayAdapter<String> {

	public InfoWindowImageAdapter(Context context, List<String> images) {
		super(context, R.layout.infowindow_image_layout, images);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get the data item for the position
				String fbid = getItem(position);
				// Find or inflate the template
				ImageView v;
				if(convertView == null){
					v= (ImageView)LayoutInflater.from(getContext()).inflate(R.layout.infowindow_image_layout,parent,false);
				} else {
					v = (ImageView)convertView;
					v.setImageResource(android.R.color.transparent);
				}
						
				String imageUri = Utils.getFacebookImageUrl(fbid);
				//Populate views with the fb image data
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(imageUri,v);
				
				    
			
				return v;
	}
}

