package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import extra.PaceUtil;
import extra.Unit;

@Entity
public class RaceTime extends Model {
	
	@Id
	public String id;
	
	public String username;
	public String title;
	public int time;
	//always store distance in km
	public double km;
	public Unit displayUnit;
	public long date;
	
	public RaceTime(String username, String title, int time, double km,
			Unit displayUnit, long date) {
		this.username = username;
		this.title = title;
		this.time = time;
		this.km = km;
		this.displayUnit = displayUnit;
		this.date = date;
	}
	
	///////////
	//Helpers//
	///////////
	
	public String getTimeString() {
		return PaceUtil.timeString(this.time);
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
	
    public static Model.Finder<String,RaceTime> find =
		new Model.Finder(String.class, RaceTime.class);
    
    public static List<RaceTime> findByUsername(String username) {
    	return find.where().eq("username", username).findList();
    }
    
    public static List<RaceTime> findByUsernameOrderBy(String username,
    		String sortCol) {
    	return find.where().eq("username", username).orderBy(sortCol).
    			findList();
    }
    
    public static void create(String username, String title, int time,
    		double km, Unit displayUnit, long date) {
    	RaceTime rt = new RaceTime(username, title, time, km, displayUnit, date);
    	rt.save();
    }
    
    public static void deleteRaceTime(String id) {
    	RaceTime rt = find.where().eq("id", id).findUnique();
    	if (rt != null) {
    		rt.delete();
    	}
    }

}
