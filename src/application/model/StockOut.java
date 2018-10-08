package application.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StockOut {
	private final IntegerProperty stockOutID;
	private final ObjectProperty<LocalDate> date;
	private final StringProperty releasedTo;
	private final StringProperty remarks;
	private final StringProperty status;
	
	public StockOut() {
		this(0,null,null,null,null);
	}
	
	public StockOut(int stockOutID, LocalDate date, String releasedTo, String remarks, String status) {
		this.stockOutID = new SimpleIntegerProperty(stockOutID);
		this.date = new SimpleObjectProperty<LocalDate>(date);
		this.releasedTo = new SimpleStringProperty(releasedTo);
		this.remarks = new SimpleStringProperty(remarks);
		this.status = new SimpleStringProperty(status);
	}
	
	// Setter methods
	public void setStockOutID(int stockOutID) {
		this.stockOutID.set(stockOutID);
	}
	
	public void setDate(LocalDate date) {
		this.date.set(date);
	}
	
	public void setReleasedTo(String releasedTo) {
		this.releasedTo.set(releasedTo);
	}
	
	public void setRemarks(String remarks) {
		this.remarks.set(remarks);
	}
	
	public void setStatus(String status) {
		this.status.set(status);
	}
	
	// Getter methods
	public int getStockOutID() {
		return stockOutID.get();
	}
	
	public IntegerProperty stockOutIDProperty() {
		return stockOutID;
	}
	
	public LocalDate getDate() {
		return date.get();
	}
	
	public ObjectProperty<LocalDate> dateProperty(){
		return date;
	}
	
	public String getReleasedTo() {
		return releasedTo.get();
	}
	
	public StringProperty releasedToProperty() {
		return releasedTo;
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
		return "(NULL,'"+date.get()+"','"+releasedTo.get()+"','"+remarks.get()+"','"+status.get()+"')";
	}
}
