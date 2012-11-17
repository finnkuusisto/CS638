package models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Gravatar {
	
	public static final String URL_PRE = "http://www.gravatar.com/avatar/";
	
	private String hash;
	
	public Gravatar(String email) {
		byte[] buf = email.trim().toLowerCase().getBytes();
		try {
			this.hash = Gravatar.toHexString(
					MessageDigest.getInstance("MD5").digest(buf));
			this.hash = this.hash.toLowerCase();
		}
		catch (NoSuchAlgorithmException e) {
			//all java implementations must implement MD5
		}
	}
	
	public String getURL() {
		return this.getURL(0, false);
	}
	
	public String getURL(int size, boolean useMysteryMan) {
		StringBuilder str = new StringBuilder();
		str.append(Gravatar.URL_PRE);
		str.append(this.hash);
		str.append(Gravatar.getOptions(size, useMysteryMan));
		return str.toString();
	}
	
	public static String getURL(String email) {
		return Gravatar.getURL(email, 0, false);
	}
	
	public static String getURL(String email, int size, boolean useMysteryMan) {
		return (new Gravatar(email)).getURL(size, useMysteryMan);
	}
	
	private static String getOptions(int size, boolean useMysteryMan) {
		StringBuilder opts = new StringBuilder();
		opts.append("?");
		if (size > 0 && size < 512) {
			opts.append("s=");
			opts.append(size);
		}
		if (useMysteryMan) {
			//check if we need the &
			if (opts.length() > 1) {
				opts.append("&");
			}
			opts.append("d=mm");
		}
		return (opts.length() > 1) ? opts.toString() : "";
	}
	
	private static String toHexString(byte[] buf) {
		BigInteger bi = new BigInteger(1, buf);
		return String.format("%0" + (buf.length << 1) + "X", bi);
	}

}