package models;

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

/**
 * User entity managed by Ebean
 */
@Entity
public class UserInfo extends Model {

    @Id
    public String username;
    public String email;
    public String fullName;
    public String about;
    
    public String passHash;
    public String salt;
    
    public UserInfo(String fullName, String username, String email,
    		String password) {
    	this.fullName = fullName;
    	this.username = username;
    	this.email = email;
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
    
    public static void create(String fullName, String username, String email,
    		String password) {
    	UserInfo user = new UserInfo(fullName, username, email, password);
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
        return find.where().eq("username", username).findUnique();
    }
    
    public static List<UserInfo> findUsers(List<String> usernames) {
    	return find.where().in("username", usernames).findList();
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
    
    ////////////
    //Security//
    ////////////
    
    public static final String HASH_ALGORITHM = "SHA-256"; //guaranteed
    public static final int SALT_SIZE = 32;
    
    private static final MessageDigest MD = UserInfo.getMD();
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
    	return new String(salt);
    }
    
    private static String hashPassword(String password, String salt) {
    	String salted = salt + password;
    	return new String(UserInfo.MD.digest(salted.getBytes()));
    }
    
}

