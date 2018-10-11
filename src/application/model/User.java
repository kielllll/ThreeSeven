package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

	private final IntegerProperty userID;
	private final IntegerProperty loginID;
	private final StringProperty firstname;
	private final StringProperty lastname;
	private final StringProperty password;
	private final StringProperty accessType;
	private final StringProperty status;
	
	//Calling the super constructor to initialize properties
	public User() {
		this(0,0,null,null,null,null,null);
	}
	
	//Constructor when instantiating an instance of User
	public User(int userID, int loginID, String firstname, String lastname, String password, String accessType, String status) {
		this.userID = new SimpleIntegerProperty(userID);
		this.loginID = new SimpleIntegerProperty(loginID);
		this.firstname = new SimpleStringProperty(firstname);
		this.lastname = new SimpleStringProperty(lastname);
		this.password = new SimpleStringProperty(password);
		this.accessType = new SimpleStringProperty(accessType);
		this.status = new SimpleStringProperty(status);
	}
	
	//Setter methods
	public void setUserID(int userID) {
		this.userID.set(userID);
	}
	
	public void setLoginID(int loginID) {
		this.loginID.set(loginID);
	}
	
	public void setFirstname(String firstname) {
		this.firstname.set(firstname);
	}
	
	public void setLastname(String lastname) {
		this.lastname.set(lastname);
	}
	
	public void setPassword(String password) {
		this.password.set(password);
	}
	
	public void setAccessType(String accessType) {
		this.accessType.set(accessType);
	}
	
	public void setStatus(String status) {
		this.status.set(status);
	}
	
	//Getter methods
	public int getUserID() {
		return userID.get();
	}
	
	public IntegerProperty userIDProperty() {
		return userID;
	}

	public int getLoginID() {
		return loginID.get();
	}
	
	public IntegerProperty loginIDProperty() {
		return loginID;
	}
	
	public String getFirstname() {
		return firstname.get();
	}
	
	public StringProperty firstnameProperty() {
		return firstname;
	}
	
	public String getLastname() {
		return lastname.get();
	}
	
	public StringProperty lastnameProperty() {
		return lastname;
	}
	
	public String getPassword() {
		return password.get();
	}
	
	public StringProperty passwordProperty() {
		return password;
	}
	
	public String getAccessType() {
		return accessType.get();
	}
	
	public StringProperty accessTypeProperty() {
		return accessType;
	}
	
	public String getStatus() {
		return status.get();
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
	//For storing data in database or viewing data
	@Override
	public String toString() {
		int accessType = (this.accessType.get().equalsIgnoreCase("administrator"))?1:2;
		return "(NULL,"+loginID.get()+",'"+firstname.get()+"','"+lastname.get()+"','"+password.get()+"','"+accessType+"','"+status.get()+"')";
	}
}
