package net.djp3.qualoscopy.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import net.djp3.qualoscopy.QEventHandlerResultListener;
import net.djp3.qualoscopy.QEventVoid;
import net.djp3.qualoscopy.events.QEvent;
import net.djp3.qualoscopy.events.QEventType;
import net.djp3.qualoscopy.events.QEventWrapper;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



public class QEventWrapperTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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
		
		QEventWrapper t1;
		try{
			//t1 = new QEventWrapper(QEventType.VOID,new QEventVoid(),(QEventHandlerResultListener) null);
			t1 = new QEventWrapper(null,new QEventVoid(),(QEventHandlerResultListener) null);
			fail("Should throw an exception");
		}catch(IllegalArgumentException e){
			//ok
		}catch(RuntimeException e){
			fail("Should not throw an exception"+e);
		}
		
		try{
			//t1 = new QEventWrapper(QEventType.VOID,new QEventVoid(),(QEventHandlerResultListener) null);
			t1 = new QEventWrapper(null,new QEventVoid(),(List<QEventHandlerResultListener>) null);
			fail("Should throw an exception");
		}catch(IllegalArgumentException e){
			//ok
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
		
		try{
			t1 = new QEventWrapper(QEventType.VOID,null,(QEventHandlerResultListener) null);
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
		
		try{
			t1 = new QEventWrapper(QEventType.VOID,null,(List<QEventHandlerResultListener>) null);
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
		
		QEvent ttEvent1 = new QEvent("name","password");
		QEventHandlerResultListener rl = new MyQEventHandlerResultListener();
		List<QEventHandlerResultListener> list = new ArrayList<QEventHandlerResultListener>();
		list.add(rl);
		try{
			t1 = new QEventWrapper(QEventType.CREATE_WORLD,ttEvent1,rl);
			t1 = new QEventWrapper(QEventType.CREATE_TERRITORY,ttEvent1,list);
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
	}

	@Test
	public void testResetEventHandler() {
		for(QEventType tet : QEventType.values()){
			QEventWrapper t1 = new QEventWrapper(tet,new QEventVoid(),new ResultChecker(false));
			QEventWrapper t2 = new QEventWrapper(tet,new QEventVoid(),new ResultChecker(false));
			t1.resetEvent();
			t1.resetEventHandler();
			try{
				assertTrue(t1.getHandler() != null);
				t2.set(t1);
				assertEquals(t1,t2);
				assertEquals(t1.hashCode(),t2.hashCode());
				assertEquals(t1,t1.fromJSON(t1.toJSON()));
			}
			catch(AssertionError e){
				System.err.println("Failed trying to handle: "+tet);
				throw e;
			}
		}
	}
	
	@Test
	public void testEquals() {
		QEventWrapper t1 = new QEventWrapper(QEventType.VOID,new QEventVoid(),new ResultChecker(false));
		QEventWrapper t2 = new QEventWrapper(QEventType.VOID,new QEventVoid(),new ResultChecker(false));
		assertEquals(t1,t1);
		assertTrue(t1.hashCode() == t2.hashCode());
		assertEquals(t1,t2);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("foo"));
		
		
		t2 = new QEventWrapper(QEventType.CREATE_WORLD,new QEvent("a","b"),new ResultChecker(false));
		t1.set(t2);
		assertEquals(t1,t1);
		assertTrue(t1.equals(t2));
		
		t2 = new QEventWrapper(QEventType.CREATE_WORLD,null,new ResultChecker(false));
		assertEquals(t2,t2);
		assertTrue(t1.hashCode() != t2.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		
		t2 = new QEventWrapper(QEventType.CREATE_WORLD,new QEvent("c","d"),new ResultChecker(false));
		assertEquals(t2,t2);
		assertTrue(t1.hashCode() != t2.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		
	}
	

}
