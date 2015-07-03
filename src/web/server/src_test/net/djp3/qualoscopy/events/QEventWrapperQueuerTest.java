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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.djp3.qualoscopy.GlobalsQualoscopy;
import net.djp3.qualoscopy.QualoscopyWebServer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class QEventWrapperQueuerTest {

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
	public void testLogging() {
		
		List<QEventWrapper> events = new ArrayList<QEventWrapper>();
		
		String logFileName = "test/test_"+this.getClass().getCanonicalName();     
		
		GlobalsQualoscopy globals = new GlobalsQualoscopy("TEST_VERSION",true);
		Globals.setGlobals(globals);
		
		QEventWrapperQueuer eventPublisher = QualoscopyWebServer.createEventQueue(logFileName);     
		globals.addQuittable(eventPublisher);
		
		String proposedVersion = Globals.getGlobals().getSystemVersion();
		
		QEventCheckVersion qEvent1 = new QEventCheckVersion(Globals.getGlobals().getSystemVersion(),proposedVersion);
		ResultChecker resultChecker = new ResultChecker(false);
		QEventWrapper event = new QEventWrapper(QEventType.CHECK_VERSION,qEvent1,resultChecker);
		events.add(event);
		eventPublisher.onData(event);
		synchronized(resultChecker.getSemaphore()){
			while(resultChecker.getResultOK() == null){
				try {
					resultChecker.getSemaphore().wait();
				} catch (InterruptedException e) {
				}
			}
		}
		try{
			assertTrue(resultChecker.getResultOK());
		}
		catch(AssertionError e){
			System.err.println(resultChecker.getResults().toJSONString());
			throw e;
		}
		
		QEventInitiateSession qEvent2 =  new QEventInitiateSession(Globals.getGlobals().getSystemVersion(),proposedVersion);
		resultChecker = new ResultChecker(false);
		event = new QEventWrapper(QEventType.INITIATE_SESSION,qEvent2,resultChecker);
		events.add(event);
		eventPublisher.onData(event);
		synchronized(resultChecker.getSemaphore()){
			while(resultChecker.getResultOK() == null){
				try {
					resultChecker.getSemaphore().wait();
				} catch (InterruptedException e) {
				}
			}
		}
		try{
			System.out.println(resultChecker.getResults().toJSONString());
			assertTrue(resultChecker.getResultOK());
		}
		catch(AssertionError e){
			System.err.println(resultChecker.getResults().toJSONString());
			throw e;
		}
		
		
	}

}
