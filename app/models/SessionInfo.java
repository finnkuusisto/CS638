package models;

import java.security.SecureRandom;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.mvc.Http.Session;

@Entity
public class SessionInfo extends Model {
	
	private static final SecureRandom SECURE_RAND = new SecureRandom();

	@Id
	public String username;
	
	public String key;
	public long createTime;
	
	public SessionInfo(String username) {
		this.username = username;
		Date now = new Date();
		this.createTime = now.getTime();
		this.key = Long.toHexString(SECURE_RAND.nextLong());
	}
	
	////////////
	//Security//
	////////////
	
	public static UserInfo getLoggedInUser(Session session) {
		//need to check both the username and key
		String username = session.get("username");
		String key = session.get("key");
		//we need both
		if (username == null || key == null) {
			return null;
		}
		//next check if the key is correct
		if (SessionInfo.keyMatch(username, key)) {
			return UserInfo.findByUsername(username);
		}
		return null;
	}
	
	private static boolean keyMatch(String username, String key) {
		SessionInfo info = SessionInfo.findByUsername(username);
		if (info == null) {
			return false;
		}
		return info.key.equals(key);
	}
	
	///////////
    //Queries//
    ///////////
	
    public static Model.Finder<String,SessionInfo> find =
		new Model.Finder(String.class, SessionInfo.class);
    
    public static SessionInfo findByUsername(String username) {
    	if (username == null) {
    		return null;
    	}
    	return find.where().eq("username", username).findUnique();
    }
    
    public static String create(String username) {
    	if (username == null) {
    		return null;
    	}
    	//make sure it's unique
    	SessionInfo.deleteSessionInfo(username);
    	SessionInfo info = new SessionInfo(username);
    	info.save();
    	return info.key;
    }
    
    public static void deleteSessionInfo(String username) {
    	SessionInfo info = SessionInfo.findByUsername(username);
    	if (info != null) {
    		info.delete();
    	}
    }
	
}
