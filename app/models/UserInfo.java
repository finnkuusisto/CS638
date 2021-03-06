package models;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import javax.persistence.*;

import play.Logger;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import extra.PaceUtil;
import extra.SortContainer;

/**
 * User entity managed by Ebean
 */
@Entity
public class UserInfo extends Model {

    @Id
    public String username;
    public String email;
    public String zipCode;
    public boolean publicEmail;
    public String fullName;
    @Column(columnDefinition="TEXT") //yeah, this assumes such a type exists
    public String about;
    public long joinDate;
    public String url;
    
    //for suggestions
    public long predicted5k;
    
    //for basic statistics
    public long lastLogin;
    public long loginCount;
    
    public String passHash;
    public String salt;
    
    public UserInfo(String fullName, String username, String email,
    		String zipCode, long joinDate, String password) {
    	this.fullName = fullName;
    	this.username = username;
    	this.email = email;
    	this.zipCode = ZipCodeInfo.getValidatedZipCode(zipCode);
    	this.joinDate = joinDate;
    	this.url = "";
    	//we'll start predicted 5k at 27 minutes
    	this.predicted5k = PaceUtil.timeToSec(27, 0);
    	this.salt = UserInfo.newSalt();
    	this.passHash = UserInfo.hashPassword(password, this.salt);
    }
    
    private boolean authenticate(String password) {
    	String hashed = UserInfo.hashPassword(password, this.salt);
    	return hashed.equals(this.passHash);
    }
    
    public String toString() {
        return "User(" + email + ")";
    }

    ///////////
    //Queries//
    ///////////
    
    public static Model.Finder<String,UserInfo> find =
    		new Model.Finder(String.class, UserInfo.class);
    
    /**
     * Retrieve all users.
     */
    public static List<UserInfo> findAll() {
        return find.all();
    }
    
    public static List<UserInfo> findAllOrderBy(String field) {
        return find.orderBy(field).findList();
    }
    
    public static void create(String fullName, String username, String email,
    		String zipCode, String password) {
    	//Date objects are always UTC/GMT
    	Date now = new Date();
    	UserInfo user = new UserInfo(fullName, username, email, zipCode,
    			now.getTime(), password);
    	user.save();
    }

    public static boolean usernameAvailable(String username) {
    	return UserInfo.findByUsername(username) == null;
    }
    
    public static boolean emailAvailable(String email) {
    	return UserInfo.findByEmail(email) == null;
    }
    
    /**
     * Retrieve a User from email.
     */
    public static UserInfo findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }
    
    /**
     * Retrieve a User from username.
     */
    public static UserInfo findByUsername(String username) {
    	if (username == null) {
    		return null;
    	}
        return find.where().eq("username", username).findUnique();
    }
    
    public static List<UserInfo> findUsers(List<String> usernames) {
    	return find.where().in("username", usernames).findList();
    }
    
    public static void updateInfoForLoginSuccess(String username) {
    	UserInfo info = UserInfo.findByUsername(username);
    	if (info != null) {
    		info.lastLogin = (new Date()).getTime();
    		info.loginCount++;
    		info.save();
    	}
    }
    
    /**
     * Authenticate a User.
     */
    public static boolean authenticate(String username, String password) {
    	UserInfo user = UserInfo.findByUsername(username);
    	//check if user exists
        if (user == null) {
        	return false;
        }
        //check password
    	return user.authenticate(password);
    }
    
    //////////////////////
    //Suggestion Helpers//
    //////////////////////
    
    public static List<UserInfo> getSuggestedUsers(String username) {
    	//first find the user we're suggesting for
    	List<UserInfo> ret = new ArrayList<UserInfo>(10);
    	UserInfo thisUser = UserInfo.findByUsername(username);
    	if (thisUser == null) {
    		return ret;
    	}
    	//just compare predicted 5k times of everyone else
    	List<UserInfo> all = UserInfo.findAll();  //TODO limit to nearby users
    	Queue<SortContainer<UserInfo>> sorted =
    			new PriorityQueue<SortContainer<UserInfo>>();
    	for (UserInfo user : all) {
    		//skip the user we're suggesting for
    		if (!user.username.equals(thisUser.username)) {
    			double sortVal =
    					Math.abs(thisUser.predicted5k - user.predicted5k);
    			sorted.offer(new SortContainer<UserInfo>(sortVal, user));
    		}
    	}
    	//now only return the 10 closest other users
    	for (int i = 0; !sorted.isEmpty() && i < 10; i++) {
    		ret.add(sorted.remove().obj);
    	}
    	return ret;
    }
    
    public static List<UserInfo> getNewestUsers() {
    	List<UserInfo> ordered = UserInfo.findAllOrderBy("joinDate");
    	//need the last 10 or so
    	List<UserInfo> ret = new ArrayList<UserInfo>();
    	for (int i = ordered.size() - 1; i >= Math.max(0, ordered.size() - 10); i--) {
    		ret.add(ordered.get(i));
    	}
    	return ret;
    }
    
    ////////////
    //Security//
    ////////////
    
    public static final String HASH_ALGORITHM = "SHA-256"; //guaranteed
    public static final int SALT_SIZE = 32;
    
    private static MessageDigest getMD() {
    	MessageDigest md = null;
    	try {
			md = MessageDigest.getInstance(HASH_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			//only use guaranteed provided algorithms
			e.printStackTrace();
		}
    	return md;
    }
    private static final SecureRandom SECURE_RAND = new SecureRandom();

    
    private static String newSalt() {
    	byte[] salt = new byte[UserInfo.SALT_SIZE];
    	UserInfo.SECURE_RAND.nextBytes(salt);
    	return UserInfo.toHexString(salt);
    }
    
    private static String hashPassword(String password, String salt) {
    	String salted = salt + password;
    	return UserInfo.toHexString(UserInfo.getMD().digest(salted.getBytes()));
    }
    
	private static String toHexString(byte[] buf) {
		BigInteger bi = new BigInteger(1, buf);
		return String.format("%0" + (buf.length << 1) + "X", bi);
	}
    
}

