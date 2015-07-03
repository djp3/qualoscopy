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


package net.djp3.qualoscopy.events.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.djp3.qualoscopy.GlobalsQualoscopy;
import net.djp3.qualoscopy.events.QEventCheckVersion;
import net.djp3.qualoscopy.events.QEventVoid;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class QEventHandlerVoidTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		while(Globals.getGlobals() != null){
			Thread.sleep(100);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Globals.setGlobals(null);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testParameters() {
		QEventHandlerVoid q = new QEventHandlerVoid();
		QEventVoid eventV = new QEventVoid();
		
		/* Fail in super class */
		JSONObject json = q.checkParameters(0,eventV);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		assertEquals(QEventHandler.ERROR_GLOBALS_NULL,((JSONArray)json.get("errors")).get(0));

		GlobalsQualoscopy g = new GlobalsQualoscopy("TEST_VERSION");
		Globals.setGlobals(g);
		
		/* Fail due to typing */
		QEventCheckVersion eventCV = new QEventCheckVersion(null,null);
		
		json = q.checkParameters(0,eventCV);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		assertTrue(((String)((JSONArray)json.get("errors")).get(0)).contains(QEventHandler.ERROR_TYPE_MISMATCH));
		
		/* Ok */
		eventV = new QEventVoid();
		json = q.checkParameters(0,eventV);
		assertTrue(json == null);
	}
	
	@Test
	public void testOnEvent() {
		QEventHandlerVoid q = new QEventHandlerVoid();
		
		QEventVoid event = new QEventVoid();
		
		GlobalsQualoscopy g = new GlobalsQualoscopy("TEST_VERSION");
		Globals.setGlobals(g);
		
		JSONObject json = q.onEvent();
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		assertEquals(QEventHandler.ERROR_PARAMETERS_NOT_CHECKED,((JSONArray)json.get("errors")).get(0));
		
		/* Ok */
		event = new QEventVoid();
		json = q.checkParameters(0,event);
		assertTrue(json == null);
		
		json = q.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
	}

}
