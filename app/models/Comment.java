package models;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Comment extends Model {

	@Id
	public String id;
	
	//username of the user attending
	public String username;
	
	public String comment;
	
	public long time;
	//id of the event being attended
	public String eventID;
	
	public Comment() {}
	
	public Comment(String username, String comment, String eventID, long time) {
		this.username = username;
		this.eventID = eventID;
		this.comment = comment;
		this.time = time;
	}
	
	///////////
	//Queries//
	///////////
	
	public static Model.Finder<String,Comment> find =
			new Model.Finder(String.class, Comment.class);
	
	
	/**
	 * Get the number of comments for the event
	 * @param eventID
	 * @return
	 */
	public static int numCommentsForEvent(String eventID) {
		return find.where().eq("eventID", eventID).findRowCount();
	}
	
	/**
	 * Get all comments for event
	 * @param eventID
	 * @return
	 */
	public static List<Comment> findCommentsForEvent(String eventID) {
		return find.where().eq("eventID", eventID).findList();
	}
	

	
	public static Comment findByID(String id) {
		return find.where().eq("id", id).findUnique();
	}
	

	public static void create(String username, String comment, String eventID) {
		if (username == null || eventID == null || comment == null) {
			return;
		}
		Date now = new Date();
		Comment newComment = new Comment(username,comment, eventID, now.getTime());
		newComment.save();
	}
	public static void delete(String eventID, String commentID) {
			if (eventID == null || commentID == null) {
				return;
			}
			Comment theComment = find.where().eq("id", commentID).eq("eventID",
					eventID).findUnique();
			theComment.delete();
		}
	
	public static void delete(String username, String comment, long time, String eventID) {
		if (username == null || eventID == null || comment == null) {
			return;
		}
		Comment theComment = find.where().eq("username", username).eq("eventID",
				eventID).eq("comment", comment).eq("time", time).findUnique();
		theComment.delete();
	}
	
	
	
}
