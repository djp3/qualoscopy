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
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.djp3.qualoscopy.events.QEventType;
import net.djp3.qualoscopy.events.QEventWrapper;
import net.djp3.qualoscopy.events.QEventWrapperQueuer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class QEventWrapperQueuerTest {

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
	

	@Test
	public void testLogging() {
		
		List<QEventWrapper> events = new ArrayList<QEventWrapper>();
		
		String logFileName = "test/test_"+this.getClass().getCanonicalName();     
		
		GlobalsTerraTower globals = new GlobalsTerraTower("TEST_VERSION",true);
		Globals.setGlobals(globals);
		
		QEventWrapperQueuer eventPublisher = TerraTower.createEventQueue(logFileName);     
		globals.addQuittable(eventPublisher);
		
		String worldName = "earth";
		String worldPassword = "earthPassword";
		
		QEventCreateWorld ttEvent1 = new QEventCreateWorld(worldName,worldPassword);
		ResultChecker resultChecker = new ResultChecker(false);
		QEventWrapper event = new QEventWrapper(QEventType.CREATE_WORLD,ttEvent1,resultChecker);
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
		
		QEventCreateTerritory ttEvent2 = new QEventCreateTerritory(worldName,worldPassword,-180.0,180.0,10,-90.0,90.0,10);
		resultChecker = new ResultChecker(false);
		event = new QEventWrapper(QEventType.CREATE_TERRITORY,ttEvent2,resultChecker);
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
		
		String playerName = "Player Name";
		String playerPassword = "Player Password";
		QEventCreatePlayer ttEvent3 = new QEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		resultChecker = new ResultChecker(false);
		event = new QEventWrapper(QEventType.CREATE_PLAYER,ttEvent3,resultChecker);
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
		
		double lat = 0.0d;
		double lng = 0.0d;
		double alt = 0.0d;
		QEventBuildTower ttEvent4 = new QEventBuildTower(worldName,worldPassword,playerName,playerPassword,lat,lng,alt);
		resultChecker = new ResultChecker(false);
		event = new QEventWrapper(QEventType.BUILD_TOWER,ttEvent4,resultChecker);
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
		
		QEventStepTowerTerritoryGrowth ttEvent5 = new QEventStepTowerTerritoryGrowth(worldName,worldPassword);
		resultChecker = new ResultChecker(false);
		event = new QEventWrapper(QEventType.STEP_TOWER_TERRITORY_GROWTH,ttEvent5,resultChecker);
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
		
		QEventDropBomb ttEvent6 = new QEventDropBomb(worldName,worldPassword,playerName,playerPassword,lat,lng,alt);
		resultChecker = new ResultChecker(false);
		event = new QEventWrapper(QEventType.DROP_BOMB,ttEvent6,resultChecker);
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
		
		QEventBurnBombFuse ttEvent7 = new QEventBurnBombFuse(worldName,worldPassword);
		resultChecker = new ResultChecker(false);
		event = new QEventWrapper(QEventType.BURN_BOMB_FUSE,ttEvent7,resultChecker);
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
		
		

		QEventCreatePowerUp ttEvent8 = new QEventCreatePowerUp(worldName,worldPassword,"code",-1000L,-1000L,-1000L);
		resultChecker = new ResultChecker(false);
		event = new QEventWrapper(QEventType.CREATE_POWER_UP,ttEvent8,resultChecker);
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
		
		
		QEventRedeemPowerUp ttEvent9 = new QEventRedeemPowerUp(worldName,worldPassword,playerName,playerPassword,"code");
		resultChecker = new ResultChecker(false);
		event = new QEventWrapper(QEventType.REDEEM_POWER_UP,ttEvent9,resultChecker);
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
		
		Globals.getGlobals().setQuitting(true);
		
		for(QEventWrapper loopEvent: events){
			//System.err.println("loopEvent "+loopEvent.toJSON().toJSONString());
			BufferedReader reader = null;
			boolean foundTheLine = false;
			try{
				Path newFile = Paths.get(logFileName);
				reader = Files.newBufferedReader(newFile, Charset.defaultCharset());
				String lineFromFile = "";
				while((lineFromFile = reader.readLine()) != null){
					//System.err.println(lineFromFile);
					if(lineFromFile.contains(loopEvent.toJSON().toJSONString())){
						foundTheLine = true;
					}
				}
				try{
					assertTrue(foundTheLine);
				}
				catch(AssertionError e){
					System.err.println("Couldn't find: "+loopEvent.toJSON().toJSONString());
					throw e;
				}
				reader.close();
			}catch(IOException e){
				fail(e.toString());
			}
			finally{
				if(reader!= null){
					try {
						reader.close();
					} catch (IOException e) {
					}
				}
			}
		}
		
		
	}

}
