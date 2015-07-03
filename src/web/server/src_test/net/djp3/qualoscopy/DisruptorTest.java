/*
	Copyright 2014-2015
		University of California, Irvine (c/o Donald J. Patterson)
*/
/*
	This file is part of the Laboratory for Ubiquitous Computing java TerraTower game, i.e. "TerraTower"

    TerraTower is free software: you can redistribute it and/or modify
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
package net.djp3.qualoscopy;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.djp3.qualoscopy.events.QEventType;
import net.djp3.qualoscopy.events.QEventVoid;
import net.djp3.qualoscopy.events.QEventWrapper;
import net.djp3.qualoscopy.events.QEventWrapperFactory;
import net.djp3.qualoscopy.events.QEventWrapperHandler;
import net.djp3.qualoscopy.events.QEventWrapperQueuer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import edu.uci.ics.luci.utility.Globals;

public class DisruptorTest
{
	
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
    public void testDisruptor() 
    {
        // Executor that will be used to construct new threads for consumers
        Executor executor = Executors.newCachedThreadPool();

        // The factory for the event
        QEventWrapperFactory factory = new QEventWrapperFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<QEventWrapper> disruptor = new Disruptor<QEventWrapper>(factory, bufferSize, executor);

        // Connect the handler
        disruptor.handleEventsWith(new QEventWrapperHandler());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<QEventWrapper> ringBuffer = disruptor.getRingBuffer();

        QEventWrapperQueuer producer = new QEventWrapperQueuer(disruptor,ringBuffer);

        QEventWrapper event = new QEventWrapper(QEventType.VOID,new QEventVoid(),(QEventHandlerResultListener)null);
        for (int i = 0 ; i< 25000;i++)
        {
            producer.onData(event);
        }
    }
}
