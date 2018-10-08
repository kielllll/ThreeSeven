package application.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AuditLog {
	private final IntegerProperty auditLogID;
	private final ObjectProperty<LocalDate> date;
	private final StringProperty time;
	private final IntegerProperty userID;
	private final StringProperty firstname;
	private final StringProperty lastname;
	private final StringProperty accessType;
	private final StringProperty action;
	
	//Calling the super constructor to initialize properties
	public AuditLog() {
		this(0,null,null,0,null,null,null,null);
	}
	
	//Constructor when instantiating an instance of AccessType
	public AuditLog(int auditLogID, LocalDate date, String time, int userID, String firstname, String lastname, String accessType, String action) {
		this.auditLogID = new SimpleIntegerProperty(auditLogID);
		this.date = new SimpleObjectProperty<LocalDate>(date);
		this.time = new SimpleStringProperty(time);
		this.userID = new SimpleIntegerProperty(userID);
		this.firstname = new SimpleStringProperty(firstname);
		this.lastname = new SimpleStringProperty(lastname);
		this.accessType = new SimpleStringProperty(accessType);
		this.action = new SimpleStringProperty(action);
	}
	
	//Setter methods
	public void setAuditLogID(int auditLogID) {
		this.auditLogID.set(auditLogID);
	}
	
	public void setDate(LocalDate date) {
		this.date.set(date);
	}
	
	public void setTime(String time) {
		this.time.set(time);
	}
	
	public void setUserID(int userID) {
		this.userID.set(userID);
	}
	
	public void setFirstname(String firstname) {
		this.firstname.set(firstname);
	}
	
	public void setLastname(String lastname) {
		this.lastname.set(lastname);
	}
	
	public void setAccessType(String accessType) {
		this.accessType.set(accessType);
	}
	
	public void setAction(String action) {
		this.action.set(action);
	}
	
	//Getter methods
	public int getAuditLogID() {
		return auditLogID.get();
	}
	
	public IntegerProperty auditLogIDProperty() {
		return auditLogID;
	}
	
	public LocalDate getDate() {
		return date.get();
	}
	
	public ObjectProperty<LocalDate> dateProperty() {
		return date;
	}
	
	public String getTime() {
		return time.get();
	}
	
	public StringProperty timeProperty() {
		return time;
	}
	
	public int getUserID() {
		return userID.get();
	}
	
	public IntegerProperty userIDProperty() {
		return userID;
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
	
	public String getAccessType() {
		return accessType.get();
	}
	
	public StringProperty accessTypeIDProperty() {
		return accessType;
	}
	
	public String getAction() {
		return action.get();
	}
	
	public StringProperty actionProperty() {
		return action;
	}
	
	//For storing data in database or viewing data
	@Override
	public String toString() {
		return "(NULL,"+userID.get()+",'"+date.get()+"','"+time.get()+"','"+action.get()+"')";
	}
}
