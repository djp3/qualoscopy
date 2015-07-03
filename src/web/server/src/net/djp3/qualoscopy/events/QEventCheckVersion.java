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

import net.minidev.json.JSONObject;

public class QEventCheckVersion extends QEvent {

	private String correctVersion;
	private String proposedVersion;

	public QEventCheckVersion(String correctVersion,String proposedVersion) {
		super();
		this.correctVersion = correctVersion;
		this.proposedVersion = proposedVersion;
	}

	public String getCorrectVersion() {
		return this.correctVersion;
	}
	
	public String getProposedVersion() {
		return this.proposedVersion;
	}

	public JSONObject toJSON() {
		JSONObject ret = super.toJSON();
		ret.put("correct_version", this.getCorrectVersion());
		ret.put("proposed_version", this.getProposedVersion());
		return ret;
	}

	static public QEventCheckVersion fromJSON(JSONObject in) {
		//QEvent parent = QEvent.fromJSON(in);

		String correctVersion = (String) in.get("correct_version");
		String proposedVersion = (String) in.get("proposed_version");

		return (new QEventCheckVersion(correctVersion,proposedVersion));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((correctVersion == null) ? 0 : correctVersion.hashCode());
		result = prime * result
				+ ((proposedVersion == null) ? 0 : proposedVersion.hashCode());
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
		if (correctVersion == null) {
			if (other.correctVersion != null) {
				return false;
			}
		} else if (!correctVersion.equals(other.correctVersion)) {
			return false;
		}
		if (proposedVersion == null) {
			if (other.proposedVersion != null) {
				return false;
			}
		} else if (!proposedVersion.equals(other.proposedVersion)) {
			return false;
		}
		return true;
	}

}
