package net.djp3.qualoscopy.datastore;


public class Permission {
	
	private String permissionID = null;
	
	private AdminDomain ad = null;
	private boolean canRead = false;
	private boolean canEdit = false;
	private boolean canArchive = false;
	private boolean canDelete = false;
	private boolean canReadUsers = false;
	private boolean canEditUsers = false;
	private boolean canAddUsers = false;
	
	
	
	public Permission(AdminDomain adminDomain,boolean canRead, boolean canEdit, boolean canArchive,
			boolean canDelete, boolean canReadUsers, boolean canEditUsers,
			boolean canAddUsers) {
		super();
		this.setAdminDomain(adminDomain);
		this.setCanRead(canRead);
		this.setCanEdit(canEdit);
		this.setCanArchive(canArchive);
		this.setCanDelete(canDelete);
		this.setCanReadUsers(canReadUsers);
		this.setCanEditUsers(canEditUsers);
		this.setCanAddUsers(canAddUsers);
	}
	
	
	public String getPermissionID() {
		return permissionID;
	}


	public void setPermissionID(String permissionID) {
		this.permissionID = permissionID;
	}


	public AdminDomain getAdminDomain() {
		return ad;
	}


	public void setAdminDomain(AdminDomain ad) {
		this.ad = ad;
	}


	public boolean isCanRead() {
		return canRead;
	}
	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}
	public boolean getCanEdit() {
		return canEdit;
	}
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	public boolean getCanArchive() {
		return canArchive;
	}
	public void setCanArchive(boolean canArchive) {
		this.canArchive = canArchive;
	}
	public boolean getCanDelete() {
		return canDelete;
	}
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	public boolean getCanReadUsers() {
		return canReadUsers;
	}
	public void setCanReadUsers(boolean canReadUsers) {
		this.canReadUsers = canReadUsers;
	}
	public boolean getCanEditUsers() {
		return canEditUsers;
	}
	public void setCanEditUsers(boolean canEditUsers) {
		this.canEditUsers = canEditUsers;
	}
	public boolean getCanAddUsers() {
		return canAddUsers;
	}
	public void setCanAddUsers(boolean canAddUsers) {
		this.canAddUsers = canAddUsers;
	}
	
	
}
