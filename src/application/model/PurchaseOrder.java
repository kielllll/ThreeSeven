package application.model;

import java.time.LocalDate;

import application.DAO.SupplierDAOImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PurchaseOrder {
	private final IntegerProperty purchaseID;
	private final ObjectProperty<LocalDate> purchaseDate;
	private final StringProperty supplier;
	private final ObjectProperty<LocalDate> dateNeeded;
	private final StringProperty status;
	
	public PurchaseOrder() {
		this(0,null,null,null,null);
	}
	
	public PurchaseOrder(int purchaseID, LocalDate purchaseDate, String supplier, LocalDate dateNeeded, String status) {
		this.purchaseID = new SimpleIntegerProperty(purchaseID);
		this.purchaseDate = new SimpleObjectProperty<LocalDate>(purchaseDate);
		this.supplier = new SimpleStringProperty(supplier);
		this.dateNeeded = new SimpleObjectProperty<LocalDate>(dateNeeded);
		this.status = new SimpleStringProperty(status);
	}
	
	// Setter methods
	public void setPurchaseID(int purchaseID) {
		this.purchaseID.set(purchaseID);
	}
	
	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate.set(purchaseDate);
	}
	
	public void setSupplier(String supplier) {
		this.supplier.set(supplier);
	}
	
	public void setDateNeeded(LocalDate dateNeeded) {
		this.dateNeeded.set(dateNeeded);
	}
	
	public void setStatus(String status) {
		this.status.set(status);
	}
	
	// Getter methods
	public int getPurchaseID() {
		return purchaseID.get();
	}
	
	public IntegerProperty purchaseIDProperty() {
		return purchaseID;
	}
	
	public LocalDate getPurchaseDate() {
		return purchaseDate.get();
	}
	
	public ObjectProperty<LocalDate> purchaseDateProperty() {
		return purchaseDate;
	}
	
	public String getSupplier() {
		return supplier.get();
	}
	
	public StringProperty supplierProperty() {
		return supplier;
	}
	
	public LocalDate getDateNeeded() {
		return dateNeeded.get();
	}
	
	public ObjectProperty<LocalDate> dateNeededProperty() {
		return dateNeeded;
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
									.filter(s->s.getName().equals(supplier.get()))
									.findFirst()
									.get()
									.getSupplierID();
		return "(NULL,'"+purchaseDate.get()+"',"+supplierID+",'"+dateNeeded.get()+"','"+status.get()+"')";
	}
}
