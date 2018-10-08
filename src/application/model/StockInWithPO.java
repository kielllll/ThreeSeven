package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class StockInWithPO {
	private final IntegerProperty stockID;
	private final IntegerProperty poID;
	
	public StockInWithPO() {
		this(0,0);
	}
	
	public StockInWithPO(int stockID, int poID) {
		this.stockID = new SimpleIntegerProperty(stockID);
		this.poID = new SimpleIntegerProperty(poID);
	}
	
	// Setter methods
	public void setStockID(int stockID) {
		this.stockID.set(stockID);
	}
	
	public void setPoID(int poID) {
		this.poID.set(poID);
	}
	
	// Getter methods
	public int getStockID() {
		return stockID.get();
	}
	
	public IntegerProperty stockIDProperty() {
		return stockID;
	}
	
	public int getPoID() {
		return poID.get();
	}
	
	public IntegerProperty poIDProperty() {
		return poID;
	}
	
	@Override
	public String toString() {
		return "("+stockID.get()+","+poID.get()+")";
	}
}
