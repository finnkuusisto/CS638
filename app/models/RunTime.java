package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import extra.Unit;

@Entity
public class RunTime extends Model {
	
	@Id
	public String id;
	
	public String title;
	public int time;
	public double distance;
	public Unit unit;

}
