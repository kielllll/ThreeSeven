package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Unit {
	private final IntegerProperty unitID;
	private final StringProperty description;
	
	public Unit() {
		this(0, null);
	}
	
	public Unit(int unitID, String description) {
		this.unitID = new SimpleIntegerProperty(unitID);
		this.description = new SimpleStringProperty(description);
	}
	
	// Setter methods
	public void setUnitID(int unitID) {
		this.unitID.set(unitID);
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	// Getter methods
	public int getUnitID() {
		return unitID.get();
	}
	
	public IntegerProperty unitIDProperty() {
		return unitID;
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
