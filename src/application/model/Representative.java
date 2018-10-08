package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Representative {
	private final IntegerProperty repID;
	private final StringProperty repName;
	private final StringProperty repContact;
	
	//Calling the super constructor to initialize properties
	public Representative() {
		this(0,null,null);
	}
	
	//Constructor when instantiating an instance of User
	public Representative(int repID, String repName, String repContact) {
		this.repID = new SimpleIntegerProperty(repID);
		this.repName = new SimpleStringProperty(repName);
		this.repContact = new SimpleStringProperty(repContact);
	}
	
	//Setter Methods
	public void setRepID(int repID) {
		this.repID.set(repID);
	}
	
	public void setRepName(String repName) {
		this.repName.set(repName);
	}
	
	public void setRepContact(String repContact) {
		this.repContact.set(repContact);
	}
	
	//Getter Methods
	public int getRepID() {
		return repID.get();
	}
	
	public IntegerProperty repIDProperty() {
		return repID;
	}
	
	public String getRepName() {
		return repName.get();
	}
	
	public StringProperty repNameProperty() {
		return repName;
	}
	
	public String getRepContact() {
		return repContact.get();
	}
	
	public StringProperty repContactProperty() {
		return repContact;
	}
	
	@Override
	public String toString() {
		return "(NULL,'"+repName.get()+"','"+repContact.get()+"')";
	}
}
