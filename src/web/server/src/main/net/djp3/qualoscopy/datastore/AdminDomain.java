package net.djp3.qualoscopy.datastore;


public class AdminDomain {
	
	String adminDomainID = null;
	String name = null;
	
	
	public AdminDomain(String name) {
		super();
		this.setName(name);
	}
	
	public String getAdminDomainID() {
		return adminDomainID;
	}
	public void setAdminDomainID(String adminDomainID) {
		this.adminDomainID = adminDomainID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
