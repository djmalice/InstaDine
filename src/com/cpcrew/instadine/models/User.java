package com.cpcrew.instadine.models;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("User")
public class User  {
	
	private ParseUser parseUser;
	
	private User() {
		super();
	}
	
	public User(ParseUser pUser) {
		parseUser = pUser;
	}
	
	public void setParseUser(ParseUser pUser) {
		parseUser = pUser;
	}
	
	public ParseUser getParseUser() {
		return parseUser;
	}

	public String getObjectId() {
		return parseUser.getString("objectId");
	}
	
	public String getId() {
		//return getString("objectId");
		return parseUser.getObjectId();
	}
	
	public void setFirstName(String first) {
		parseUser.put("first" , first);
	}
	
	public String getFirstName() {
		return parseUser.getString("first");
	}
	
	public void setLastName(String last) {
		parseUser.put("last", last);
	}
	
	public String getLastName() {
		return parseUser.getString("last");
	}
	
	public ParseRelation<ParseUser> getFriendRelation() {
		return parseUser.getRelation("pfriends");
	}
	
	/**
	 *  link Friend(User) and User
	 *  
	 */
	public void addToFriends(User friend ) {
		getFriendRelation().add(friend.parseUser);
		parseUser.saveInBackground();
	}
	
	public void removeFromFriends(User friend) {
		getFriendRelation().remove(friend.parseUser);
		parseUser.saveInBackground();
	}
	
//	public ParseRelation<Group> getGroupRelation() {
//		return getRelation("gid");
//	}
	
	public static User wrapParseUser(ParseUser pUser ) {
		User user = new User(pUser);
		return user;
	}
	
	public static List<User> wrapParseUsers(List<ParseUser> pUsers ) {
		List<User> users = new ArrayList<User>();
		for ( ParseUser pUser : pUsers) {
			User user = wrapParseUser(pUser);
			users.add(user);
		}
		return users;
	}
	
	public ParseFile getProfileImage() {
		return parseUser.getParseFile("profileImage");
	}
	
	public String getFacebookId() {
		return parseUser.getString("facebookid");
	}
	
	public List<String> getFacebookFriendsIds() {
		return parseUser.getList("friendsfb");
	}
	
	public String getUserName() {
		return parseUser.getString("username");
	}
	
	
// Never do this ( it uses inverse relationship concept to query)
//	/**
//	 *  link Group and User
//	 *  
//	 */
//	public void addToGroup(Group group ) {
//		getGroupRelation().add(group);
//		saveInBackground();
//	}
//	
//	public void removeFromGroup(Group group) {
//		getGroupRelation().remove(group);
//		saveInBackground();
//	}
}
