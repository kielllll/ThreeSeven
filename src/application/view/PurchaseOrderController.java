package application.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import application.DAO.ItemDAOImpl;
import application.DAO.PODetailsDAOImpl;
import application.DAO.PurchaseOrderDAOImpl;
import application.DAO.SupplierDAOImpl;
import application.model.Item;
import application.model.PODetails;
import application.model.PurchaseOrder;
import application.util.Authorize;
import application.util.Components;
import application.util.Dialogs;
import application.util.Sessions;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PurchaseOrderController implements Initializable {
	@FXML
    private JFXTextField txtPOID;
    @FXML
    private JFXDatePicker dpPO;
    @FXML
    private JFXComboBox<String> cbSupplier;
    @FXML
    private JFXDatePicker dpNeeded;
    @FXML
    private JFXComboBox<String> cbStatus;
    @FXML
    private JFXButton btnAddItem;
    @FXML
    private JFXButton btnRemoveItem;
    @FXML
    private JFXTextField txtCost;
    @FXML
    private TableView<PODetails> tblPODetails;
    @FXML
    private TableColumn<PODetails, Integer> colID;
    @FXML
    private TableColumn<PODetails, String> colName;
    @FXML
    private TableColumn<PODetails, Integer> colQuantity;
    @FXML
    private TableColumn<PODetails, Double> colCost;
    @FXML
    private TableColumn<PODetails, Double> colSubTotal;
    @FXML
    private Label lblError;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnRefresh;
    @FXML
    private JFXTextField txtSearch;
    @FXML
    private TableView<PurchaseOrder> tblPO;
    @FXML
    private TableColumn<PurchaseOrder, Integer> colPO;
    @FXML
    private TableColumn<PurchaseOrder, LocalDate> colDate;
    @FXML
    private TableColumn<PurchaseOrder, String> colSupplier;
    @FXML
    private TableColumn<PurchaseOrder, LocalDate> colNeeded;
    @FXML
    private TableColumn<PurchaseOrder, String> colStatus;

	private static PurchaseOrderController instance = new PurchaseOrderController();
	
	private ObservableList<PurchaseOrder> listPO = FXCollections.observableArrayList();
	private ObservableList<PODetails> listPODetails = FXCollections.observableArrayList();
	
	private double cost = 0.00;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			txtCost.setText(cost+"");
			txtPOID.setEditable(false);
			txtPOID.setText(PurchaseOrderDAOImpl.getInstance().getAll().size()+1+"");
			Components.hideError(lblError);
			
			dpPO.setValue(LocalDate.now());
			
			cbStatus.getItems().addAll("pending","approved","canceled");
			cbStatus.setValue("pending");
			authorize();
			initTable();
			
			tblPO.setOnMouseClicked(e->{
				if(Bindings.isNotEmpty(tblPO.getItems()).get()) {
					PurchaseOrder p = tblPO.getFocusModel().getFocusedItem();
					
					disableField(true);
					
					txtPOID.setText(p.getPurchaseID()+"");
					dpPO.setValue(p.getPurchaseDate());
					cbSupplier.setValue(p.getSupplier());
					dpNeeded.setValue(p.getDateNeeded());
					cbStatus.setValue(p.getStatus());
					
					if(cbStatus.getValue().equals("delivered"))
						cbStatus.setDisable(true);
					else cbStatus.setDisable(false);
					List<PODetails> listDetails = PODetailsDAOImpl.getInstance()
																.getAll()
																.parallelStream()
																.filter(i->i.getPurchaseID()==p.getPurchaseID())
																.sorted((o1,o2) -> (o1.getPurchaseID()>o2.getPurchaseID())?1:-1)
																.collect(Collectors.toList());
					
					listPODetails.clear();
					listDetails.stream()
							.forEach(i->listPODetails.add(i));
					updateCost();
				}
			});
			
			cbSupplier.focusedProperty().addListener((observable, oldValue, newValue) -> {
				if(newValue) {
					Components.hideError(lblError);
				}
			});
			
			btnAddItem.setOnAction(e->{
				if((getSupplier()+"").equals("null"))
					Components.showError(lblError, "Error: No supplier selected");
				else {
					Dialogs.getInstance().showPO_Item();
					PO_ItemsDialogController.getInstance().clear();
					PO_ItemsDialogController.getInstance().initList();
					Components.hideError(lblError);
				}
			});
			
			btnRemoveItem.setOnAction(e->{
				if(Bindings.isNotEmpty(tblPODetails.getItems()).get()) {
					tblPODetails.getItems().remove(tblPODetails.getFocusModel().getFocusedItem());
					updateCost();
				} else Components.showError(lblError, "Error: No item selected");
			});
			
			btnRemoveItem.setOnMouseReleased(e->{
				if(Bindings.isEmpty(tblPODetails.getItems()).get()) {
					disableCBSupplier(false);
				}
			});
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields())
					Components.showError(lblError, "Error: Please fill up required fields.");
				else {
					if(Bindings.isEmpty(tblPODetails.getItems()).get())
						Components.showError(lblError, "Error: No item requested");
					else {
						if(Dialogs.getInstance().showConfirm("Purchase Order", "Confirm request on purchase order?")==0) {
							PurchaseOrder po = new PurchaseOrder(Integer.parseInt(txtPOID.getText()), dpPO.getValue(), cbSupplier.getValue(), dpNeeded.getValue(), cbStatus.getValue());
							
							boolean exists = PurchaseOrderDAOImpl.getInstance()
													.getAll()
													.stream()
													.filter(p->p.getPurchaseID()==po.getPurchaseID())
													.findFirst()
													.isPresent();
							
							if(exists) { // for updating the purchase order
								Sessions.getInstance().audit(po.getStatus()+" purchase order #"+po.getPurchaseID());
								PurchaseOrderDAOImpl.getInstance().update("UPDATE purchase_orders SET status='"+cbStatus.getValue()+"' WHERE purchase_order_ID="+po.getPurchaseID());
							} else {
								PurchaseOrderDAOImpl.getInstance().insert(po);
								
								tblPODetails.getItems()
										.parallelStream()
										.forEach(p->{
											p.setPurchaseID(po.getPurchaseID());
											PODetailsDAOImpl.getInstance().insert(p);
										});
								
								Sessions.getInstance().audit("Requested purchase order #"+po.getPurchaseID());
							}
							clear();
							initList();
						}
					}
				}
			});
			
			btnClear.setOnAction(e->{
				clear();
				updateCost();
				disableField(false);
			});
			
			btnRefresh.setOnAction(e->{
				initList();
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initList() {
		try {
			if(!cbSupplier.getItems().isEmpty())
				cbSupplier.getItems().clear();
			SupplierDAOImpl.getInstance()
						.getAll()
						.stream()
						.forEach(s->{
							if(s.getStatus().equals("active"))
								cbSupplier.getItems().add(s.getName());
						});
			
			listPO.clear();
			
			// Sorts the data from the database
			List<PurchaseOrder> sortedList = PurchaseOrderDAOImpl.getInstance()
															.getAll()
															.parallelStream()
															.sorted((o1,o2) -> ((o1.getPurchaseID()<o2.getPurchaseID())?1:-1))
															.collect(Collectors.toList());
			
			sortedList.stream()
					.forEach(e->{
						listPO.add(e);
					});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initTable() {
		try {
			initList();
			
			colID.setCellValueFactory(cellData -> cellData.getValue().purchaseIDProperty().asObject());
			colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
			colCost.setCellValueFactory(cellData -> cellData.getValue().costProperty().asObject());
			colSubTotal.setCellValueFactory(cellData -> cellData.getValue().subTotalProperty().asObject());
			
			tblPODetails.setItems(listPODetails);
			
			colPO.setCellValueFactory(cellData -> cellData.getValue().purchaseIDProperty().asObject());
			colDate.setCellValueFactory(cellData -> cellData.getValue().purchaseDateProperty());
			colSupplier.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
			colNeeded.setCellValueFactory(cellData -> cellData.getValue().dateNeededProperty());
			colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<PurchaseOrder> filteredData = new FilteredList<>(listPO, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(i -> {
	                // If filter text is empty, display all data.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (((i.getPurchaseID()+"").toLowerCase()).contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<PurchaseOrder> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblPO.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblPO.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void add(int itemID, int quantity) {
		try {
			Item i = ItemDAOImpl.getInstance()
								.getAll()
								.parallelStream()
								.filter(item -> item.getItemID()==itemID)
								.findFirst()
								.get();
			
			tblPODetails.getItems().add(new PODetails(i.getItemID(), i.getName(), quantity, i.getPrice(), (quantity*i.getPrice())));
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void update(int itemID, int quantity) {
		try {
			Item i = ItemDAOImpl.getInstance()
					.getAll()
					.parallelStream()
					.filter(item -> item.getItemID()==itemID)
					.findFirst()
					.get();
			
			tblPODetails.getItems()
						.parallelStream()
						.forEach(p->{
							if(p.getName().equals(i.getName())) {
								p.setQuantity(p.getQuantity()+quantity);
								p.setSubTotal(p.getQuantity()*p.getCost());
							}
						});
			
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public boolean isItemExisting(String name) {
		return tblPODetails.getItems()
				.parallelStream()
				.filter(p->p.getName().equals(name))
				.findFirst()
				.isPresent()?true:false;
	}
	
	public String getSupplier() {
		return cbSupplier.getValue();
	}
	
	public void disableCBSupplier(boolean bool) {
		cbSupplier.setDisable(bool);
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtPOID.setText(PurchaseOrderDAOImpl.getInstance().getAll().size()+1+"");
		txtCost.setText("0.0");
		dpPO.setValue(LocalDate.now());
		cbStatus.setValue("pending");
		cbSupplier.setValue(null);
		dpNeeded.setValue(null);
		tblPODetails.getItems().clear();
		disableField(false);
	}
	
	public void disableField(boolean choice) {
		if(choice) {
//			cbSupplier.setEditable(false);
			cbStatus.setDisable(true);
			cbSupplier.setDisable(true);
			dpPO.setDisable(true);
			dpNeeded.setDisable(true);
			btnAddItem.setDisable(true);
			btnRemoveItem.setDisable(true);
			cbSupplier.opacityProperty().set(1.0);
			dpPO.opacityProperty().set(1.0);
			dpNeeded.opacityProperty().set(1.0);
		} else {
//			cbSupplier.setEditable(true);
			cbSupplier.setDisable(false);
			dpPO.setDisable(false);
			dpNeeded.setDisable(false);
			btnAddItem.setDisable(false);
			btnRemoveItem.setDisable(false);
			if(Sessions.getInstance().getUser().getAccessType().equals("administrator")) {
				cbStatus.setDisable(false);
			}
		}
			
	}
	
	public void updateCost() {
		cost = 0.0;
		tblPODetails.getItems()
					.parallelStream()
					.forEach(e->{
						cost+=e.getSubTotal();
					});
		txtCost.setText(cost+"");
	}
	
	public boolean hasIncompleteFields() {
		if((dpPO.getValue()+"").equals("null")) return true;
		if((cbSupplier.getValue()+"").equals("null")) return true;
		if((dpNeeded.getValue()+"").equals("null")) return true;
		if((cbStatus.getValue()+"").equals("null")) return true;
		return false;
	}
	
	public void authorize() {
		if(!Sessions.getInstance().getUser().getAccessType().equals("Master")) {
			if(Authorize.getInstance().isEmployee(Sessions.getInstance().getUser().getAccessType())) {
				cbStatus.setValue("pending");
				cbStatus.setDisable(true);
			}
		}
	}
	
	public static PurchaseOrderController getInstance() {
		return instance;
	}
}
