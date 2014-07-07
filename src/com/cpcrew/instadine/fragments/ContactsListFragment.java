package com.cpcrew.instadine.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.adapters.ContactArrayAdapter;
import com.cpcrew.instadine.models.User;

public class ContactsListFragment extends Fragment {

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
		// TODO Fetch contacts

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		makeToast("Method 3: To implement : Fetch contacts from databse");
		View v = inflater.inflate(R.layout.fragment_contacts, container, false);
		lvContacts = (ListView) v.findViewById(R.id.lvContacts);
		pb = (ProgressBar) v.findViewById(R.id.pbContacts);
		lvContacts.setAdapter(contactAdapter);
		return v;

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

}
