package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import play.db.ebean.*;

@Entity
public class Follow extends Model {
	
	//username of user doing the following
	public String follower;
	//username of user being followed
	public String followed;
	
	public Follow(String follower, String followed) {
		this.follower = follower;
		this.followed = followed;
	}
	
	///////////
	//Queries//
	///////////
	
	public static Model.Finder<String,Follow> find =
			new Model.Finder(String.class, Follow.class);
	
	public static List<String> findFollowersOf(String username) {
		//first get all the relations where username is the followed user
		List<Follow> followers = Follow.findByFollowed(username);
		//and copy all the follower usernames over
		List<String> ret = new ArrayList<String>(followers.size());
		for (Follow f : followers) {
			ret.add(f.follower);
		}
		return ret;
	}
	
	public static List<String> findUsersFollowedBy(String username) {
		//first get all the relations where username is the following user
		List<Follow> followed = Follow.findByFollower(username);
		//and copy the usernames of those followed
		List<String> ret = new ArrayList<String>(followed.size());
		for (Follow f : followed) {
			ret.add(f.followed);
		}
		return ret;
	}
	
	/**
	 * Get the follow relations where the given username specifies the user
	 * doing the following.
	 * @param username
	 * @return
	 */
	public static List<Follow> findByFollower(String username) {
		return find.where().eq("follower", username).findList();
	}
	
	/**
	 * Get the follow relations where the given username specifies that user
	 * being followed.
	 * @param username
	 * @return
	 */
	public static List<Follow> findByFollowed(String username) {
		return find.where().eq("followed", username).findList();
	}
	
	public static boolean alreadyFollowing(String follower, String followed) {
		return find.where().eq("follower", follower).eq("followed", followed).
				findList().size() > 0;
	}
	
	public static void create(String follower, String followed) {
		Follow follow = new Follow(follower, followed);
		follow.save();
	}
	
}
