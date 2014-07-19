package com.cpcrew.instadine.adapters;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.Group;
import com.cpcrew.instadine.models.LoggedInUser;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;


public class GroupArrayAdapter extends ArrayAdapter<Group>{

	public static String TAG = GroupArrayAdapter.class.getSimpleName();
	
	public GroupArrayAdapter(Context context, ArrayList<Group> groups) {
		super(context, 0, groups);
	}

	
    private static class ViewHolder {
    	ParseImageView ivGroupPhoto;
    	TextView tvGroupName;
    	TextView tvGroupMembers;
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Group thisGroup = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflator = LayoutInflater.from(getContext());
			convertView = inflator.inflate(R.layout.group_item, parent, false);
			viewHolder.ivGroupPhoto = (ParseImageView) convertView
					.findViewById(R.id.ivGroupPhoto);
			viewHolder.tvGroupName = (TextView) convertView
					.findViewById(R.id.tvGroupName);
			viewHolder.tvGroupMembers = (TextView) convertView
					.findViewById(R.id.tvGroupMembers);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.tvGroupName.setText(thisGroup.getGroupName());
		// TODO Get the members name as a string
		//viewHolder.tvGroupMembers.setText(thisGroup.getDesc());
		viewHolder.tvGroupMembers.setText(createDescToShow(thisGroup.getDesc()));
		// Load the image
		ParseFile photoFile = thisGroup.getPhotoFile();
		if (photoFile != null) {
			viewHolder.ivGroupPhoto.setParseFile(photoFile);
			viewHolder.ivGroupPhoto.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					// nothing to do
				}
			});
		}

		return convertView;
	}
	
	private String createDescToShow(String input) {	
		String currentUser = LoggedInUser.getcurrentUser().getFirstName();
		String result = input.replaceFirst(Pattern.quote(currentUser), "");
		result = result.replaceAll(",$", "");
		System.out.println(result);
		result = result.replaceAll(", ,", ", ");
		System.out.println(result);
		result = result.replaceAll("^ ", "");
		System.out.println(result);
		result = result.replaceAll("^,", "");
		result = result.replaceAll(",,", ", ");
		System.out.println(result);
		return "Me, " + result;
	}
	

}
