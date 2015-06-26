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

import java.util.Arrays;

import net.djp3.qualoscopy.PasswordUtils;
import net.minidev.json.JSONObject;

public class QEvent {

	private String worldName;
	private byte[] worldHashedPassword;

	public QEvent(String worldName, String worldPassword) {
		this(worldName, PasswordUtils.hashPassword(worldPassword));
	}

	public QEvent(String worldName, byte[] worldHashedPassword) {
		setWorldName(worldName);
		setWorldHashedPassword(worldHashedPassword);
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public byte[] getWorldHashedPassword() {
		if (worldHashedPassword == null) {
			return new byte[0];
		} else {
			return Arrays.copyOf(worldHashedPassword,
					worldHashedPassword.length);
		}
	}

	private void setWorldHashedPassword(byte[] worldHashedPassword) {
		if (worldHashedPassword == null) {
			this.worldHashedPassword = new byte[0];
		} else {
			this.worldHashedPassword = Arrays.copyOf(worldHashedPassword,
					worldHashedPassword.length);
		}
	}

	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		ret.put("world_name", this.getWorldName());
		ret.put("world_hashed_password",
				PasswordUtils.bytesToHexString(this.getWorldHashedPassword()));
		return ret;
	}

	static public QEvent fromJSON(JSONObject in) {
		String worldName = null;
		Object _worldName = in.get("world_name");
		if (_worldName != null) {
			worldName = (String) _worldName;
		}

		byte[] worldHashedPassword = new byte[0];
		Object _worldHashedPassword = in.get("world_hashed_password");
		if (_worldHashedPassword != null) {
			worldHashedPassword = PasswordUtils
					.hexStringToByteArray((String) _worldHashedPassword);
		}
		return (new QEvent(worldName, worldHashedPassword));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(worldHashedPassword);
		result = prime * result
				+ ((worldName == null) ? 0 : worldName.hashCode());
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
		if (!Arrays.equals(worldHashedPassword, other.worldHashedPassword)) {
			return false;
		}
		if (worldName == null) {
			if (other.worldName != null) {
				return false;
			}
		} else if (!worldName.equals(other.worldName)) {
			return false;
		}
		return true;
	}

}