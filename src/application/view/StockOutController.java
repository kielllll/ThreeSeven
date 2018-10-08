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
import application.DAO.StockDAOImpl;
import application.DAO.StockOutDAOImpl;
import application.DAO.StockOutDetailsDAOImpl;
import application.model.Item;
import application.model.StockOut;
import application.model.StockOutDetails;
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

public class StockOutController implements Initializable {
	@FXML
    private JFXTextField txtStockOutID;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private JFXTextField txtReleasedTo;
    @FXML
    private JFXComboBox<String> cbStatus;
    @FXML
    private JFXTextArea txtRemarks;
    @FXML
    private JFXButton btnAddItem;
    @FXML
    private JFXButton btnRemoveItem;
    @FXML
    private TableView<StockOutDetails> tblDetails;
    @FXML
    private TableColumn<StockOutDetails, Integer> colID;
    @FXML
    private TableColumn<StockOutDetails, String> colName;
    @FXML
    private TableColumn<StockOutDetails, Integer> colQuantity;
    @FXML
    private Label lblError;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXTextField txtSearch;
    @FXML
    private TableView<StockOut> tblStockOut;
    @FXML
    private TableColumn<StockOut, Integer> colStockOutID;
    @FXML
    private TableColumn<StockOut, LocalDate> colDate;
    @FXML
    private TableColumn<StockOut, String> colReleasedTo;
    @FXML
    private TableColumn<StockOut, String> colRemarks;
    @FXML
    private TableColumn<StockOut, String> colStatus;

    private static StockOutController instance = new StockOutController();
	
    private ObservableList<StockOut> list = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			Components.hideError(lblError);
			initTable();
			txtStockOutID.setEditable(false);
			txtStockOutID.setText((StockOutDAOImpl.getInstance().getAll().size()+1)+"");
			
			cbStatus.getItems().addAll("pending","approved","canceled");
			cbStatus.setValue("pending");
			
			dpDate.setValue(LocalDate.now());
			
