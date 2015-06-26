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


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import edu.uci.ics.luci.utility.Quittable;

public class QEventWrapperQueuer implements Quittable{ 

private static transient volatile Logger log = null;
public static Logger getLog(){
	if(log == null){
		log = LogManager.getLogger(QEventWrapperQueuer.class);
	}
	return log;
}

private final RingBuffer<QEventWrapper> ringBuffer;
private BufferedWriter logWriter = null;
private boolean quitting  = false;
private Disruptor<QEventWrapper> disruptor;

/** Constructor that doesn't log to a file
 * 
 * @param ringBuffer
 */
public QEventWrapperQueuer(Disruptor<QEventWrapper> disruptor, RingBuffer<QEventWrapper> ringBuffer){
	this(disruptor,ringBuffer,null);
}

/** Constructor that logs to a file
 * 
 * @param ringBuffer
 * @param logFileName
 */
public QEventWrapperQueuer(Disruptor<QEventWrapper> disruptor,RingBuffer<QEventWrapper> ringBuffer,String logFileName)
{
	if(disruptor == null){
		getLog().fatal("disruptor can't be null");
		throw new IllegalArgumentException("disruptor can't be null");
	}
	
    this.disruptor = disruptor;
    
	if(ringBuffer == null){
		getLog().fatal("ringBuffer can't be null");
		throw new IllegalArgumentException("ringBuffer can't be null");
	}
	
    this.ringBuffer = ringBuffer;
    
    
    /* Try and set up a file logger */
	if(logFileName != null){
		Path newFile = Paths.get(logFileName);
		try {
			Files.deleteIfExists(newFile);
			newFile = Files.createFile(newFile);
			logWriter = Files.newBufferedWriter(newFile, Charset.defaultCharset());
		} catch (IOException e) {
			getLog().error("Error creating event Log File, "+logFileName+"\n"+e);
		}
	}
		
}

private static final EventTranslatorOneArg<QEventWrapper, QEventWrapper> TRANSLATOR =
    new EventTranslatorOneArg<QEventWrapper,QEventWrapper>()
    {
        public void translateTo(QEventWrapper event, long sequence, QEventWrapper incoming)
        {
            event.set(incoming);
        }
    };

public void onData(QEventWrapper incoming) {
	if (!isQuitting()) {
		/* Write event to log */
		if (logWriter != null) {
			try {
				logWriter.append(incoming.toJSON().toJSONString());
				logWriter.newLine();
				logWriter.flush();
			} catch (IOException exception) {
				getLog().error(
						"Error writing event to log file:"
								+ incoming.getEventType().toString());
			}
		}

		/* Submit event for handling */
		ringBuffer.publishEvent(TRANSLATOR, incoming);
	}
}

@Override
public void setQuitting(boolean quitting) {
	if(this.quitting && !quitting){
		getLog().warn("Already quitting, can't unquit");
	}
	else{
		if(quitting){
			this.quitting = quitting;
			if(logWriter != null){
				try {
					logWriter.close();
				} catch (IOException e) {
				}
				logWriter = null;
			}
			if(disruptor != null){
				disruptor.shutdown();
				disruptor = null;
			}
		}
	}
}

@Override
public boolean isQuitting() {
	return quitting;
}
}

