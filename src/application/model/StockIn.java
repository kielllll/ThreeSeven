package application.model;

import java.time.LocalDate;

import application.DAO.SupplierDAOImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StockIn {
	private final IntegerProperty stockInID;
	private final ObjectProperty<LocalDate> stockDate;
	private final StringProperty supplier;
	private final IntegerProperty invoiceNumber;
	private final ObjectProperty<LocalDate> invoiceDate;
	
	public StockIn() {
		this(0,null,null,0,null);
	}
	
	public StockIn(int stockInID, LocalDate stockDate, String supplier, int invoiceNumber, LocalDate invoiceDate) {
		this.stockInID = new SimpleIntegerProperty(stockInID);
		this.stockDate = new SimpleObjectProperty<LocalDate>(stockDate);
		this.supplier = new SimpleStringProperty(supplier);
		this.invoiceNumber = new SimpleIntegerProperty(invoiceNumber);
		this.invoiceDate = new SimpleObjectProperty<LocalDate>(invoiceDate);
	}
	
	// Setter methods
	public void setStockInID(int stockInID) {
		this.stockInID.set(stockInID);
	}
	
	public void setStockDate(LocalDate stockDate) {
		this.stockDate.set(stockDate);
	}
	
	public void setSupplier(String supplier) {
		this.supplier.set(supplier);
	}
	
	public void setInvoiceNumber(int invoiceNumber) {
		this.invoiceNumber.set(invoiceNumber);
	}
	
	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate.set(invoiceDate);
	}
	
	// Getter methods
	public int getStockInID() {
		return stockInID.get();
	}
	
	public IntegerProperty stockInIDProperty() {
		return stockInID;
	}
	
	public LocalDate getStockDate() {
		return stockDate.get();
	}
	
	public ObjectProperty<LocalDate> stockDateProperty(){
		return stockDate;
	}
	
	public String getSupplier() {
		return supplier.get();
	}
	
	public StringProperty supplierProperty() {
		return supplier;
	}
	
	public int getInvoiceNumber() {
		return invoiceNumber.get();
	}
	
	public IntegerProperty invoiceNumberProperty() {
		return invoiceNumber;
	}
	
	public LocalDate getInvoiceDate() {
		return invoiceDate.get();
	}
	
	public ObjectProperty<LocalDate> invoiceDateProperty(){
		return invoiceDate;
	}
	
	@Override
	public String toString() {
		int supplierID = SupplierDAOImpl.getInstance()
										.getAll()
										.parallelStream()
										.filter(e -> e.getName().equals(supplier.get()))
										.findFirst()
										.get()
										.getSupplierID();
		
		return "(NULL,'"+stockDate.get()+"',"+supplierID+","+invoiceNumber.get()+",'"+invoiceDate.get()+"')";
	}
}
