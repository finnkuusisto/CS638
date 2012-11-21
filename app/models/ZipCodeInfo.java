package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class ZipCodeInfo extends Model {

	@Id
	public String zipcode;
	public String state;
	public String fipsRegion;
	public String city;
	public double latitude;
	public double longitude;
	
	public ZipCodeInfo(String zipcode, String state, String fipsRegion,
			String city, double latitude, double longitude) {
		this.zipcode = zipcode;
		this.state = state;
		this.fipsRegion = fipsRegion;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	///////////
    //Queries//
    ///////////
	
    public static Model.Finder<String,ZipCodeInfo> find =
		new Model.Finder(String.class, ZipCodeInfo.class);
    
    /**
     * Retrieve all
     */
    public static List<ZipCodeInfo> findAll() {
    	return find.all();
    }
    
    public static boolean  zipCodeExists(String zipcode) {
    	return ZipCodeInfo.findByZipCode(zipcode) != null;
    }
    
    public static ZipCodeInfo findByZipCode(String zipcode) {
    	return find.where().eq("zipcode", zipcode).findUnique();
    }
    
    public static List<ZipCodeInfo> findByCity(String city) {
    	return find.where().eq("city", city).findList();
    }
    
    public static void create(String zipcode, String state, String fipsRegion,
			String city, double latitude, double longitude) {
    	ZipCodeInfo zci = new ZipCodeInfo(zipcode, state, fipsRegion, city,
    			latitude, longitude);
    	zci.save();
    }

    ///////////
    //Helpers//
    ///////////
    
    public static String getLocationString(String zip) {
    	ZipCodeInfo info = ZipCodeInfo.findByZipCode(zip);
    	if (info != null) {
    		return info.city + ", " + info.state;
    	}
    	return "Somewhere, USA";
    }
    
    public static String getValidatedZipCode(String zip) {
    	if (zip != null) {
    		zip = zip.trim();
    		if (zip.length() > 5) {
    			zip = zip.substring(0, 5);
    		}
    		if (!ZipCodeInfo.zipCodeExists(zip)) {
    			zip = null;
    		}
    	}
    	return zip;
    }
    
    public static double distanceBetweenZips(String zip1, String zip2) {
    	//first get both locations
    	ZipCodeInfo z1 = ZipCodeInfo.findByZipCode(zip1);
    	ZipCodeInfo z2 = ZipCodeInfo.findByZipCode(zip2);
    	//if either is no good, just say they are infinitely far apart
    	if (z1 == null || z2 == null) {
    		return Double.POSITIVE_INFINITY;
    	}
    	//otherwise compute it
    	return ZipCodeInfo.haversine(z1.latitude, z1.longitude,
    			z2.latitude, z2.longitude);
    }
    
    private static double haversine(double lat1, double lon1,
    		double lat2, double lon2) {
    	//radius of the earth in km
    	final double EARTH_R = 6371;
    	//convert to radians
    	lat1 = Math.toRadians(lat1);
    	lon1 = Math.toRadians(lon1);
    	lat2 = Math.toRadians(lat2);
    	lon2 = Math.toRadians(lon2);
    	//and haversine that shit
    	double dLat = lat2 - lat1;
    	double dLon = lon2 - lon1; 
    	double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    	        Math.cos(lat1) * Math.cos(lat2) * 
    	        Math.sin(dLon / 2) * Math.sin(dLon / 2); 
    	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); 
    	return (EARTH_R * c); // Distance in km
    }
    
}