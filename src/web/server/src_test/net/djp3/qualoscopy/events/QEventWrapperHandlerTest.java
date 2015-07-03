package net.djp3.qualoscopy.events;

import net.djp3.qualoscopy.EventHandlerResultChecker;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class QEventWrapperHandlerTest {
	
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
		QEventWrapperHandler qewh = new QEventWrapperHandler();
		
		EventHandlerResultChecker rc = new EventHandlerResultChecker();
		
		QEventWrapper qew = new QEventWrapper(0L,QEventType.VOID,new QEventVoid(),rc);
		qewh.onEvent(qew, 1, true);
	}

}
