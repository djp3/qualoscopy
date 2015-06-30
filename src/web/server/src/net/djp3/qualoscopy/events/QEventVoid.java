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
package net.djp3.qualoscopy;

import net.djp3.qualoscopy.events.QEvent;
import net.minidev.json.JSONObject;

public class QEventVoid extends QEvent {

public QEventVoid(){
	super((String)null,(String)null);
}

public QEventVoid(String worldName,String worldPassword){
	super(worldName,worldPassword);
}

public QEventVoid(String worldName,byte[] worldPassword){
	super(worldName,worldPassword);
}

@Override
public JSONObject toJSON() {
	return(super.toJSON());
}

static public QEventVoid fromJSON(JSONObject in) {
	QEvent parent = QEvent.fromJSON(in);
	String worldName = parent.getWorldName();
	byte[] worldHashedPassword = parent.getWorldHashedPassword();
	return(new QEventVoid(worldName,worldHashedPassword));
}

}