			txtReleasedTo.textProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.matches("[\\sA-Za-z]*"))
					txtReleasedTo.setText(oldValue);
				if(newValue.length()>50)
					txtReleasedTo.setText(oldValue);
			});
			
			tblStockOut.setOnMouseClicked(e->{
				if(Bindings.isNotEmpty(tblStockOut.getItems()).get()) {
					StockOut s = tblStockOut.getFocusModel().getFocusedItem();
					
					disableField(true);
					
					txtStockOutID.setText(s.getStockOutID()+"");
					dpDate.setValue(s.getDate());
					txtReleasedTo.setText(s.getReleasedTo());
					cbStatus.setValue(s.getStatus());
					txtRemarks.setText(s.getRemarks());
					
					if(cbStatus.getValue().equals("pending"))
						cbStatus.setDisable(false);
					else cbStatus.setDisable(true);
					
					List<StockOutDetails> listDetails = StockOutDetailsDAOImpl.getInstance()
																		.getAll()
																		.stream()
																		.filter(i-> i.getStockOutID()==s.getStockOutID())
																		.sorted((o1,o2) -> (o1.getStockOutID()>o2.getStockOutID())?1:-1)
																		.collect(Collectors.toList());
					
					tblDetails.getItems().clear();
					listDetails.stream()
							.forEach(i->{
								i.setStockOutID(ItemDAOImpl.getInstance()
														.getAll()
														.stream()
														.filter(item->item.getName().equals(i.getName()))
														.findFirst()
														.get()
														.getItemID());
								
								tblDetails.getItems().add(i);
							});
				}
			});
			
			btnAddItem.setOnAction(e->{
				Dialogs.getInstance().showSO_Item();
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
						if(Dialogs.getInstance().showConfirm("Stock Out", "Confirm stock out transaction?")==0) {
							StockOut s = new StockOut(Integer.parseInt(txtStockOutID.getText()), dpDate.getValue(), txtReleasedTo.getText(), txtRemarks.getText(), cbStatus.getValue());
							
							boolean exists = StockOutDAOImpl.getInstance()
														.getAll()
														.stream()
														.filter(i->i.getStockOutID()==s.getStockOutID())
														.findFirst()
														.isPresent();
							
							if(exists) { // for updating the stock out
								String query = "UPDATE stock_out_transactions SET status='"+cbStatus.getValue()+"' WHERE stock_out_ID="+s.getStockOutID();
								StockOutDAOImpl.getInstance().update(query);
								if((cbStatus.getValue()+"").equals("approved")){
									tblDetails.getItems()
									.stream()
									.forEach(i->{
										int currentQty = StockDAOImpl.getInstance()
																.getAll()
																.stream()
																.filter(item -> item.getItemID()==i.getStockOutID())
																.findFirst()
																.get()
																.getQuantity();
										
										StockDAOImpl.getInstance().update("UPDATE stocks SET quantity="+(currentQty-i.getQuantity())+" WHERE item_ID="+Components.getItemID(i.getName()));
										if(Components.isEmpty(i.getName())) {
											ItemDAOImpl.getInstance().update("UPDATE items SET status='out of stock' WHERE item_ID="+Components.getItemID(i.getName()));
										}
									});
								}
								Sessions.getInstance().audit(s.getStatus()+" a stock out transaction with ID = "+s.getStockOutID());
							} else {
								if(cbStatus.getValue().equals("approved")) {
									StockOutDAOImpl.getInstance().insert(s);
									tblDetails.getItems()
											.stream()
											.forEach(i->{
												int currentQty = StockDAOImpl.getInstance()
														.getAll()
														.stream()
														.filter(item -> item.getItemID()==i.getStockOutID())
														.findFirst()
														.get()
														.getQuantity();
												
												i.setStockOutID(s.getStockOutID());
												StockOutDetailsDAOImpl.getInstance().insert(i);
												
												StockDAOImpl.getInstance().update("UPDATE stocks SET quantity="+(currentQty-i.getQuantity())+" WHERE item_ID="+Components.getItemID(i.getName()));
												if(Components.isEmpty(i.getName())) {
													ItemDAOImpl.getInstance().update("UPDATE items SET status='out of stock' WHERE item_ID="+Components.getItemID(i.getName()));
												}
											});
									Sessions.getInstance().audit(s.getStatus()+" a stock out transaction with ID = "+s.getStockOutID());
								} else {
									StockOutDAOImpl.getInstance().insert(s);
									tblDetails.getItems()
											.stream()
											.forEach(i->{
												i.setStockOutID(s.getStockOutID());
												StockOutDetailsDAOImpl.getInstance().insert(i);
											});
									Sessions.getInstance().audit("Requested a stock out transaction with ID = "+s.getStockOutID());
								}
								
							}
							
							disableField(false);
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
			
			List<StockOut> sortedList = StockOutDAOImpl.getInstance()
												.getAll()
												.stream()
												.sorted((o1, o2) -> (o1.getStockOutID()<o2.getStockOutID())?1:-1)
												.collect(Collectors.toList());
			
			sortedList.stream()
					.forEach(s->list.add(s));
												
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initTable() {
		try {
			initList();
			
			colID.setCellValueFactory(cellData -> cellData.getValue().stockOutIDProperty().asObject());
			colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
			
			colStockOutID.setCellValueFactory(cellData -> cellData.getValue().stockOutIDProperty().asObject());
			colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
			colReleasedTo.setCellValueFactory(cellData -> cellData.getValue().releasedToProperty());
			colRemarks.setCellValueFactory(cellData -> cellData.getValue().remarksProperty());
			colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<StockOut> filteredData = new FilteredList<>(list, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(i -> {
	                // If filter text is empty, display all data.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if ((i.getStockOutID()+"").contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<StockOut> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblStockOut.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblStockOut.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public boolean isItemExisting(String name) {
		return tblDetails.getItems()
				.parallelStream()
				.filter(p->p.getName().equals(name))
				.findFirst()
				.isPresent()?true:false;
	}
	
	public void add(int itemID, int quantity) {
		try {
			Item i = ItemDAOImpl.getInstance()
								.getAll()
								.stream()
								.filter(item -> item.getItemID()==itemID)
								.findFirst()
								.get();
			
			tblDetails.getItems().add(new StockOutDetails(i.getItemID(), i.getName(), quantity));
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
							}
						});
			
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public boolean hasIncompleteFields() {
		if((dpDate.getValue()+"").equals("null")) return true;
		if((txtReleasedTo.getText()+"").equals("")) return true;
		if((cbStatus.getValue()+"").equals("null")) return true;
		if((txtRemarks.getText()+"").equals("")) return true;
		return false;
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtStockOutID.setText(StockOutDAOImpl.getInstance().getAll().size()+1+"");
		dpDate.setValue(LocalDate.now());
		txtReleasedTo.setText("");
		cbStatus.setValue("pending");
		txtRemarks.setText("");
		tblDetails.getItems().clear();
	}
	
	public int getItemQty(String name) {
		if(Bindings.isNotEmpty(tblDetails.getItems()).get())
			if(isItemExisting(name))
				return tblDetails.getItems()
						.stream()
						.filter(i->i.getName().equals(name))
						.findFirst()
						.get()
						.getQuantity();
		return 0;
	}
	
	public void disableField(boolean choice) {
		if(choice) {
			dpDate.setDisable(true);
			txtReleasedTo.setEditable(false);
			txtRemarks.setEditable(false);
			dpDate.opacityProperty().set(1.0);
			cbStatus.opacityProperty().set(1.0);
			btnAddItem.setDisable(true);
			btnRemoveItem.setDisable(true);
		} else {
			dpDate.setDisable(false);
			txtReleasedTo.setEditable(true);
			txtRemarks.setEditable(true);
			btnAddItem.setDisable(false);
			btnRemoveItem.setDisable(false);
			if(Sessions.getInstance().getUser().getAccessType().equals("administrator"))
				cbStatus.setDisable(false);
		}
	}
	
	public static StockOutController getInstance() {
		return instance;
	}
}
