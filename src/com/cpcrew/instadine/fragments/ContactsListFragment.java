package com.cpcrew.instadine.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cpcrew.instadine.adapters.ContactArrayAdapter;
import com.cpcrew.instadine.models.User;

public class ContactsListFragment extends Fragment{
	
	private static String TAG = ContactsListFragment.class.getSimpleName();
	private ArrayList<User> contacts;
	private ContactArrayAdapter contactAdapter;
	protected ListView lvContacts;
	private ProgressBar pb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contacts = new ArrayList<User>();
		contactAdapter = new ContactArrayAdapter(getActivity(), contacts);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	public void showProgressBar() {
		pb.setVisibility(ProgressBar.VISIBLE);
	}
	
	public void hideProgressBar() {
		// run a background job and once complete
		pb.setVisibility(ProgressBar.GONE);
	}

}
