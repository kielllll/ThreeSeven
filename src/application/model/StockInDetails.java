package application.model;

import application.DAO.ItemDAOImpl;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StockInDetails {
	
	private final IntegerProperty stockInID;
	private final StringProperty name;
	private final IntegerProperty quantity;
	private final DoubleProperty cost;
	private final DoubleProperty subTotal;
	
	public StockInDetails() {
		this(0,null,0,0.0,0.0);
	}
	
	public StockInDetails(int stockInID, String name, int quantity, double cost, double subTotal) {
		this.stockInID = new SimpleIntegerProperty(stockInID);
		this.name = new SimpleStringProperty(name);
		this.quantity = new SimpleIntegerProperty(quantity);
		this.cost = new SimpleDoubleProperty(cost);
		this.subTotal = new SimpleDoubleProperty(subTotal);
	}
	
	// Setter methods
	public void setStockInID(int stockInID) {
		this.stockInID.set(stockInID);
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}
	
	public void setCost(double cost) {
		this.cost.set(cost);
	}
	
	public void setSubTotal(double subTotal) {
		this.subTotal.set(subTotal);
	}
	
	// Getter mthods
	public int getStockInID() {
		return stockInID.get();
	}
	
	public IntegerProperty stockInIDProperty() {
		return stockInID;
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
	
	public double getCost() {
		return cost.get();
	}
	
	public DoubleProperty costProperty() {
		return cost;
	}
	
	public double getSubTotal() {
		return subTotal.get();
	}
	
	public DoubleProperty subTotalProperty() {
		return subTotal;
	}
	
	@Override
	public String toString() {
		int itemID = ItemDAOImpl.getInstance()
								.getAll()
								.parallelStream()
								.filter(e->e.getName().equals(name.get()))
								.findFirst()
								.get()
								.getItemID();
		
		return "("+stockInID.get()+","+itemID+","+quantity.get()+")";
	}
}
