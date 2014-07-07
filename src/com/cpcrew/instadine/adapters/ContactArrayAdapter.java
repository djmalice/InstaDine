package com.cpcrew.instadine.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.User;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

public class ContactArrayAdapter extends ArrayAdapter<User>{

	
	public ContactArrayAdapter(Context context, ArrayList<User> contacts) {
		super(context, 0, contacts);
	}
	private static class ViewHolder {
		TextView tvName;
		ParseImageView pivPhoto;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User user = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null ) {
			viewHolder = new ViewHolder();
			LayoutInflater inflator = LayoutInflater.from(getContext());
			convertView = inflator.inflate(R.layout.contact_item, parent, false);
			viewHolder.pivPhoto = (ParseImageView) convertView.findViewById(R.id.ivPhoto);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.tvName.setText(user.getFirstName() + " " + user.getLastName());
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
		}
		return convertView;
	}

}
