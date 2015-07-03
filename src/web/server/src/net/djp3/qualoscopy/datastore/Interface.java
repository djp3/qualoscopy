package net.djp3.qualoscopy.datastore;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.webserver.handlers.login.Datastore;

public class Interface {
	
	final static Integer ITERATIONS = 200;


	private static transient volatile Logger log = null;


	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(Interface.class);
		}
		return log;
	}
	
	private static volatile Interface instance = null;
	
	public static Interface getInstance(){
		if(instance == null){
			getLog().info("Creating a default Datastore Interface");
			instance = new Interface(null);
		}
		return(instance);
	}
	
	static void createInterface(Datastore db){
		instance = new Interface(db);
	}
	
	/**
	 * This is supported just for testing
	 * @param db
	 * @param seed
	 */
	static void createInterface(Datastore db,long seed){
		instance = new Interface(db,seed);
	}
	
	Random r = new Random(System.currentTimeMillis());
	
	private Datastore db;
	
	private Interface(Datastore db){
		this(db,System.currentTimeMillis());
	}
	
	void setRandom(Random r){
		this.r = r;
	}
	
	/**
	 * This interface is provided just for testing purposes
	 * @param db
	 * @param seed
	 */
	private Interface(Datastore db,long seed){
		this.db = db;
		this.setRandom(new Random(seed));
	}
	
	
	public String createSessionID(){
		try {
			return SHA256.sha256(r.nextLong()+"",ITERATIONS);
		} catch (IllegalArgumentException e) {
			getLog().fatal(e.toString());
		} catch (NoSuchAlgorithmException e) {
			getLog().fatal(e.toString());
		}
		return null;
	}
	
	public synchronized String createSalt(){
		try {
			return SHA256.sha256(r.nextLong()+"",ITERATIONS);
		} catch (IllegalArgumentException e) {
			getLog().fatal(e.toString());
		} catch (NoSuchAlgorithmException e) {
			getLog().fatal(e.toString());
		}
		return null;
	}
}
