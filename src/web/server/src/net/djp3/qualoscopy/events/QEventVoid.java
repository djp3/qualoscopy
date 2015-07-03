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

public class QEventVoid extends QEvent {

	/*This is just here so I can generate hashCode and equals */
	private long serialVersionUID = -1470891569401787268L;

	public QEventVoid() {
		super();
	}

	@Override
	public JSONObject toJSON() {
		return (super.toJSON());
	}

	static public QEventVoid fromJSON(JSONObject in) {
		return (new QEventVoid());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (int) (serialVersionUID ^ (serialVersionUID >>> 32));
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
		if (!(obj instanceof QEventVoid)) {
			return false;
		}
		QEventVoid other = (QEventVoid) obj;
		if (serialVersionUID != other.serialVersionUID) {
			return false;
		}
		return true;
	}
}
