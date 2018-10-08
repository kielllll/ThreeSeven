package application.model;

import application.DAO.SupplierDAOImpl;
import application.DAO.UnitDAOImpl;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
	private final IntegerProperty itemID;
	private final StringProperty name;
	private final StringProperty supplier;
	private final StringProperty description;
	private final DoubleProperty price;
	private final StringProperty unit;
	private final IntegerProperty quantity;
	private final StringProperty status;
	
	public Item() {
		this(0,null,null,null,0.0,null,0,null);
	}
	
	public Item(int itemID, String name, String supplier, String description, double price, String unit, int quantity, String status) {
		this.itemID = new SimpleIntegerProperty(itemID);
		this.name = new SimpleStringProperty(name);
		this.supplier = new SimpleStringProperty(supplier);
		this.description = new SimpleStringProperty(description);
		this.price = new SimpleDoubleProperty(price);
		this.unit = new SimpleStringProperty(unit);
		this.quantity = new SimpleIntegerProperty(quantity);
		this.status = new SimpleStringProperty(status);
	}
	
	// Setter methods
	public void setItemID(int itemID) {
		this.itemID.set(itemID);
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public void setSupplier(String supplier) {
		this.supplier.set(supplier);
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	public void setPrice(double price) {
		this.price.set(price);
	}
	
	public void setUnit(String unit) {
		this.unit.set(unit);
	}
	
	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}
	
	public void setStatus(String status) {
		this.status.set(status);
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
	
	public String getSupplier() {
		return supplier.get();
	}
	
	public StringProperty supplierProperty() {
		return supplier;
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public StringProperty descriptionProperty() {
		return description;
	}
	
	public double getPrice() {
		return price.get();
	}
	
	public DoubleProperty priceProperty() {
		return price;
	}
	
	public String getUnit() {
		return unit.get();
	}
	
	public StringProperty unitProperty() {
		return unit;
	}
	
	public int getQuantity() {
		return quantity.get();
	}
	
	public IntegerProperty quantityProperty() {
		return quantity;
	}
	
	public String getStatus() {
		return status.get();
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
	@Override
	public String toString() {
		int supplierID = SupplierDAOImpl.getInstance()
										.getAll()
										.parallelStream()
										.filter(s -> s.getName().equals(supplier.get()))
										.findFirst()
										.get()
										.getSupplierID();
		
		int unitID = UnitDAOImpl.getInstance()
								.getAll()
								.parallelStream()
								.filter(u -> u.getDescription().equals(unit.get()))
								.findFirst()
								.get()
								.getUnitID();
		
		return "(NULL,'"+name.get()+"',"+supplierID+",'"+description.get()+"',"+price.get()+","+unitID+",'"+status.get()+"')";
	}
}
