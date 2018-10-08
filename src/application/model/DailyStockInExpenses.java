package application.model;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DailyStockInExpenses {
	private final IntegerProperty stockInID;
	private final ObjectProperty<LocalDate> date;
	private final StringProperty supplier;
	private final IntegerProperty invoiceNumber;
	private final ObjectProperty<LocalDate> invoiceDate;
	private final DoubleProperty amount;
	
	public DailyStockInExpenses() {
		this(0,null,null,0,null,0.00);
	}
	
	public DailyStockInExpenses(int stockInID, LocalDate date, String supplier, int invoiceNumber, LocalDate invoiceDate, double amount) {
		this.stockInID = new SimpleIntegerProperty(stockInID);
		this.date = new SimpleObjectProperty<LocalDate>(date);
		this.supplier = new SimpleStringProperty(supplier);
		this.invoiceNumber = new SimpleIntegerProperty(invoiceNumber);
		this.invoiceDate = new SimpleObjectProperty<LocalDate>(invoiceDate);
		this.amount = new SimpleDoubleProperty(amount);
	}
	
	// Setter methods
	public void setStockInID(int stockInID) {
		this.stockInID.set(stockInID);
	}
	
	public void setDate(LocalDate date) {
		this.date.set(date);
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
	
	public void setAmount(double amount) {
		this.amount.set(amount);
	}
	
	// Getter methods
	public int getStockInID() {
		return stockInID.get();
	}
	
	public IntegerProperty stockInIDProperty() {
		return stockInID;
	}
	
	public LocalDate getDate() {
		return date.get();
	}
	
	public ObjectProperty<LocalDate> dateProperty(){
		return date;
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
	
	public LocalDate getInvoiceDate(){
		return invoiceDate.get();
	}
	
	public ObjectProperty<LocalDate> invoiceDateProperty(){
		return invoiceDate;
	}
	
	public double getAmount() {
		return amount.get();
	}
	
	public DoubleProperty amountProperty() {
		return amount;
	}
	
	@Override
	public String toString() {
		return "";
	}
}
