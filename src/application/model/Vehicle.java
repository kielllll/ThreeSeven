package application.model;

import java.time.LocalDate;

import application.DAO.VehicleCategoryDAOImpl;
import application.DAO.VehicleModelDAOImpl;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Vehicle {
	private final IntegerProperty vehicleID;
	private final StringProperty plateNumber;
	private final StringProperty mvNumber;
	private final StringProperty engineNumber;
	private final StringProperty chassisNumber;
	private final StringProperty model;
	private final StringProperty type;
	private final StringProperty encumberedTo;
	private final DoubleProperty amount;
	private final ObjectProperty<LocalDate> maturityDate;
	private final StringProperty status;
	private final ObjectProperty<Image> image;
	
	public Vehicle() {
		this(0,null,null,null,null,null,null,null,0.00,null,null,null);
	}
	
	public Vehicle(int vehicleID,String plateNumber, String mvNumber, String engineNumber, String chassisNumber, String model, String type, String encumberedTo, double amount, LocalDate maturityDate, String status, Image image) {
		this.vehicleID = new SimpleIntegerProperty(vehicleID);
		this.plateNumber = new SimpleStringProperty(plateNumber);
		this.mvNumber = new SimpleStringProperty(mvNumber);
		this.engineNumber = new SimpleStringProperty(engineNumber);
		this.chassisNumber = new SimpleStringProperty(chassisNumber);
		this.model = new SimpleStringProperty(model);
		this.type = new SimpleStringProperty(type);
		this.encumberedTo = new SimpleStringProperty(encumberedTo);
		this.amount = new SimpleDoubleProperty(amount);
		this.maturityDate = new SimpleObjectProperty<LocalDate>(maturityDate);
		this.status = new SimpleStringProperty(status);
		this.image = new SimpleObjectProperty<Image>(image);
	}
	
	// Setter methods
	public void setVehicleID(int vehicleID) {
		this.vehicleID.set(vehicleID);
	}
	
	public void setPlateNumber(String plateNumber) {
		this.plateNumber.set(plateNumber);
	}
	
	public void setMvNumber(String mvNumber) {
		this.mvNumber.set(mvNumber);
	}
	
	public void setEngineNumber(String engineNumber) {
		this.engineNumber.set(engineNumber);
	}
	
	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber.set(chassisNumber);
	}
	
	public void setModel(String model) {
		this.model.set(model);
	}
	
	public void setType(String type) {
		this.type.set(type);
	}
	
	public void setEncumberedTo(String encumberedTo) {
		this.encumberedTo.set(encumberedTo);
	}
	
	public void setAmount(double amount) {
		this.amount.set(amount);
	}
	
	public void setMaturityDate(LocalDate maturityDate) {
		this.maturityDate.set(maturityDate);
	}
	
	public void setStatus(String status) {
		this.status.set(status);
	}
	
	public void setImage(Image image) {
		this.image.set(image);
	}
	
	// Getter methods
	public int getVehicleID() {
		return vehicleID.get();
	}
	
	public IntegerProperty vehicleIDProperty() {
		return vehicleID;
	}
	
	public String getPlateNumber() {
		return plateNumber.get();
	}
	
	public StringProperty plateNumberProperty() {
		return plateNumber;
	}
	
	public String getMvNumber() {
		return mvNumber.get();
	}
	
	public StringProperty mvNumberProperty() {
		return mvNumber;
	}
	
	public String getEngineNumber() {
		return engineNumber.get();
	}
	
	public StringProperty engineNumberProperty() {
		return engineNumber;
	}
	
	public String getChassisNumber() {
		return chassisNumber.get();
	}
	
	public StringProperty chassisNumberProperty() {
		return chassisNumber;
	}
	
	public String getModel() {
		return model.get();
	}
	
	public StringProperty modelProperty() {
		return model;
	}
	
	public String getType() {
		return type.get();
	}
	
	public StringProperty typeProperty() {
		return type;
	}
	
	public String getEmcumberedTo() {
		return encumberedTo.get();
	}
	
	public StringProperty encumberedToProperty() {
		return encumberedTo;
	}
	
	public double getAmount() {
		return amount.get();
	}
	
	public DoubleProperty amountProperty() {
		return amount;
	}
	
	public LocalDate getMaturityDate() {
		return maturityDate.get();
	}
	
	public ObjectProperty<LocalDate> maturityDateProperty(){
		return maturityDate;
	}
	
	public String getStatus() {
		return status.get();
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
	public Image getImage() {
		return image.get();
	}
	
	public ObjectProperty<Image> imageProperty(){
		return image;
	}
	
	@Override
	public String toString() {
		int modelID = VehicleModelDAOImpl.getInstance()
										.getAll()
										.parallelStream()
										.filter(m-> m.getDescription().equals(model.get()))
										.findFirst()
										.get()
										.getModelID();
		
		int categoryID = VehicleCategoryDAOImpl.getInstance()
										.getAll()
										.parallelStream()
										.filter(c->c.getDescription().equals(type.get()))
										.findFirst()
										.get()
										.getCategoryID();
		
		
		
		return "(NULL,'"+plateNumber.get()+"',"+plateNumber.get()+"',"+mvNumber.get()+"',"+engineNumber.get()+"',"+chassisNumber.get()+"',"+modelID+","+categoryID+",'"+encumberedTo.get()+"',"+amount.get()+",'"+maturityDate.get()+"','"+status.get()+"')";
	}
}
