package com.cpcrew.instadine.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cpcrew.instadine.models.Group;

public class GroupArrayAdapter extends ArrayAdapter<Group>{

	
	public GroupArrayAdapter(Context context, ArrayList<Group> groups) {
		super(context, 0, groups);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}

}
