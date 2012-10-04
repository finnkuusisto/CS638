package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

/**
 * User entity managed by Ebean
 */
@Entity
public class UserGroup extends Model {
	
	@Id
	public String id;
	public String creatorUsername;
	public String name;
	public String description;
	
	public UserGroup(String creatorUsername, String name, String description) {
		this.id = java.util.UUID.randomUUID().toString();
		this.creatorUsername = creatorUsername;
		this.name = name;
		this.description = description;
	}
	
    ///////////
    //Queries//
    ///////////
    
    public static Model.Finder<String,UserGroup> find =
    		new Model.Finder(String.class, UserGroup.class);
    
    /**
     * Retrieve all users.
     */
    public static List<UserGroup> findAll() {
        return find.all();
    }
    
    public static void create(String creatorUsername, String name,
    		String description) {
    	UserGroup group = new UserGroup(creatorUsername, name, description);
    	group.save();
    }
    
    public static List<UserGroup> findByCreator(String username) {
    	return find.where().eq("creatorUsername", username).findList();
    }

}
