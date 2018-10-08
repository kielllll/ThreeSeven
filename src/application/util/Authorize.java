package application.util;

public class Authorize {
	
	private static Authorize instance = new Authorize();
	
	public boolean isEmployee(String auth) {
		return (auth.equals("employee"))?true:false;
	}
	
	public static Authorize getInstance() {
		return instance;
	}
}
