package net.djp3.qualoscopy.datastore;

import java.util.HashSet;
import java.util.Set;

public class User {
	
	String userID = null;
	
	String firstName = null;
	String lastName = null;
	Set<Permission> permissions = new HashSet<Permission>();
	
	public User(String firstName, String lastName) {
		super();
		this.setFirstName(firstName);
		this.setLastName(lastName);
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	

}
