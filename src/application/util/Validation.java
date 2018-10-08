package application.util;

public class Validation {
	
	public static boolean isContactValidated(String contact) {
		return (contact.length()==11)?true:false;
	}
	
	public static boolean isPasswordValidated(String pass, String confirmPass) {
		return (pass.equals(confirmPass))?true:false;
	}
}
