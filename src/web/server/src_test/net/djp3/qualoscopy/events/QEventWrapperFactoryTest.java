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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.djp3.qualoscopy.events.QEventType;
import net.djp3.qualoscopy.events.QEventWrapper;
import net.djp3.qualoscopy.events.QEventWrapperFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class QEventWrapperFactoryTest {

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
	public void test() {
		QEventWrapperFactory x = new QEventWrapperFactory();
		QEventWrapper a = x.newInstance();
		QEventWrapper b = x.newInstance();
		assertEquals(a.getEventType(),x.defaultEventType);
		assertEquals(b.getEventType(),x.defaultEventType);
		assertEquals(a,b);
		
		QEventWrapperFactory y = new QEventWrapperFactory(QEventType.VOID);
		QEventWrapper c = y.newInstance();
		QEventWrapper d = y.newInstance();
		assertEquals(c.getEventType(),y.defaultEventType);
		assertEquals(d.getEventType(),y.defaultEventType);
		assertEquals(a,b);
		
		assertTrue(a!=c);
		assertTrue(b!=d);
	}

}
