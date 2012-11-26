package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import extra.Unit;

@Entity
public class RunTime extends Model {
	
	@Id
	public String id;
	
	public String username;
	public String title;
	public int time;
	//always store distance in km
	public double km;
	public Unit displayUnit;
	public long date;
	
	public RunTime(String username, String title, int time, double km,
			Unit displayUnit, long date) {
		this.username = username;
		this.title = title;
		this.time = time;
		this.km = km;
		this.displayUnit = displayUnit;
		this.date = date;
	}
	
	///////////
    //Queries//
    ///////////
	
    public static Model.Finder<String,RunTime> find =
		new Model.Finder(String.class, RunTime.class);
    
    public static List<RunTime> findByUsername(String username) {
    	return find.where().eq("username", username).findList();
    }
    
    public static List<RunTime> findByUsernameOrderBy(String username,
    		String sortCol) {
    	return find.where().eq("username", username).orderBy(sortCol).
    			findList();
    }
    
    public static void create(String username, String title, int time,
    		double km, Unit displayUnit, long date) {
    	RunTime rt = new RunTime(username, title, time, km, displayUnit, date);
    	rt.save();
    }
    
    public static void deleteRunTime(String id) {
    	RunTime rt = find.where().eq("id", id).findUnique();
    	if (rt != null) {
    		rt.delete();
    	}
    }

}
