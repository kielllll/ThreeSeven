package application.model;

import application.DAO.ItemDAOImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StockOutDetails {
	private final IntegerProperty stockOutID;
	private final StringProperty name;
	private final IntegerProperty quantity;
	
	public StockOutDetails() {
		this(0,null,0);
	}
	
	public StockOutDetails(int stockOutID, String name, int quantity) {
		this.stockOutID = new SimpleIntegerProperty(stockOutID);
		this.name = new SimpleStringProperty(name);
		this.quantity = new SimpleIntegerProperty(quantity);
	}
	
	// Setter methods
	public void setStockOutID(int stockOutID) {
		this.stockOutID.set(stockOutID);
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}
	
	// Getter methods
	public int getStockOutID() {
		return stockOutID.get();
	}
	
	public IntegerProperty stockOutIDProperty() {
		return stockOutID;
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
		int itemID = ItemDAOImpl.getInstance()
								.getAll()
								.stream()
								.filter(i->i.getName().equals(name.get()))
								.findFirst()
								.get()
								.getItemID();
		
		return "("+stockOutID.get()+","+itemID+","+quantity.get()+")";
	}
}
