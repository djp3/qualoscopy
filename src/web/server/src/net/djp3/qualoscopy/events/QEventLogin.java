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

import net.djp3.qualoscopy.webhandlers.HandlerCheckSession;
import net.djp3.qualoscopy.webhandlers.HandlerLogin;
import net.minidev.json.JSONObject;

public class QEventLogin extends QEventCheckSession {

	private String saltedHashedPassword;

	public QEventLogin(HandlerLogin login){
		this(login,login.getSaltedHashedPassword());
	}
	
	public QEventLogin(QEventLogin login){
		this(login,login.getSaltedHashedPassword());
	}
	
	public QEventLogin(HandlerCheckSession checkSession,String saltedHashedPassword) {
		super(checkSession);
		this.saltedHashedPassword = saltedHashedPassword;
	}
	
	public QEventLogin(QEventCheckSession checkSession,String saltedHashedPassword) {
		super(checkSession);
		this.saltedHashedPassword = saltedHashedPassword;
	}
	
	
	public String getSaltedHashedPassword() {
		return saltedHashedPassword;
	}

	public void setSaltedHashedPassword(String saltedHashedPassword) {
		this.saltedHashedPassword = saltedHashedPassword;
	}

	public JSONObject toJSON() {
		JSONObject ret = super.toJSON();
		ret.put("salted_hashed_password", this.getSaltedHashedPassword());
		return ret;
	}

	static public QEventLogin fromJSON(JSONObject in) {
		QEventCheckSession parent = QEventCheckSession.fromJSON(in);
		
		String _SHP = (String) in.get("salted_hashed_password");

		return (new QEventLogin(parent,_SHP));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result;
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
		if (!(obj instanceof QEventLogin)) {
			return false;
		}
		return true;
	}

}
