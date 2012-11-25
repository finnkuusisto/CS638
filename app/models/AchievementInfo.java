package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class AchievementInfo extends Model {
	
	@Id
	public String username;
	
	public boolean firstEventCreation;
	public boolean firstEventAttend;
	public boolean firstEventAttendedByUser;
	public boolean firstFollowUser;
	public boolean firstFollowedByUser;
	
	public AchievementInfo(String username) {
		this.username = username;
	}
	
	///////////
	//Queries//
	///////////
	
	public static Model.Finder<String,AchievementInfo> find =
			new Model.Finder(String.class, AchievementInfo.class);
	
	public static AchievementInfo findAchievementInfoFor(String username) {
		return find.where().eq("username", username).findUnique();
	}
	
	public static void create(String username) {
		if (username == null) {
			return;
		}
		AchievementInfo achInfo = new AchievementInfo(username);
		achInfo.save();
	}

}
