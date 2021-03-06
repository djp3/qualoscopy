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

public class QEvent {

	private QEventType type;

	public QEvent(QEventType type) {
		setType(type);
	}
	
	public QEventType getType() {
		return type;
	}

	private void setType(QEventType type) {
		this.type = type;
	}

	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		ret.put("event_type", QEventType.toString(getType()));
		return ret;
	}
	

	static public QEvent fromJSON(JSONObject in) {
		//QEvent parent = QEvent.fromJSON(in);

		String _type = (String) in.get("event_type");
		return (new QEvent(QEventType.fromString(_type)));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof QEvent)) {
			return false;
		}
		QEvent other = (QEvent) obj;
		if (type != other.type) {
			return false;
		}
		return true;
	}

	

}
