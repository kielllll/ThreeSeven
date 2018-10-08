package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Stock {
	private final IntegerProperty itemID;
	private final StringProperty name;
	private final IntegerProperty quantity;
	
	public Stock() {
		this(0,null,0);
	}
	
	public Stock(int itemID, String name, int quantity) {
		this.itemID = new SimpleIntegerProperty(itemID);
		this.name = new SimpleStringProperty(name);
		this.quantity = new SimpleIntegerProperty(quantity);
	}
	
	// Setter methods
	public void setItemID(int itemID) {
		this.itemID.set(itemID);
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}
	
	// Getter methods
	public int getItemID() {
		return itemID.get();
	}
	
	public IntegerProperty itemIDProperty() {
		return itemID;
	}
	
	public String getName() {
		return name.get();
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public int getQuantity() {
		return quantity.get();
	}
	
	public IntegerProperty quantityProperty() {
		return quantity;
	}
	
	@Override
	public String toString() {
		return "("+itemID.get()+","+quantity.get()+")";
	}
}
