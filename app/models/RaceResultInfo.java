package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import extra.PaceUtil;
import extra.Unit;

import play.db.ebean.Model;

@Entity
public class RaceResultInfo extends Model {

		@Id
		public String id;
		
		public String raceName;
		public String firstName;
		public String lastName;
		public double seconds;
		public Unit displayUnit;
		public double km;
		public long date;
		
		public RaceResultInfo(){}
		
		public RaceResultInfo(String raceName, String firstName, String lastName, int seconds, long date, Unit displayUnit,
				double km){
			this.raceName = raceName;
			this.firstName = firstName;
			this.lastName = lastName;
			this.seconds = seconds;
			this.date = date;
			this.displayUnit = displayUnit;
			this.km = km;
			
		}
		
		public String getTimeString(){
			return PaceUtil.timeString((int)this.seconds);
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
		public static Model.Finder<String,RaceResultInfo> find =
			new Model.Finder(String.class, RaceResultInfo.class);
	    
		public static List<RaceResultInfo> findByName(String firstName, String lastName) {
			return find.where().eq("lastName", lastName).eq("firstName", firstName).findList();
		}
		
		public static RaceResultInfo findById(String id) {
	    	if (id == null) {
	    		return null;
	    	}
	        return find.where().eq("id", id).findUnique();
	    }
		
		public static void deleteResult(String id){
			RaceResultInfo info = find.where().eq("id", id).findUnique();
			info.delete();
		}
	    
		
		
}
