package application.model;

import application.DAO.RepresentativeDAOImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Supplier {
	private final IntegerProperty supplierID;
	private final StringProperty name;
	private final StringProperty repName;
	private final StringProperty repContact;
	private final StringProperty location;
	private final StringProperty status;
	
	//Calling the super constructor to initialize properties
	public Supplier() {
		this(0,null,null,null,null,null);
	}
	
	//Constructor when instantiating an instance of User
	public Supplier(int supplierID, String name, String repName, String repContact, String location, String status) {
		this.supplierID = new SimpleIntegerProperty(supplierID);
		this.name = new SimpleStringProperty(name);
		this.repName = new SimpleStringProperty(repName);
		this.repContact = new SimpleStringProperty(repContact);
		this.location = new SimpleStringProperty(location);
		this.status = new SimpleStringProperty(status);
	}
	
	//Setter Methods
	public void setSupplierID(int supplierID) {
		this.supplierID.set(supplierID);
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public void setRepName(String repName) {
		this.repName.set(repName);
	}
	
	public void setRepContact(String repContact) {
		this.repContact.set(repContact);
	}
	
	public void setLocation(String location) {
		this.location.set(location);
	}
	
	public void setStatus(String status) {
		this.status.set(status);
	}
	
	//Getter Methods
	public int getSupplierID() {
		return supplierID.get();
	}
	
	public IntegerProperty supplierIDProperty() {
		return supplierID;
	}
	
	public String getName() {
		return name.get();
	}
	
	public StringProperty nameProperty() {
		return name;
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
	
	public String getLocation() {
		return location.get();
	}
	
	public StringProperty locationProperty() {
		return location;
	}
	
	public String getStatus() {
		return status.get();
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
	@Override
	public String toString() {
		int repID = RepresentativeDAOImpl.getInstance().getAll().size();
		return "(NULL"+",'"+name.get()+"',"+repID+",'"+location.get()+"','"+status.get()+"')";
	}
}
