package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AccessType {
	private final IntegerProperty accessTypeID;
	private final StringProperty description;
	
	//Calling the super constructor to initialize properties
	public AccessType() {
		this(0,null);
	}
	
	//Constructor when instantiating an instance of AccessType
	public AccessType(int accessTypeID, String description) {
		this.accessTypeID = new SimpleIntegerProperty(accessTypeID);
		this.description = new SimpleStringProperty(description);
	}
	
	//Setter methods
	public void setAccessTypeID(int accessTypeID) {
		this.accessTypeID.set(accessTypeID);
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	//Getter methods
	public int getAccessTypeID() {
		return accessTypeID.get();
	}
	
	public IntegerProperty accessTypeIDProperty() {
		return accessTypeID;
	}
	
	
	public String getDescription() {
		return description.get();
	}
	
	public StringProperty descriptionProperty() {
		return description;
	}
	
	//For storing data in database or viewing data
	@Override
	public String toString() {
		return "("+accessTypeID.get()+",'"+description.get()+"')";
	}
}
