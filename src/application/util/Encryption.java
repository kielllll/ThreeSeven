package application.util;

public class Encryption extends BCrypt{
	
	// Ecrypts the plain text password
	public static String hashPassword(String password) {
		return hashpw(password, gensalt());
	}
	
	public static boolean validatePassword(String password, String encryptedPassword) {
		return (checkpw(password,encryptedPassword))?true:false;
	}
}
