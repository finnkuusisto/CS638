package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;


import com.avaje.ebean.*;

import extra.PaceUtil;
import extra.Unit;


@Entity
public class EventInfo extends Model {

	
    @Id
   public String id;

    public long createDate;
	public String creatorUsername;
	public String name;
	public String description;
	public double km;
	public Unit displayUnit;
	public String pace;
	public String routeDescription;
	
	public EventInfo(String creatorUsername, String name, String description,
			double distance, String unit, String routeDescription, String pace,
			long createDate){
		this.creatorUsername = creatorUsername;
		this.name = name;
		this.description = description;
		this.routeDescription = routeDescription;
		this.pace = pace;
		if(unit.equals("mi.")){
			this.displayUnit = Unit.miles;
			this.km = PaceUtil.mileToKm(distance);
		} else if(unit.equals("m")){
			this.displayUnit = Unit.meters;
			this.km = distance / 1000;
		} else if(unit.equals("km")) {
			this.displayUnit = Unit.kilometers;	
			this.km = distance;
		}
		this.createDate = createDate;
	}
	
	public String getDistanceString() {
		String ret = null;
		switch (this.displayUnit) {
			case miles:
				ret = PaceUtil.kmToMile(this.km) + " mi.";
			case meters:
				ret = (this.km / 1000) + " m";
			case kilometers:
				ret = this.km + " km";
		}
		return ret;
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
    			distance, unit, routeDescription, pace, now.getTime());
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
	
	public static List<EventInfo> getFolloweesEvents(String follower) {
		List<String> users = Follow.findUsersFollowedBy(follower);
		List<String> eventIDs =
				new ArrayList<String>(Attend.findEventsAttendedBy(users));
		List<EventInfo> events = EventInfo.findEvents(eventIDs);
		//reduce size
		while (events.size() > 10) {
			events.remove(events.size() - 1);
		}
		return events;
	}
	
}
