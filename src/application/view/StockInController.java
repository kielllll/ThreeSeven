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
import application.DAO.StockDAOImpl;
import application.DAO.StockInDAOImpl;
import application.DAO.StockInDetailsDAOImpl;
import application.DAO.StockInWithPODAOImpl;
import application.DAO.SupplierDAOImpl;
import application.model.Item;
import application.model.StockIn;
import application.model.StockInDetails;
import application.model.StockInWithPO;
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

public class StockInController implements Initializable {
	
	@FXML
    private JFXTextField txtStockInID;
    @FXML
    private JFXDatePicker dpStockIn;
    @FXML
    private JFXComboBox<String> cbSupplier;
    @FXML
    private JFXTextField txtInvoice;
    @FXML
    private JFXDatePicker dpInvoice;
    @FXML
    private JFXComboBox<Integer> cbPO;
    @FXML
    private JFXButton btnLoad;
    @FXML
    private JFXButton btnAddItem;
    @FXML
    private JFXButton btnRemoveItem;
    @FXML
    private JFXTextField txtCost;
    @FXML
    private TableView<StockInDetails> tblDetails;
    @FXML
    private TableColumn<StockInDetails, Integer> colItemID;
    @FXML
    private TableColumn<StockInDetails, String> colName;
    @FXML
    private TableColumn<StockInDetails, Integer> colQuantity;
    @FXML
    private TableColumn<StockInDetails, Double> colCost;
    @FXML
    private TableColumn<StockInDetails, Double> colSubTotal;
    @FXML
    private Label lblError;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXTextField txtSearch;
    @FXML
    private TableView<StockIn> tblStockIn;
    @FXML
    private TableColumn<StockIn, Integer> colStockInID;
    @FXML
    private TableColumn<StockIn, LocalDate> colTransactionDate;
    @FXML
    private TableColumn<StockIn, String> colSupplier;
    @FXML
    private TableColumn<StockIn, Integer> colInvoiceNumber;
    @FXML
    private TableColumn<StockIn, LocalDate> colInvoiceDate;
	
	private static StockInController instance = new StockInController();
	
	private ObservableList<StockIn> list = FXCollections.observableArrayList();
	
	private double cost = 0.00;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			Components.hideError(lblError);
			initTable();
			cost = 0;
			txtStockInID.setEditable(false);
			txtStockInID.setText(StockInDAOImpl.getInstance().getAll().size()+1+"");
			txtCost.setEditable(false);
			txtCost.setText(cost+"");
			
			dpStockIn.setValue(LocalDate.now());
			
