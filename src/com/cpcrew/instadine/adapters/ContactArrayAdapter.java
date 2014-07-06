package com.cpcrew.instadine.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cpcrew.instadine.models.User;

public class ContactArrayAdapter extends ArrayAdapter<User>{

	
	public ContactArrayAdapter(Context context, ArrayList<User> contacts) {
		super(context, 0, contacts);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}

}
