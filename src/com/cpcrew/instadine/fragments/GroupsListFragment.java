package com.cpcrew.instadine.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cpcrew.instadine.adapters.GroupArrayAdapter;
import com.cpcrew.instadine.models.Group;

public class GroupsListFragment extends Fragment{
	
	private static String TAG = GroupsListFragment.class.getSimpleName();
	private ArrayList<Group> groups;
	private GroupArrayAdapter groupAdapter;
	protected ListView lvGroups;
	private ProgressBar pb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		groups = new ArrayList<Group>();
		groupAdapter = new GroupArrayAdapter(getActivity(), groups);
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
