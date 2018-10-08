package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VehicleModel {
	private final IntegerProperty modelID;
	private final StringProperty description;
	
	public VehicleModel() {
		this(0,null);
	}
	
	public VehicleModel(int modelID, String description) {
		this.modelID = new SimpleIntegerProperty(modelID);
		this.description = new SimpleStringProperty(description);
	}
	
	// Setter methods
	public void setModelID(int modelID) {
		this.modelID.set(modelID);
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	// Getter methods
	public int getModelID() {
		return modelID.get();
	}
	
	public IntegerProperty modelIDProperty() {
		return modelID;
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public StringProperty descriptionProperty() {
		return description;
	}
	
	@Override
	public String toString() {
		return "(NULL,'"+description.get()+"')";
	}
}
