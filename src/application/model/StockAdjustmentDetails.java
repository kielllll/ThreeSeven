package application.model;

import application.DAO.ItemDAOImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StockAdjustmentDetails {
	private final IntegerProperty stockAdjID;
	private final StringProperty name;
	private final IntegerProperty sysQty;
	private final IntegerProperty newQty;
	
	public StockAdjustmentDetails() {
		this(0,null,0,0);
	}
	
	public StockAdjustmentDetails(int stockAdjID, String name, int sysQty, int newQty) {
		this.stockAdjID = new SimpleIntegerProperty(stockAdjID);
		this.name = new SimpleStringProperty(name);
		this.sysQty = new SimpleIntegerProperty(sysQty);
		this.newQty = new SimpleIntegerProperty(newQty);
	}
	
	// Setter methods
	public void setStockAdjID(int stockAdjID) {
		this.stockAdjID.set(stockAdjID);
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public void setSysQty(int sysQty) {
		this.sysQty.set(sysQty);
	}
	
	public void setNewQty(int newQty) {
		this.newQty.set(newQty);
	}
	
	// Getter methods
	public int getStockAdjID() {
		return stockAdjID.get();
	}
	
	public IntegerProperty stockAdjIDProperty() {
		return stockAdjID;
	}
	
	public String getName() {
		return name.get();
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public int getSysQty() {
		return sysQty.get();
	}
	
	public IntegerProperty sysQtyProperty() {
		return sysQty;
	}
	
	public int getNewQty() {
		return newQty.get();
	}
	
	public IntegerProperty newQtyProperty() {
		return newQty;
	}
	
	@Override
	public String toString() {
		int itemID = ItemDAOImpl.getInstance()
							.getAll()
							.stream()
							.filter(e->e.getName().equals(name.get()))
							.findFirst()
							.get()
							.getItemID();
		
		return "("+stockAdjID.get()+","+itemID+","+sysQty.get()+","+newQty.get()+")";
	}
}
