/*
	Copyright 2015
		Donald J. Patterson
*/
/*
	This file is part of the Qualoscopy Web Service, i.e. "Qualoscopy"

    Qualoscopy is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Utilities is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Utilities.  If not, see <http://www.gnu.org/licenses/>.
*/


package net.djp3.qualoscopy.events;

import net.djp3.qualoscopy.webhandlers.HandlerCheckVersion;
import net.minidev.json.JSONObject;

public class QEventCheckVersion extends QEvent {

	private String aPIVersion = null;
	private String requestedVersion = null;

	public QEventCheckVersion(HandlerCheckVersion checkVersion) {
		this(checkVersion.getAPIVersion(),checkVersion.getRequestedVersion());
	}
	
	public QEventCheckVersion(String aPIVersion,String requestedVersion) {
		super();
		this.setAPIVersion(aPIVersion);
		this.setRequestedVersion(requestedVersion);
	}
	

	private void setAPIVersion(String aPIVersion){
		this.aPIVersion = aPIVersion;
	}
	
	private void setRequestedVersion(String requestedVersion){
		this.requestedVersion = requestedVersion;
	}
	
	public String getAPIVersion(){
		return this.aPIVersion;
	}
	
	public String getRequestedVersion(){
		return this.requestedVersion;
	}


	public JSONObject toJSON() {
		JSONObject ret = super.toJSON();
		ret.put("api_version", getAPIVersion());
		ret.put("requested_version", getRequestedVersion());
		return ret;
	}

	static public QEventCheckVersion fromJSON(JSONObject in) {
		//QEvent parent = QEvent.fromJSON(in);

		String _APIVersion = (String) in.get("api_version");
		String _RequestedVersion = (String) in.get("requested_version");

		return (new QEventCheckVersion(_APIVersion,_RequestedVersion));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((aPIVersion == null) ? 0 : aPIVersion.hashCode());
		result = prime
				* result
				+ ((requestedVersion == null) ? 0 : requestedVersion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof QEventCheckVersion)) {
			return false;
		}
		QEventCheckVersion other = (QEventCheckVersion) obj;
		if (aPIVersion == null) {
			if (other.aPIVersion != null) {
				return false;
			}
		} else if (!aPIVersion.equals(other.aPIVersion)) {
			return false;
		}
		if (requestedVersion == null) {
			if (other.requestedVersion != null) {
				return false;
			}
		} else if (!requestedVersion.equals(other.requestedVersion)) {
			return false;
		}
		return true;
	}


}
