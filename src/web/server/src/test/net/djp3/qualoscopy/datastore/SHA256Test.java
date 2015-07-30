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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class SHA256Test {
	
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
		String x = System.currentTimeMillis() + "";

		try {
			assertEquals(SHA256.sha256(x, 0), SHA256.sha256(x, 0));
			fail("This should throw an exception");
		} catch (IllegalArgumentException e) {
			assertTrue(e.toString().contains(SHA256.ITERATIONS_MUST_BE_1));

		}

		for (int i = 1; i < 100; i++) {
			assertEquals(SHA256.sha256(x, i), SHA256.sha256(x, i));
		}

	}

	@Test
	public void test2() {
		String test = "Luke Raus";
		System.out.println(test + ":" + SHA256.sha256(test, 1));

		test = "Hello World 123456";
		System.out.println(test + ":" + SHA256.sha256(test, 1));

		test = "éñƔ";
		System.out.println(test + ":" + SHA256.sha256(test, 1));

	}
	
	@Test
	public void test3() {
		
		assertTrue(SHA256.makeSomethingUp()!=SHA256.makeSomethingUp());

	}

}
