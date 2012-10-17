package extra;

public class PaceUtil {

	public static final double KM_PER_MILE = 1.60934;
	public static final int SEC_PER_MIN = 60;
	public static final int SEC_PER_HOUR = SEC_PER_MIN * 60;
	public static final int SEC_PER_DAY = SEC_PER_HOUR * 24;
	
	public static double kmToMile(double km) {
		return (km / PaceUtil.KM_PER_MILE);
	}
	
	public static double mileToKm(double mile) {
		return (mile * PaceUtil.KM_PER_MILE);
	}
	
	public static int timeToSec(int minutes, int seconds) {
		return PaceUtil.timeToSec(0, minutes, seconds);
	}
	
	public static int timeToSec(int hours, int minutes, int seconds) {
		return (hours * PaceUtil.SEC_PER_HOUR) +
				(minutes * PaceUtil.SEC_PER_MIN) + seconds;
	}
	
	public static String timeString(int seconds) {
		int hours = seconds / PaceUtil.SEC_PER_HOUR;
		seconds -= (hours * PaceUtil.SEC_PER_HOUR);
		int minutes = seconds / PaceUtil.SEC_PER_MIN;
		seconds -= (minutes * PaceUtil.SEC_PER_MIN);
		String ret = minutes + ":" + seconds;
		if (hours > 0) {
			ret = hours + ":" + ret;
		}
		return ret;
	}
	
	public static int runTimeEstimate(double inKm, int inSec, double outKm) {
		//Based on an article entitled Time Predicting by Peter S. Riegel
		//outSec = inSec * (outKm / inKm) ^ 1.06
		double time = inSec * Math.pow((outKm / inKm), 1.06);
		return (int)time;
	}
	
}
