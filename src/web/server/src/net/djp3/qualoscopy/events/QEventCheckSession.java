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

import net.djp3.qualoscopy.api.HandlerCheckSession;
import net.djp3.qualoscopy.webhandlers.HandlerCheckVersion;
import net.minidev.json.JSONObject;

public class QEventCheckSession extends QEventCheckVersion {

	private String source;
	private String userID;
	private String hashedSaltedSessionID;

	public QEventCheckSession(HandlerCheckSession checkSession) {
		this((HandlerCheckVersion)checkSession,checkSession.getUserID(), checkSession.getSessionID(),checkSession.getSource());
	}
	
	public QEventCheckSession(QEventCheckSession checkSession) {
		this((QEventCheckVersion)checkSession,checkSession.getUserID(), checkSession.getHashedSaltedSessionID(),checkSession.getSource());
	}
	
	public QEventCheckSession(HandlerCheckVersion checkVersion,String userID,String hashedSaltedSessionID,String source) {
		super(checkVersion);
		this.setSource(source);
		this.setUserID(userID);
		this.setHashedSaltedSessionID(hashedSaltedSessionID);
	}
	
	public QEventCheckSession(QEventCheckVersion checkVersion,String userID,String hashedSaltedSessionID,String source) {
		super(checkVersion.getAPIVersion(),checkVersion.getRequestedVersion());
		this.setUserID(userID);
		this.setHashedSaltedSessionID(hashedSaltedSessionID);
		this.setSource(source);
	}
	
	private void setUserID(String userID){
		this.userID = userID;
	}
	
	private void setHashedSaltedSessionID(String hashedSaltedSessionID){
		this.hashedSaltedSessionID = hashedSaltedSessionID;
	}
	
	private void setSource(String source){
		this.source = source;
	}


	public String getUserID() {
		return this.userID;
	}
	
	public String getHashedSaltedSessionID() {
		return this.hashedSaltedSessionID;
	}
	
	public String getSource() {
		return this.source;
	}

	public JSONObject toJSON() {
		JSONObject ret = super.toJSON();
		ret.put("user_id", this.getUserID());
		ret.put("hashed_salted_session_id", this.getHashedSaltedSessionID());
		ret.put("source", this.getSource());
		return ret;
	}

	static public QEventCheckSession fromJSON(JSONObject in) {
		QEventCheckVersion parent = QEventCheckVersion.fromJSON(in);

		String _UserID = (String) in.get("user_id");
		String _HashedSaltedSessionID = (String) in.get("hashed_salted_session_id");
		String _Source = (String) in.get("source");

		return (new QEventCheckSession(parent,_UserID,_HashedSaltedSessionID,_Source));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((hashedSaltedSessionID == null) ? 0 : hashedSaltedSessionID
						.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
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
		if (!(obj instanceof QEventCheckSession)) {
			return false;
		}
		QEventCheckSession other = (QEventCheckSession) obj;
		if (hashedSaltedSessionID == null) {
			if (other.hashedSaltedSessionID != null) {
				return false;
			}
		} else if (!hashedSaltedSessionID.equals(other.hashedSaltedSessionID)) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		if (userID == null) {
			if (other.userID != null) {
				return false;
			}
		} else if (!userID.equals(other.userID)) {
			return false;
		}
		return true;
	}

	
	


}
