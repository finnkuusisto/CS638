import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.AchievementInfo;
import models.Attend;
import models.EventInfo;
import models.UserInfo;
import models.ZipCodeInfo;

import com.avaje.ebean.Ebean;

import extra.PaceUtil;

import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        InitialData.insert(app);
    }
	
    static class InitialData {
    	
    	public static void insert(Application app) {
    		if (Ebean.find(ZipCodeInfo.class).findRowCount() == 0) {
    			
    			Map<String,List<Object>> all = 
    					(Map<String,List<Object>>)Yaml.load("initial-data.yml");
    			
                // Insert all the zip codes
                Ebean.save(all.get("zips"));
    			
    			// Insert users first
    			//Finn and Paul to start
    			UserInfo finn = new UserInfo("Finn Kuusisto", "finn",
    					"finn.kuusisto@gmail.com", "53726", 1353463497488L,
    					"finn");
    			finn.about = "I am super-duper awesome.";
    			finn.save();
    			
    			UserInfo paul = new UserInfo("Paul Bolanowski", "paul",
    					"marathonpaulb@gmail.com", "53706", 1353453497488L,
    					"paul");
    			paul.about = "I am not quite as super-duper awesome as Finn.  Finn is pretty darn cool.";
    			paul.save();
    			
    			//then a bunch of randoms
                for (int i = 0; i < 100; i++) {
                	genRandomUser(i).save();
                }
                
                //make all of their AchievementInfo records
                for (UserInfo user : UserInfo.findAll()) {
                	if (AchievementInfo.findAchievementInfoFor(user.username)
                			== null) {
                		AchievementInfo.create(user.username);
                	}
                }
                
                //create random events
                genRandomEvent(20);

    		}
    	}
    	
    	//to generate random unique users
    	static String[] firsts = "James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Mary,Patricia,Linda,Barbara,Elizabeth,Jennifer,Maria,Susan,Margaret,Dorothy".split(",");
    	static String[] lasts = "Smith,Johnson,Williams,Brown,Jones,Miller,Davis,Garcia,Rodriguez,Wilson".split(",");
    	static String[] zips = "53562,53701,53702,53703,53704,53705,53706,53707,53708,53711,53713,53714,53715,53716,53717,53718,53719,53725,53726,53744,53774,53778,53779,53782,53783,53784,53785,53786,53787,53788,53789,53790,53791,53792,53793,53794".split(",");
    	static long join = 1353463497488L;
    	static String hash = "F3A4749FD47F18996B6579745B14D3E2D791E6F522448AE41F46A1C08CB0D7F2";
    	static String salt = "4B813C6F90D2D8C162BB67F16F6E92873258999EC875DEBB5A3571BB875D8322";
    	static Set<String> taken = new HashSet<String>();
    	
    	public static String[] genUniqueName() {
    	    String firstName = firsts[(int)(Math.random() * firsts.length)];
    	    String lastName = lasts[(int)(Math.random() * lasts.length)];
    	    while (taken.contains(firstName + " " + lastName)) {
        	    firstName = firsts[(int)(Math.random() * firsts.length)];
        	    lastName = lasts[(int)(Math.random() * lasts.length)];
    	    }
    	    taken.add(firstName + " " + lastName);
    	    return new String[]{firstName,lastName};
    	}
    	
    	public static UserInfo genRandomUser(int i) {
		    String[] names = genUniqueName();
		    String username = names[0].toLowerCase() + names[1].toLowerCase();
		    String email = "random.email." + i + "@gmail.com";
		    String zipCode = zips[(int)(Math.random() * zips.length)];
		    long joinDate = join - (long)(Math.random() * 1000 * 60 * 60 * 24 * 365);
		    String fullName = names[0] + " " + names[1];
		    String about = "I am an auto-generated user.";
		    String password = "pass";
		    UserInfo info = new UserInfo(fullName, username, email, zipCode,
		    		joinDate, password);
		    info.about = about;
		    //generate random 5k time
		    int min5k = 15 + (int)(Math.random() * 14);
		    int sec5k = (int)(Math.random() * 60);
		    info.predicted5k = PaceUtil.timeToSec(min5k, sec5k);
		    return info;
    	}
    	
    	static String[] eventNames = "Easy Run,Long Run,Short Run,Intervals,Hill Workout,Mile Repeats,Threshold,Fartlek,Pace Work,Friendly Race".split(",");
    	static double[] distances = {4, 5, 8, 10, 15};
    	public static void genRandomEvent(int num) {
    		List<UserInfo> users = UserInfo.findAll();
    		for (int i = 0; i < num; i++) {
    			//make the event
    			UserInfo user = users.get((int)(Math.random() * users.size()));
    			String username = user.username;
    			String name = user.fullName + "'s " + 
    					eventNames[(int)(Math.random() * eventNames.length)];
    			double distance =
    					distances[(int)(Math.random() * distances.length)];
    			String unit = "km";
			String description = "We'll start at the terrace, go out to picnic point, run behind Eagle Heights, head back down Johnson to Park, and finish at the terrace.";
    			EventInfo event = EventInfo.create(username, name, "", distance,
    					unit, description, "");
    			//now get some folks attending
    			Attend.create(username, event.id);
    			int numAttendees = (int)(Math.random() * 5);
    			for (int j = 0; j < numAttendees; j++) {
    				UserInfo attender =
    						users.get((int)(Math.random() * users.size()));
    				Attend.create(attender.username, event.id);
    			}
    		}
    	}
    	
    }
    
}
