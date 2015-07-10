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
import net.djp3.qualoscopy.webhandlers.HandlerInitiateSession;
import net.minidev.json.JSONObject;

public class QEventInitiateSession extends QEventCheckVersion {
	
	private String source;
	
	public QEventInitiateSession(HandlerInitiateSession initiateSession){
		this((HandlerCheckVersion)initiateSession, initiateSession.getSource());
	}
	
	public QEventInitiateSession(QEventInitiateSession initiateSession){
		this((QEventCheckVersion)initiateSession, initiateSession.getSource());
	}
	
	public QEventInitiateSession(HandlerCheckVersion checkVersion,String source){
		super(checkVersion);
		this.setSource(source);
	}
	
	public QEventInitiateSession(QEventCheckVersion checkVersion,String source){
		super(checkVersion.getAPIVersion(),checkVersion.getRequestedVersion());
		this.setSource(source);
	}
	
	private void setSource(String source){
		this.source = source;
	}
	
	public String getSource() {
		return this.source;
	}

	
	public JSONObject toJSON() {
		JSONObject ret = super.toJSON();
		
		ret.put("source", this.getSource());

		return ret;
	}

	static public QEventInitiateSession fromJSON(JSONObject in) {
		QEventCheckVersion parent = QEventCheckVersion.fromJSON(in);
		
		String _Source = (String) in.get("source");

		return (new QEventInitiateSession(parent,_Source));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		if (!(obj instanceof QEventInitiateSession)) {
			return false;
		}
		QEventInitiateSession other = (QEventInitiateSession) obj;
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		return true;
	}

}
	