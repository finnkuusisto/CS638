package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class RaceResultInfo extends Model {

		@Id
		public String raceName;
		public String firstName;
		public String lastName;
		public double seconds;
		public String date;
		
		public RaceResultInfo(){}
		
		public RaceResultInfo(String raceName, String firstName, String lastName, double seconds, String date){
			this.raceName = raceName;
			this.firstName = firstName;
			this.lastName = lastName;
			this.seconds = seconds;
			this.date = date;
			
		}
		
		
}
