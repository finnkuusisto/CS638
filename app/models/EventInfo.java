package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;


import com.avaje.ebean.*;

@Entity
public class EventInfo extends Model {

	@Id
	public String id;
	
	public String creatorUsername;
	public String name;
	public String description;
	public double distance;
	
	public EventInfo(String creatorUsername, String name, String description, double distance){
		this.id = java.util.UUID.randomUUID().toString();
		this.creatorUsername = creatorUsername;
		this.name = name;
		this.description = description;
		this.distance = distance;
		
	}
	
	///////////
    //Queries//
    ///////////
	
    public static Model.Finder<String,EventInfo> find =
		new Model.Finder(String.class, EventInfo.class);

    /**
     * Retrieve all users.
     */
    public static List<EventInfo> findAll() {
    	return find.all();
    }

    public static void create(String creatorUsername, String name,
    		String description, double distance) {
    	EventInfo group = new EventInfo(creatorUsername, name, description,distance);
    	group.save();
    }

    public static List<EventInfo> findByID(String id) {
    	return find.where().eq("id", id).findList();
    }
    
    public static List<EventInfo> findByCreator(String username) {
    	return find.where().eq("creatorUsername", username).findList();
    }
	
	
}