			txtInvoice.textProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.matches("[0-9]*"))
					txtInvoice.setText(oldValue);
				if(newValue.length()>11)
					txtInvoice.setText(oldValue);
			});
			
			tblStockIn.setOnMouseClicked(s->{
				if(Bindings.isNotEmpty(tblStockIn.getItems()).get()) {
					StockIn stock = tblStockIn.getFocusModel().getFocusedItem();
					
					disableField(true);
					
					cbPO.getItems().clear();
					txtStockInID.setText(stock.getStockInID()+"");
					dpStockIn.setValue(stock.getStockDate());
					cbSupplier.setValue(stock.getSupplier());
					txtInvoice.setText(stock.getInvoiceNumber()+"");
					dpInvoice.setValue(stock.getInvoiceDate());
					
					List<StockInDetails> listDetails = StockInDetailsDAOImpl.getInstance()
																		.getAll()
																		.parallelStream()
																		.filter(i->i.getStockInID()==stock.getStockInID())
																		.sorted((o1,o2) -> (o1.getStockInID()>o2.getStockInID())?1:-1)
																		.collect(Collectors.toList());
					
					tblDetails.getItems().clear();
					listDetails.stream()
							.forEach(i->{
								i.setStockInID(ItemDAOImpl.getInstance()
														.getAll()
														.stream()
														.filter(item->item.getName().equals(i.getName()))
														.findFirst()
														.get()
														.getItemID());	
								tblDetails.getItems().add(i);
							});
					updateCost();
					
					boolean existing = StockInWithPODAOImpl.getInstance()
													.getAll()
													.stream()
													.filter(i->i.getStockID()==stock.getStockInID())
													.findFirst()
													.isPresent();
					
					if(existing) {
						cbPO.getItems().add(StockInWithPODAOImpl.getInstance()
														.getAll()
														.stream()
														.filter(i->i.getStockID()==stock.getStockInID())
														.findFirst()
														.get()
														.getPoID());
						
						cbPO.setValue(StockInWithPODAOImpl.getInstance()
														.getAll()
														.stream()
														.filter(i->i.getStockID()==stock.getStockInID())
														.findFirst()
														.get()
														.getPoID());
					}
				}
			});
			
			btnLoad.setOnAction(e->{
				if((cbPO.getValue()+"").equals("null"))
					Components.showError(lblError, "Error: No purchase order selected");
				else {
					int purchaseID = Integer.parseInt(cbPO.getValue()+"");
					
					tblDetails.getItems().clear();
					
					String supplier = PurchaseOrderDAOImpl.getInstance()
													.getAll()
													.stream()
													.filter(i->i.getPurchaseID()==purchaseID)
													.findFirst()
													.get()
													.getSupplier();
					
					cbSupplier.setValue(supplier);
					
					PODetailsDAOImpl.getInstance()
									.getAll()
									.stream()
									.filter(i->i.getPurchaseID()==purchaseID)
									.forEach(i->{
										tblDetails.getItems().add(new StockInDetails(i.getPurchaseID(), i.getName(), i.getQuantity(), i.getCost(), i.getSubTotal()));
									});
					
					updateCost();
				}
			});
			
			btnAddItem.setOnAction(e->{
				if((cbSupplier.getValue()+"").equals("null"))
					Components.showError(lblError, "Error: No supplier selected");
				else {
					Dialogs.getInstance().showSI_Item();
					SI_ItemsDialogController.getInstance().clear();
					SI_ItemsDialogController.getInstance().initList();
					Components.hideError(lblError);
				}
			});
			
			btnRemoveItem.setOnAction(e->{
				if(Bindings.isNotEmpty(tblDetails.getItems()).get()) {
					tblDetails.getItems().remove(tblDetails.getFocusModel().getFocusedItem());
					updateCost();
				} else Components.showError(lblError, "Error: No item selected");
			});
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields())
					Components.showError(lblError, "Error: Please fill up required fields");
				else {
					if(Bindings.isEmpty(tblDetails.getItems()).get())
						Components.showError(lblError, "Error: No item requested");
					else {
						if(Dialogs.getInstance().showConfirm("Stock In", "Confirm stock in transaction?")==0) {
							StockIn s = new StockIn(Integer.parseInt(txtStockInID.getText()), dpStockIn.getValue(), cbSupplier.getValue(), Integer.parseInt(txtInvoice.getText()), dpInvoice.getValue());
							
							if(!(cbPO.getValue()+"").equals("null")) {
								StockInDAOImpl.getInstance().insert(s);
								tblDetails.getItems()
										.parallelStream()
										.forEach(i->{
											i.setStockInID(s.getStockInID());
											StockInDetailsDAOImpl.getInstance().insert(i);
											if(Components.isEmpty(i.getName())) {
												ItemDAOImpl.getInstance().update("UPDATE items SET status='in stock' WHERE item_ID="+Components.getItemID(i.getName()));
											}
											StockDAOImpl.getInstance().update("UPDATE stocks SET quantity="+i.getQuantity()+" WHERE item_ID="+Components.getItemID(i.getName()));
										});
								StockInWithPO sp = new StockInWithPO(s.getStockInID(), cbPO.getValue());
								StockInWithPODAOImpl.getInstance().insert(sp);
								Sessions.getInstance().audit("Added a stock in transaction with ID = "+s.getStockInID()+" with PO ID = "+cbPO.getValue());
								PurchaseOrderDAOImpl.getInstance().update("UPDATE purchase_orders SET status='delivered' WHERE purchase_order_ID="+sp.getPoID());
							} else {
								StockInDAOImpl.getInstance().insert(s);
								tblDetails.getItems()
										.parallelStream()
										.forEach(i->{
											i.setStockInID(s.getStockInID());
											StockInDetailsDAOImpl.getInstance().insert(i);
											if(Components.isEmpty(i.getName())) {
												ItemDAOImpl.getInstance().update("UPDATE items SET status='in stock' WHERE item_ID="+Components.getItemID(i.getName()));
											}
											Item item = ItemDAOImpl.getInstance()
																.getAll()
																.stream()
																.filter(it -> it.getName().equals(i.getName()))
																.findFirst()
																.get();
											
											int qty = StockDAOImpl.getInstance()
															.getAll()
															.stream()
															.filter(st -> st.getItemID()==item.getItemID())
															.findFirst()
															.get()
															.getQuantity();
											
											StockDAOImpl.getInstance().update("UPDATE stocks SET quantity="+(i.getQuantity()+qty)+" WHERE item_ID="+Components.getItemID(i.getName()));
										});
								Sessions.getInstance().audit("Added a stock in transaction with ID = "+s.getStockInID());
							}
							
							clear();
							initList();
//							AddItemsController.getInstance().initList();
//							UpdateItemsController.getInstance().initList();	
						}
					}
				}
			});
			
			btnClear.setOnAction(e->{
				clear();
				initList();
				disableField(false);
			});
			
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initList() {
		try {
			list.clear();
			
			List<StockIn> sortedList = StockInDAOImpl.getInstance()
												.getAll()
												.stream()
												.sorted((o1,o2) -> (o1.getStockInID()<o2.getStockInID())?1:-1)
												.collect(Collectors.toList());
			
			sortedList.stream()
					.forEach(e->{
						list.add(e);
					});
			
			cbSupplier.getItems().clear();
			SupplierDAOImpl.getInstance()
						.getAll()
						.stream()
						.forEach(e->cbSupplier.getItems().add(e.getName()));
			
			cbPO.getItems().clear();
			PurchaseOrderDAOImpl.getInstance()
						.getAll()
						.stream()
						.forEach(e->{
							if(e.getStatus().equals("approved"))
								cbPO.getItems().add(e.getPurchaseID());
						});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initTable() {
		try {
			initList();
			
			colItemID.setCellValueFactory(cellData -> cellData.getValue().stockInIDProperty().asObject());
			colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
			colCost.setCellValueFactory(cellData -> cellData.getValue().costProperty().asObject());
			colSubTotal.setCellValueFactory(cellData -> cellData.getValue().subTotalProperty().asObject());
			
			colStockInID.setCellValueFactory(cellData -> cellData.getValue().stockInIDProperty().asObject());
			colTransactionDate.setCellValueFactory(cellData -> cellData.getValue().stockDateProperty());
			colSupplier.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
			colInvoiceNumber.setCellValueFactory(cellData -> cellData.getValue().invoiceNumberProperty().asObject());
			colInvoiceDate.setCellValueFactory(cellData -> cellData.getValue().invoiceDateProperty());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<StockIn> filteredData = new FilteredList<>(list, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(i -> {
	                // If filter text is empty, display all data.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (((i.getStockInID()+"").toLowerCase()).contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<StockIn> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblStockIn.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblStockIn.setItems(sortedData);
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
			
			tblDetails.getItems().add(new StockInDetails(i.getItemID(), i.getName(), quantity, i.getPrice(), (quantity*i.getPrice())));
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
			
			tblDetails.getItems()
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
	
	public boolean hasIncompleteFields() {
		if((dpStockIn.getValue()+"").equals("null")) return true;
		if((cbSupplier.getValue()+"").equals("null")) return true;
		if((txtInvoice.getText()+"").equals("")) return true;
		if((dpInvoice.getValue()+"").equals("null")) return true;
		return false;
	}
	
	public boolean isItemExisting(String name) {
		return tblDetails.getItems()
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
	
	public void updateCost() {
		cost = 0.00;
		tblDetails.getItems()
				  .stream()
				  .forEach(e->cost+=e.getSubTotal());
		txtCost.setText(cost+"");
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtStockInID.setText(StockInDAOImpl.getInstance().getAll().size()+1+"");
		txtCost.setText("0.0");
		txtInvoice.setText("");
		dpInvoice.setValue(null);
		dpStockIn.setValue(LocalDate.now());
		cbSupplier.setValue(null);
		cbPO.setValue(null);
		tblDetails.getItems().clear();
	}
	
	public void disableField(boolean choice) {
		if(choice) {
			dpStockIn.opacityProperty().set(1.0);
			cbSupplier.opacityProperty().set(1.0);
//			setStyle("-fx-opacity: 1;")
			dpInvoice.opacityProperty().set(1.0);
			cbPO.opacityProperty().set(1.0);
			dpStockIn.setDisable(true);
			cbSupplier.setDisable(true);
			dpInvoice.setDisable(true);
			cbPO.setDisable(true);
			btnLoad.setDisable(true);
			btnSave.setDisable(true);
			btnAddItem.setDisable(true);
			btnRemoveItem.setDisable(true);
			txtInvoice.setEditable(false);
		} else {
			dpStockIn.setDisable(false);
			cbSupplier.setDisable(false);
			dpInvoice.setDisable(false);
			cbPO.setDisable(false);
			btnLoad.setDisable(false);
			btnSave.setDisable(false);
			btnAddItem.setDisable(false);
			btnRemoveItem.setDisable(false);
			txtInvoice.setEditable(true);
		}
	}
	
	public static StockInController getInstance() {
		return instance;
	}
}
