package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Attend extends Model {

	@Id
	public String id;
	
	//username of the user attending
	public String username;
	//id of the event being attended
	public String eventID;
	
	public Attend() {}
	
	public Attend(String username, String eventID) {
		this.username = username;
		this.eventID = eventID;
	}
	
	///////////
	//Queries//
	///////////
	
	public static Model.Finder<String,Attend> find =
			new Model.Finder(String.class, Attend.class);
	
	/**
	 * Get the usernames of all users attending an event.
	 * @param eventID
	 * @return
	 */
	public static List<String> findUsersAttending(String eventID) {
		List<Attend> attenders = Attend.findByEvent(eventID);
		List<String> ret = new ArrayList<String>(attenders.size());
		for (Attend a : attenders) {
			ret.add(a.username);
		}
		return ret;
	}
	
	/**
	 * Get the event IDs of all events attended by a particular user.
	 * @param username
	 * @return
	 */
	public static List<String> findEventsAttendedBy(String username) {
		List<Attend> events = Attend.findByUser(username);
		List<String> ret = new ArrayList<String>(events.size());
		for (Attend a : events) {
			ret.add(a.eventID);
		}
		return ret;
	}
	
	/**
	 * Get all of the Attend relations for a given user.
	 * @param username
	 * @return
	 */
	public static List<Attend> findByUser(String username) {
		return find.where().eq("username", username).findList();
	}
	
	/**
	 * Get all of the Attend relations for a given event.
	 * @param eventID
	 * @return
	 */
	public static List<Attend> findByEvent(String eventID) {
		return find.where().eq("eventID", eventID).findList();
	}
	
	public static boolean userIsAttending(String username, String eventID) {
		return find.where().eq("username", username).eq("eventID", eventID).
				findList().size() > 0;
	}
	
	public static void create(String username, String eventID) {
		if (username == null || eventID == null) {
			return;
		}
		Attend attend = new Attend(username, eventID);
		attend.save();
	}
	
	public static void delete(String username, String eventID) {
		if (username == null || eventID == null) {
			return;
		}
		Attend attend = find.where().eq("username", username).eq("eventID",
				eventID).findUnique();
		attend.delete();
	}
	
	
}
