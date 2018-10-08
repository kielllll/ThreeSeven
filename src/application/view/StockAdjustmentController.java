package application.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import application.DAO.ItemDAOImpl;
import application.DAO.StockAdjustmentDAOImpl;
import application.DAO.StockAdjustmentDetailsDAOImpl;
import application.DAO.StockDAOImpl;
import application.model.Item;
import application.model.StockAdjustment;
import application.model.StockAdjustmentDetails;
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

public class StockAdjustmentController implements Initializable {
	@FXML
    private JFXTextField txtStockAdjID;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private JFXTextArea txtRemarks;
    @FXML
    private JFXComboBox<String> cbStatus;
    @FXML
    private JFXButton btnAddItem;
    @FXML
    private JFXButton btnRemoveItem;
    @FXML
    private TableView<StockAdjustmentDetails> tblDetails;
    @FXML
    private TableColumn<StockAdjustmentDetails, Integer> colID;
    @FXML
    private TableColumn<StockAdjustmentDetails, String> colName;
    @FXML
    private TableColumn<StockAdjustmentDetails, Integer> colSystemQty;
    @FXML
    private TableColumn<StockAdjustmentDetails, Integer> colNewQty;
    @FXML
    private Label lblError;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXTextField txtSearch;
    @FXML
    private TableView<StockAdjustment> tblStockAdjustment;
    @FXML
    private TableColumn<StockAdjustment, Integer> colStockAdjID;
    @FXML
    private TableColumn<StockAdjustment, LocalDate> colDate;
    @FXML
    private TableColumn<StockAdjustment, String> colRemarks;
    @FXML
    private TableColumn<StockAdjustment, String> colStatus;
	
	private static StockAdjustmentController instance = new StockAdjustmentController();
	private ObservableList<StockAdjustment> list = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			Components.hideError(lblError);
			initTable();
			txtStockAdjID.setEditable(false);
			txtStockAdjID.setText(StockAdjustmentDAOImpl.getInstance().getAll().size()+1+"");
			
			cbStatus.getItems().addAll("pending","approved","canceled");
			cbStatus.setValue("pending");
			
			dpDate.setValue(LocalDate.now());
			
			tblStockAdjustment.setOnMouseClicked(e->{
				if(Bindings.isNotEmpty(tblStockAdjustment.getItems()).get()) {
					StockAdjustment s = tblStockAdjustment.getFocusModel().getFocusedItem();
					
					disableField(true);
					
					txtStockAdjID.setText(s.getStockAdjID()+"");
					dpDate.setValue(s.getDate());
					txtRemarks.setText(s.getRemarks());
					cbStatus.setValue(s.getStatus());
					
					if(cbStatus.getValue().equals("pending"))
						cbStatus.setDisable(false);
					else cbStatus.setDisable(true);
					
					List<StockAdjustmentDetails> listDetails = StockAdjustmentDetailsDAOImpl.getInstance()
																					.getAll()
																					.stream()
																					.filter(i-> i.getStockAdjID()==s.getStockAdjID())
																					.sorted((o1,o2) -> (o1.getStockAdjID()>o2.getStockAdjID())?1:-1)
																					.collect(Collectors.toList());
					
					tblDetails.getItems().clear();
					listDetails.stream()
							.forEach(i->{
								i.setStockAdjID(ItemDAOImpl.getInstance()
														.getAll()
														.stream()
														.filter(item -> item.getName().equals(i.getName()))
														.findFirst()
														.get()
														.getItemID());
								
								tblDetails.getItems().add(i);
							});
				}
			});
			
			btnAddItem.setOnAction(e->{
				Dialogs.getInstance().showSA_Item();
			});
			
