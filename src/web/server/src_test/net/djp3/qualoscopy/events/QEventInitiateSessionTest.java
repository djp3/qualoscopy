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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class QEventInitiateSessionTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public void testNormal() {
		QEvent s = new QEvent();
		QEventInitiateSession thing1 = new QEventInitiateSession(goodVersion,goodVersion);
		QEventInitiateSession thing2 = new QEventInitiateSession(goodVersion,goodVersion);
		
		assertTrue(!thing1.equals(null));
		assertTrue(!thing1.equals(s));
		assertTrue(!thing1.equals(new QEventCheckVersion(goodVersion,goodVersion)));//class != superclass
		assertTrue(thing1.equals(thing1));
		assertTrue(thing1.equals(thing2));
		assertTrue(thing2.equals(thing2));
		
		assertEquals(thing1.hashCode(),thing1.hashCode());
		assertEquals(thing1.hashCode(),thing2.hashCode());
		
		assertEquals(thing1,QEventInitiateSession.fromJSON(thing1.toJSON()));
		assertEquals(thing1,QEventInitiateSession.fromJSON(thing2.toJSON()));
		
		assertEquals(thing1.hashCode(),thing1.hashCode());
		assertEquals(thing1.hashCode(),thing2.hashCode());
		
	}

}
