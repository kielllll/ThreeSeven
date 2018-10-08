package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VehicleCategory {
	private final IntegerProperty categoryID;
	private final StringProperty description;
	
	public VehicleCategory() {
		this(0, null);
	}
	
	public VehicleCategory(int categoryID, String description) {
		this.categoryID = new SimpleIntegerProperty(categoryID);
		this.description = new SimpleStringProperty(description);
	}
	
	// Setter methods
	public void setCategoryID(int categoryID) {
		this.categoryID.set(categoryID);
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	// Getter methods
	public int getCategoryID() {
		return categoryID.get();
	}
	
	public IntegerProperty categoryIDProperty() {
		return categoryID;
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
