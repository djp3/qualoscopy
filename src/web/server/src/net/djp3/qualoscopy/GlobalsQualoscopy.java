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

import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.CalendarCache;
import edu.uci.ics.luci.utility.Globals;

public class GlobalsQualoscopy extends Globals {

static{
    /* Test that we are using GMT as the default */
    if(!TimeZone.getDefault().equals(CalendarCache.TZ_GMT)){
            throw new RuntimeException("We are in the wrong timezone:\n"+TimeZone.getDefault()+"\n We want to be in:\n "+CalendarCache.TZ_GMT);
    }

    /* Test that we are using UTF-8 as default */
    String c = java.nio.charset.Charset.defaultCharset().name();
    if(!c.equals("UTF-8")){
            throw new IllegalArgumentException("The character set is not UTF-8:"+c);
    }

}

private static final String LOG4J_CONFIG_FILE_DEFAULT = "Qualoscopy.log4j.xml";

static final long ONE_SECOND = 1000;
static final long ONE_MINUTE = 60 * ONE_SECOND;

private static transient volatile Logger log = null;
public static Logger getLog(){
	if(log == null){
		log = LogManager.getLogger(GlobalsQualoscopy.class);
	}
	return log;
}
String version = null;

@Override
public String getSystemVersion() {
	return version;
}

public GlobalsQualoscopy(String version){
	this(version,false);
}

public GlobalsQualoscopy(String version, boolean testing){
	super();
	this.version = version;
	setTesting(testing);
	setLog4JPropertyFileName(LOG4J_CONFIG_FILE_DEFAULT);
}

public static GlobalsQualoscopy getGlobalsTerraTower(){
	return (GlobalsQualoscopy) Globals.getGlobals();
}



}

