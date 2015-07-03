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
import net.djp3.qualoscopy.events.QEventInitiateSession;
import net.djp3.qualoscopy.events.QEventVoid;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class QEventHandlerInitiateSessionTest {
	
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
	
	static final String goodVersion = "0.1";
	static final String badVersion = "0.2";
	
	@Test
	public void testParameters() {
		
		QEventHandler q = new QEventHandlerInitiateSession();
		QEventInitiateSession eventIS = new QEventInitiateSession(goodVersion,goodVersion);
		
		/* Fail in the super class */
		JSONObject json = q.checkParameters(0,eventIS);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		assertEquals(QEventHandler.ERROR_GLOBALS_NULL,((JSONArray)json.get("errors")).get(0));
		
		GlobalsQualoscopy g = new GlobalsQualoscopy("TEST_VERSION");
		Globals.setGlobals(g);
		
		/* Fail for the wrong type */
		QEventVoid eventV = new QEventVoid();

		json = q.checkParameters(0,eventV);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		assertTrue(((String)((JSONArray)json.get("errors")).get(0)).contains(QEventHandler.ERROR_TYPE_MISMATCH));
		
		/* Ok! */
		eventIS = new QEventInitiateSession(goodVersion,goodVersion);
		json = q.checkParameters(0,eventIS);
		assertTrue(json == null);

	}
	
	@Test
	public void testOnEvent() {
		QEventHandlerInitiateSession q = new QEventHandlerInitiateSession();
		QEventInitiateSession event = new QEventInitiateSession(goodVersion,badVersion);
		
		GlobalsQualoscopy g = new GlobalsQualoscopy("TEST_VERSION");
		Globals.setGlobals(g);
		
		/* Fail due to not checking parameters */
		JSONObject json = q.onEvent();
		assertEquals("true",(String)json.get("error"));
		assertTrue(((String)((JSONArray)json.get("errors")).get(0)).contains(QEventHandlerCheckVersion.ERROR_PARAMETERS_NOT_CHECKED));
		
		/* Version mismatch! */
		json = q.checkParameters(0,event);
		assertTrue(json == null);
		json = q.onEvent();
		assertEquals("true",(String)json.get("error"));
		assertTrue(((String)((JSONArray)json.get("errors")).get(0)).contains(QEventHandlerCheckVersion.ERROR_API_VERSION_MISMATCH));
		
		
		/* Ok! */
		event = new QEventInitiateSession(goodVersion,goodVersion);
		json = q.checkParameters(0,event);
		assertTrue(json == null);
		
		json = q.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
	}
}
