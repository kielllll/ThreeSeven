package application.model;

import java.time.LocalDate;

import application.DAO.VehicleDAOImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PreventiveMaintenance {
	private final IntegerProperty pmID;
	private final StringProperty plateNumber;
	private final StringProperty description;
	private final ObjectProperty<LocalDate> date;
	
	public PreventiveMaintenance() {
		this(0,null,null,null);
	}
	
	public PreventiveMaintenance(int pmID, String plateNumber, String description, LocalDate date) {
		this.pmID = new SimpleIntegerProperty(pmID);
		this.plateNumber = new SimpleStringProperty(plateNumber);
		this.description = new SimpleStringProperty(description);
		this.date = new SimpleObjectProperty<LocalDate>(date);
	}
	
	// Setter methods
	public void setPmID(int pmID) {
		this.pmID.set(pmID);
	}
	
	public void setPlateNumber(String plateNumber) {
		this.plateNumber.set(plateNumber);
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	public void setDate(LocalDate date) {
		this.date.set(date);
	}
	
	// Getter methods
	public int getPmID() {
		return pmID.get();
	}
	
	public IntegerProperty pmIDProperty() {
		return pmID;
	}
	
	public String getPlateNumber() {
		return plateNumber.get();
	}
	
	public StringProperty plateNumberProperty() {
		return plateNumber;
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public StringProperty descriptionProperty() {
		return description;
	}
	
	public LocalDate getDate() {
		return date.get();
	}
	
	public ObjectProperty<LocalDate> dateProperty(){
		return date;
	}
	
	@Override
	public String toString() {
		int vehicleID = VehicleDAOImpl.getInstance()
								.getAll()
								.stream()
								.filter(i->i.getPlateNumber().equals(plateNumber.get()))
								.findFirst()
								.get()
								.getVehicleID();
		
		return "(NULL,"+vehicleID+",'"+description.get()+"','"+date.get()+"')";
	}
}
