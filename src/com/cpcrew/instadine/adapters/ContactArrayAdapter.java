package com.cpcrew.instadine.adapters;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.User;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

public class ContactArrayAdapter extends ArrayAdapter<User> {
	
	HashSet<String> mSelContacts;

	public ContactArrayAdapter(Context context, ArrayList<User> contacts , HashSet<String> selContacts) {
		super(context, 0, contacts);
		mSelContacts = selContacts;
	}

	private static class ViewHolder {
		TextView tvName;
		ParseImageView pivPhoto;
		ImageView ivSelected;
		CheckBox cbSelect;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User user = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflator = LayoutInflater.from(getContext());
			convertView = inflator
					.inflate(R.layout.contact_item, parent, false);
			viewHolder.pivPhoto = (ParseImageView) convertView
					.findViewById(R.id.ivPhoto);
			viewHolder.tvName = (TextView) convertView
					.findViewById(R.id.tvName);
			viewHolder.ivSelected = (ImageView)convertView.findViewById(R.id.ivSelected);
			viewHolder.cbSelect = (CheckBox)convertView.findViewById(R.id.cbSelect);
			convertView.setTag(viewHolder);
		}
		System.out.println(user.getFirstName() + " " + user.getLastName());
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.tvName.setText(user.getFirstName() + " "
				+ user.getLastName());
		
		if (isAlreadySelected(user)) {
			viewHolder.cbSelect.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.ivSelected.setVisibility(View.INVISIBLE);
		}
		// Load the image TODO - Verify the right API
		ParseFile photoFile = user.getParseFile("profileImage");
		if (photoFile != null) {
			viewHolder.pivPhoto.setParseFile(photoFile);
			viewHolder.pivPhoto.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					// nothing to do
				}
			});
		}

		return convertView;
	}
	
	private boolean isAlreadySelected(User user) {
		if ( mSelContacts != null ) {
			if ( mSelContacts.contains(user.getId()))
				return true;
		}
		return false;
	}

}
