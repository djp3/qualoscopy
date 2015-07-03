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
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class ResultCheckerTest {
	
	private static JSONObject goodResult;
	private static JSONObject badResult;
	private static JSONObject nullResult;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		goodResult = new JSONObject();
		goodResult.put("error","false");
		
		badResult = new JSONObject();
		badResult.put("error","true");
		JSONArray errors = new JSONArray();
		errors.add("Dummy error");
		badResult.put("errors", errors);
		
		nullResult = new JSONObject();
		
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
		ResultChecker resultChecker = new ResultChecker(false);
		resultChecker.onFinish(goodResult);
		assertTrue(resultChecker.getResultOK());
		assertEquals(resultChecker.getResults(),goodResult);
		
		resultChecker = new ResultChecker(true);
		resultChecker.onFinish(null);
		assertTrue(!resultChecker.getResultOK());
		assertTrue(resultChecker.getResults() == null);
		
		resultChecker = new ResultChecker(false);
		resultChecker.onFinish(badResult);
		assertTrue(!resultChecker.getResultOK());
		assertEquals(resultChecker.getResults(),badResult);
		
		resultChecker = new ResultChecker(false);
		synchronized(resultChecker.getSemaphore()){
			resultChecker.onFinish(nullResult);
			assertTrue(!resultChecker.getResultOK());
			assertEquals(resultChecker.getResults(),nullResult);
		}
	}

}
