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


package net.djp3.qualoscopy.datastore;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class InterfaceTest {
	
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
		
		/* Check to make sure that Interface makes a default instance */
		assertTrue(Interface.getInstance() != null);
		String uniqueSessionID_1 = Interface.getInstance().createSessionID();
		String uniqueSessionSalt_1 = Interface.getInstance().createSalt();
		
		Interface.createInterface(null);
		String uniqueSessionID_2 = Interface.getInstance().createSessionID();
		String uniqueSessionSalt_2 = Interface.getInstance().createSalt();
		
		/* Check to make sure that Interface's are consistent */
		long seed = System.currentTimeMillis();
		Interface.createInterface(null, seed);
		Interface interface1 = Interface.getInstance();
		
		Interface.createInterface(null, seed);
		Interface interface2 = Interface.getInstance();
		
		assertTrue(interface1 != interface2);
		
		String sessionID_1 = interface1.createSessionID();
		String sessionID_2 = interface2.createSessionID();
		String sessionSalt_1 = interface1.createSalt();
		String sessionSalt_2 = interface2.createSalt();
		
		assertTrue(sessionID_1.equals(sessionID_2));
		assertTrue(!sessionID_1.equals(uniqueSessionID_1));
		assertTrue(!sessionID_1.equals(uniqueSessionID_2));
		assertTrue(!sessionID_2.equals(uniqueSessionID_1));
		assertTrue(!sessionID_2.equals(uniqueSessionID_2));
		assertTrue(!uniqueSessionID_1.equals(uniqueSessionID_2));
		
		assertTrue(sessionSalt_1.equals(sessionSalt_2));
		assertTrue(!sessionSalt_1.equals(uniqueSessionSalt_1));
		assertTrue(!sessionSalt_1.equals(uniqueSessionSalt_2));
		assertTrue(!sessionSalt_2.equals(uniqueSessionSalt_1));
		assertTrue(!sessionSalt_2.equals(uniqueSessionSalt_2));
		assertTrue(!uniqueSessionSalt_1.equals(uniqueSessionSalt_2));
		
	}

}
