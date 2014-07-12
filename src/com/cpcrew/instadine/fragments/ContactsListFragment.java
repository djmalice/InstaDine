package com.cpcrew.instadine.fragments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.adapters.ContactArrayAdapter;
import com.cpcrew.instadine.models.User;
import com.parse.ParseObject;

public class ContactsListFragment extends Fragment {

	private static String TAG = ContactsListFragment.class.getSimpleName();
	private ArrayList<User> contacts;
	private ContactArrayAdapter contactAdapter;
	protected ListView lvContacts;
	private ProgressBar pb;
	HashSet<String> selectedSet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contacts = new ArrayList<User>();
		// TODO Figure how to send ParseObjects
		ArrayList<String> selContacts = getArguments().getStringArrayList( "selectedusers");
		selectedSet = null;
		if (selContacts != null )
			selectedSet = new HashSet<String>(selContacts);
		contactAdapter = new ContactArrayAdapter(getActivity(), contacts ,selectedSet);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_contacts, container, false);
		lvContacts = (ListView) v.findViewById(R.id.lvContacts);
		pb = (ProgressBar) v.findViewById(R.id.pbContacts);
		lvContacts.setAdapter(contactAdapter);
		lvContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		return v;

	}
	public void setContactAdapter(List<User> friends ) {
		contactAdapter.clear();
		Log.d(TAG, "Number of friends " + friends.size());
		contactAdapter.addAll(friends);
		contactAdapter.selectNone();
	}
	
	// TODO lvContacts.getCheckedItemPositions() Not working
	// Check with Nathan
	public ArrayList<User> getCheckedContacts() {
		int cntChoice = lvContacts.getCount();
		ArrayList<User> selUsers = new ArrayList<User>();
		SparseBooleanArray sparseBooleanArray = lvContacts.getCheckedItemPositions();
		for (int i = 0; i < cntChoice; i++) {
			if (sparseBooleanArray.get(i) == true) {
				selUsers.add((User) lvContacts.getItemAtPosition(i));				
			} else if (sparseBooleanArray.get(i) == false) {
			}
		}
		return selUsers;
	}
	
	// TODO lvContacts.getCheckedItemPositions() Not working
	// Check with Nathan
	public ArrayList<String> getCheckedContactsIds() {
		int cntChoice = lvContacts.getCount();
		ArrayList<String> selUsers = new ArrayList<String>();
		SparseBooleanArray sparseBooleanArray = lvContacts.getCheckedItemPositions();
		for (int i = 0; i < cntChoice; i++) {
			if (sparseBooleanArray.get(i) == true) {
				User user = (User) lvContacts.getItemAtPosition(i);
				selUsers.add(user.getObjectId() );	
			} else if (sparseBooleanArray.get(i) == false) {
				System.out.println("false");
			}
		}
		return selUsers;
	}
 
	public void showProgressBar() {
		pb.setVisibility(ProgressBar.VISIBLE);
	}

	public void hideProgressBar() {
		// run a background job and once complete
		pb.setVisibility(ProgressBar.GONE);
	}

	// TODO Remove once methods have been implemented
	public void makeToast(final String msg) {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	// wrokaround Return friends that have been selected
	public ArrayList<String> getCheckedUsers() {
		ArrayList<String> selectedUsers = new ArrayList<String>();
		for (int i = 0; i < lvContacts.getCount();++i ) {
			if (contactAdapter.checkedUsers.get(i) == true) {
				User user = (User) lvContacts.getItemAtPosition(i);
				selectedUsers.add(user.getObjectId() );	
			}
		}
		return selectedUsers;
	}
		

}
