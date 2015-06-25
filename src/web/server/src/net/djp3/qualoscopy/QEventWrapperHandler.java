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
import net.djp3.qualoscopy.events.handlers.QEventHandler;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lmax.disruptor.EventHandler;

public class QEventWrapperHandler implements EventHandler<QEventWrapper> {

	private static transient volatile Logger log = null;

	public static Logger getLog() {
		if (log == null) {
			log = LogManager.getLogger(QEventWrapperHandler.class);
		}
		return log;
	}

	@Override
	public void onEvent(QEventWrapper eventWrapper, long sequence,
			boolean endOfBatch) {

		QEventHandler handler = eventWrapper.getHandler();
		QEvent event = eventWrapper.getEvent();
		long timestamp = eventWrapper.getTimestamp();
		getLog().debug("Handling a " + eventWrapper.getEventType().toString());
		JSONObject result = null;
		if (handler != null) {
			result = handler.checkParameters(timestamp, event);
			if (result == null) {
				JSONObject result2 = handler.onEvent();
				result = result2;
			}
		}

		for (QEventHandlerResultListener rl : eventWrapper.getResultListeners()) {
			rl.onFinish(result);
		}
		getLog().debug(
				"Done Handling a " + eventWrapper.getEventType().toString());
	}
}
