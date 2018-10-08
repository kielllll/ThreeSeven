package application.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StockAdjustment {
	private final IntegerProperty stockAdjID;
	private final ObjectProperty<LocalDate> date;
	private final StringProperty remarks;
	private final StringProperty status;
	
	public StockAdjustment() {
		this(0,null,null,null);
	}
	
	public StockAdjustment(int stockAdjID, LocalDate date, String remarks, String status) {
		this.stockAdjID = new SimpleIntegerProperty(stockAdjID);
		this.date = new SimpleObjectProperty<LocalDate>(date);
		this.remarks = new SimpleStringProperty(remarks);
		this.status = new SimpleStringProperty(status);
	}
	
	// Setter methods
	public void setStockAdjID(int stockAdjID) {
		this.stockAdjID.set(stockAdjID);
	}
	
	public void setDate(LocalDate date) {
		this.date.set(date);
	}
	
	public void setRemarks(String remarks) {
		this.remarks.set(remarks);
	}
	
	public void setStatus(String status) {
		this.status.set(status);
	}
	
	// Getter methods
	public int getStockAdjID() {
		return stockAdjID.get();
	}
	
	public IntegerProperty stockAdjIDProperty() {
		return stockAdjID;
	}
	
	public LocalDate getDate() {
		return date.get();
	}
	
	public ObjectProperty<LocalDate> dateProperty(){
		return date;
	}
	
	public String getRemarks() {
		return remarks.get();
	}
	
	public StringProperty remarksProperty() {
		return remarks;
	}
	
	public String getStatus() {
		return status.get();
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
	@Override
	public String toString() {
		return "(NULL,'"+date.get()+"','"+remarks.get()+"','"+status.get()+"')";
	}
}
