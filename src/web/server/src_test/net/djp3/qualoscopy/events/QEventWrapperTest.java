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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import net.djp3.qualoscopy.QEventHandlerResultListener;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;



public class QEventWrapperTest {

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

	private class MyQEventHandlerResultListener implements QEventHandlerResultListener{

		@Override
		public void onFinish(JSONObject result) {
		}
		
	}

	@Test
	public void testBasics() {
		
		QEventWrapper q1;
		try{
			q1 = new QEventWrapper(null,new QEventVoid(),(QEventHandlerResultListener) null);
			fail("Should throw an exception");
		}catch(IllegalArgumentException e){
			//ok
		}catch(RuntimeException e){
			fail("Should not throw an exception"+e);
		}
		
		try{
			q1 = new QEventWrapper(null,new QEventVoid(),(List<QEventHandlerResultListener>) null);
			fail("Should throw an exception");
		}catch(IllegalArgumentException e){
			//ok
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
		
		try{
			q1 = new QEventWrapper(QEventType.VOID,null,(QEventHandlerResultListener) null);
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
		
		try{
			q1 = new QEventWrapper(QEventType.VOID,null,(List<QEventHandlerResultListener>) null);
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
		
		QEvent qEvent1 = new QEvent();
		QEventHandlerResultListener rl = new MyQEventHandlerResultListener();
		List<QEventHandlerResultListener> list = new ArrayList<QEventHandlerResultListener>();
		list.add(rl);
		try{
			q1 = new QEventWrapper(QEventType.CHECK_VERSION,qEvent1,rl);
			q1 = new QEventWrapper(QEventType.VOID,qEvent1,list);
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
	}

	@Test
	public void testResetEventHandler() {
		for(QEventType tet : QEventType.values()){
			QEventWrapper q1 = new QEventWrapper(tet,new QEventVoid(),new ResultChecker(false));
			QEventWrapper q2 = new QEventWrapper(tet,new QEventVoid(),new ResultChecker(false));
			q1.resetEvent();
			q1.resetEventHandler();
			try{
				assertTrue(q1.getHandler() != null);
				q2.set(q1);
				assertEquals(q1,q2);
				assertEquals(q1.hashCode(),q2.hashCode());
				assertEquals(q1,q1.fromJSON(q1.toJSON()));
			}
			catch(AssertionError e){
				System.err.println("Failed trying to handle: "+tet);
				throw e;
			}
		}
	}
	
	@Test
	public void testEquals() {
		
		/* Check basics */
		long now = System.currentTimeMillis();
		QEventWrapper q1 = new QEventWrapper(now,QEventType.VOID,new QEventVoid(),new ResultChecker(false));
		QEventWrapper q2 = new QEventWrapper(now,QEventType.VOID,new QEventVoid(),new ResultChecker(false));
		assertEquals(q1,q1);
		assertTrue(q1.hashCode() == q1.hashCode());
		assertEquals(q1.hashCode(),q2.hashCode());
		assertEquals(q1,q2);
		assertTrue(!q1.equals(null));
		assertTrue(!q1.equals("foo"));
		
		/*Check after using set*/
		q2 = new QEventWrapper(now, QEventType.CHECK_VERSION,new QEventCheckVersion("0.1","0.2"),new ResultChecker(false));
		q1.set(q2);
		assertEquals(q1,q1);
		assertTrue(q1.equals(q2));
		
		/*Check after using set with mismatched Version*/
		q2 = new QEventWrapper(now, QEventType.CHECK_VERSION,new QEvent(),new ResultChecker(false));
		try{
			q1.set(q2);
			fail("There should be an exception thrown because object and version don't match");
		}
		catch(IllegalArgumentException e){
			/*This should throw an exception */
		}
		
		/* Check when object is null */
		q2 = new QEventWrapper(QEventType.CHECK_VERSION,null,new ResultChecker(false));
		assertEquals(q2,q2);
		assertTrue(q1.hashCode() != q2.hashCode());
		assertTrue(!q1.equals(q2));
		assertTrue(!q2.equals(q1));
		
		/* Check when eventType is different */
		q2 = new QEventWrapper(QEventType.VOID,new QEventVoid(),new ResultChecker(false));
		assertEquals(q2,q2);
		assertTrue(q1.hashCode() != q2.hashCode());
		assertTrue(!q1.equals(q2));
		assertTrue(!q2.equals(q1));
		
		/* Check when only time is different*/
		q2 = new QEventWrapper(now-1,QEventType.CHECK_VERSION,new QEvent(),new ResultChecker(false));
		assertEquals(q2,q2);
		assertTrue(q1.hashCode() != q2.hashCode());
		assertTrue(!q1.equals(q2));
		assertTrue(!q2.equals(q1));
		
	}
	

}
