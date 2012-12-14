package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;


import com.avaje.ebean.*;

import extra.Unit;


@Entity
public class EventInfo extends Model {

	
    @Id
   public String id;

    public long createDate;
	public String creatorUsername;
	public String name;
	public String description;
	public double distance;
	public Unit unit;
	public String pace;
	public String routeDescription;
	
	public EventInfo(String creatorUsername, String name, String description,
			double distance, String unit, String routeDescription, String pace,
			long createDate){
		this.creatorUsername = creatorUsername;
		this.name = name;
		this.description = description;
		this.distance = distance;
		this.routeDescription = routeDescription;
		this.pace = pace;
		if(unit.equals("Miles")){
			this.unit = Unit.miles;
		} else if(unit.equals("Meters")){
			this.unit = Unit.meters;
		} else if(unit.equals("Kilometers")) {
			this.unit = Unit.kilometers;		
		}
		this.createDate = createDate;
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
    
    public static List<EventInfo> findAllOrderBy(String field) {
        return find.orderBy(field).findList();
    }

    public static EventInfo create(String creatorUsername, String name,
    		String description, double distance, String unit, String routeDescription, String pace) {
    	//Date objects are always UTC/GMT
    	Date now = new Date();
    	EventInfo group = new EventInfo(creatorUsername, name, description,
    			distance,unit, routeDescription, pace, now.getTime());
    	group.save();
    	return group;
    }
    
    public static EventInfo findByID(String id) {
    	return find.where().eq("id", id).findUnique();
    }
    
    public static List<EventInfo> findEvents(List<String> ids) {
    	return find.where().in("id", ids).findList();
    }
    
    public static List<EventInfo> findByCreator(String username) {
    	return find.where().eq("creatorUsername", username).findList();
    }


	public static List<EventInfo> getSuggestedEvents(String username) {
		List<EventInfo> events = find.all();
		//TODO do some magic like check the pace of attendees
		List<EventInfo> ret = new ArrayList<EventInfo>();
		for (int i = 0; i < 10 && i < events.size(); i++) {
			ret.add(events.get(i));
		}
		return ret;
	}
	
	public static List<EventInfo> getNewestEvents() {
		List<EventInfo> ordered = EventInfo.findAllOrderBy("createDate");
		//need the last 10 or so
    	List<EventInfo> ret = new ArrayList<EventInfo>();
    	for (int i = ordered.size() - 1; i >= Math.max(0, ordered.size() - 10); i--) {
    		ret.add(ordered.get(i));
    	}
    	return ret;
	}
	
}