			btnRemoveItem.setOnAction(e->{
				if(Bindings.isNotEmpty(tblDetails.getItems()).get()) {
					tblDetails.getItems().remove(tblDetails.getFocusModel().getFocusedItem());
				}
			});
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields())
					Components.showError(lblError, "Error: Please fill up required fields");
				else {
					if(Bindings.isEmpty(tblDetails.getItems()).get())
						Components.showError(lblError, "Error: No item requested");
					else {
						if(Dialogs.getInstance().showConfirm("Stock Adjustment", "Confirm stock adjustment transaction?")==0) {
							StockAdjustment s = new StockAdjustment(Integer.parseInt(txtStockAdjID.getText()), dpDate.getValue(), txtRemarks.getText(), cbStatus.getValue());
							
							boolean exists = StockAdjustmentDAOImpl.getInstance()
															.getAll()
															.stream()
															.filter(i->i.getStockAdjID()==s.getStockAdjID())
															.findFirst()
															.isPresent();
							
							if(exists) {
								String query = "UPDATE stock_adjustment_transactions SET status='"+cbStatus.getValue()+"' WHERE stock_adjustment_ID="+s.getStockAdjID();
								StockAdjustmentDAOImpl.getInstance().update(query);
								if((cbStatus.getValue()+"").equals("approved")) {
									tblDetails.getItems()
									.stream()
									.forEach(i->{
										StockDAOImpl.getInstance().update("UPDATE stocks SET quantity="+i.getNewQty()+" WHERE item_ID="+getItemID(i.getName()));
										if(isEmpty(i.getName())) {
											ItemDAOImpl.getInstance().update("UPDATE items SET status='out of stock' WHERE item_ID="+getItemID(i.getName()));
										} else ItemDAOImpl.getInstance().update("UPDATE items SET status='in stock' WHERE item_ID="+getItemID(i.getName()));
									});
								}
								Sessions.getInstance().audit(s.getStatus()+" a stock adjustment transaction with ID = "+s.getStockAdjID());
							} else {
								if(cbStatus.getValue().equals("approved")) {
									StockAdjustmentDAOImpl.getInstance().insert(s);
									tblDetails.getItems()
									.stream()
									.forEach(i->{
										StockDAOImpl.getInstance().update("UPDATE stocks SET quantity="+i.getNewQty()+" WHERE item_ID="+getItemID(i.getName()));
										if(isEmpty(i.getName())) {
											ItemDAOImpl.getInstance().update("UPDATE items SET status='out of stock' WHERE item_ID="+getItemID(i.getName()));
										} else ItemDAOImpl.getInstance().update("UPDATE items SET status='in stock' WHERE item_ID="+getItemID(i.getName()));
										i.setStockAdjID(s.getStockAdjID());
										StockAdjustmentDetailsDAOImpl.getInstance().insert(i);
									});
									Sessions.getInstance().audit(s.getStatus()+" a stock adjustment transaction with ID = "+s.getStockAdjID());
								} else {
									StockAdjustmentDAOImpl.getInstance().insert(s);
									tblDetails.getItems()
											.stream()
											.forEach(i->{
												i.setStockAdjID(s.getStockAdjID());
												StockAdjustmentDetailsDAOImpl.getInstance().insert(i);
											});
									Sessions.getInstance().audit("Requested a stock adjustment transaction with ID = "+s.getStockAdjID());
								}
							}
							
							clear();
							initList();
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
			
			List<StockAdjustment> sortedList = StockAdjustmentDAOImpl.getInstance()
																.getAll()
																.stream()
																.sorted((o1,o2) -> (o1.getStockAdjID()<o2.getStockAdjID())?1:-1)
																.collect(Collectors.toList());
			sortedList.stream()
					.forEach(e->{
						list.add(e);
					});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public int getItemID(String name) {
		return ItemDAOImpl.getInstance()
						.getAll()
						.stream()
						.filter(i->i.getName().equals(name))
						.findFirst()
						.get()
						.getItemID();
	}
	
	public boolean isEmpty(String name) {
		return (StockDAOImpl.getInstance()
							.getAll()
							.stream()
							.filter(i->i.getName().equals(name))
							.findFirst()
							.get()
							.getQuantity()==0)?true:false;
	}
	
	public void initTable() {
		try {
			initList();
			
			colID.setCellValueFactory(cellData -> cellData.getValue().stockAdjIDProperty().asObject());
			colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			colSystemQty.setCellValueFactory(cellData -> cellData.getValue().sysQtyProperty().asObject());
			colNewQty.setCellValueFactory(cellData -> cellData.getValue().newQtyProperty().asObject());
			
			colStockAdjID.setCellValueFactory(cellData -> cellData.getValue().stockAdjIDProperty().asObject());
			colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
			colRemarks.setCellValueFactory(cellData -> cellData.getValue().remarksProperty());
			colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<StockAdjustment> filteredData = new FilteredList<>(list, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(i -> {
	                // If filter text is empty, display all data.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (((i.getStockAdjID()+"").toLowerCase()).contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<StockAdjustment> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblStockAdjustment.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblStockAdjustment.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public boolean isItemExisting(String name) {
		return tblDetails.getItems()
						.stream()
						.filter(i->i.getName().equals(name))
						.findFirst()
						.isPresent();
	}
	
	public int getItemQty(String name) {
		if(Bindings.isNotEmpty(tblDetails.getItems()).get())
			if(isItemExisting(name))
				return tblDetails.getItems()
						.stream()
						.filter(i->i.getName().equals(name))
						.findFirst()
						.get()
						.getNewQty();
		return 0;
	}
	
	public void add(int itemID, int quantity) {
		try {
			Item i = ItemDAOImpl.getInstance()
								.getAll()
								.stream()
								.filter(item -> item.getItemID()==itemID)
								.findFirst()
								.get();
			
			int sysQty = StockDAOImpl.getInstance()
							.getAll()
							.stream()
							.filter(item -> item.getItemID()==i.getItemID())
							.findFirst()
							.get()
							.getQuantity();
			
			tblDetails.getItems().add(new StockAdjustmentDetails(i.getItemID(), i.getName(), sysQty, quantity));
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void update(int itemID, int quantity) {
		try {
			Item i = ItemDAOImpl.getInstance()
					.getAll()
					.stream()
					.filter(item -> item.getItemID()==itemID)
					.findFirst()
					.get();
			
			tblDetails.getItems()
						.stream()
						.forEach(p->{
							if(p.getName().equals(i.getName())) {
								p.setNewQty(quantity);
							}
						});
			
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public boolean hasIncompleteFields() {
		if((dpDate.getValue()+"").equals("null")) return true;
		if((txtRemarks.getText()+"").equals("")) return true;
		if((cbStatus.getValue()+"").equals("null")) return true;
		return false;
	}
	
	public void disableField(boolean choice) {
		if(choice) {
			dpDate.setDisable(true);
			txtRemarks.setEditable(false);
			btnAddItem.setDisable(true);
			btnRemoveItem.setDisable(true);
			cbStatus.setDisable(true);
			dpDate.opacityProperty().set(1.0);
		} else {
			dpDate.setDisable(false);
			txtRemarks.setEditable(true);
			btnAddItem.setDisable(false);
			btnRemoveItem.setDisable(false);
			if(Sessions.getInstance().getUser().getAccessType().equals("administrator"))
				cbStatus.setDisable(false);
		}
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtStockAdjID.setText(StockAdjustmentDAOImpl.getInstance().getAll().size()+1+"");
		dpDate.setValue(LocalDate.now());
		txtRemarks.setText("");
		cbStatus.setValue("pending");
		tblDetails.getItems().clear();
	}
	
	public static StockAdjustmentController getInstance() {
		return instance;
	}
}
