package application.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import application.DAO.ItemDAOImpl;
import application.DAO.SupplierDAOImpl;
import application.DAO.UnitDAOImpl;
import application.model.Item;
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

public class UpdateItemsController implements Initializable {
	@FXML
	private JFXTextField txtItemID;
	@FXML
	private JFXTextField txtName;
	@FXML
	private JFXComboBox<String> cbSupplier;
	@FXML
	private JFXTextArea txtDescription;
	@FXML
	private JFXTextField txtPrice;
	@FXML
	private JFXComboBox<String> cbUnit;
	@FXML
	private JFXButton btnAdd;
	@FXML
	private JFXComboBox<String> cbStatus;
	@FXML
	private Label lblError;
	@FXML
	private JFXButton btnSave;
	@FXML
	private JFXButton btnClear;
	
	@FXML
	private JFXButton btnRefresh;
	@FXML
	private JFXComboBox<String> cbSearch;
	@FXML
	private JFXTextField txtSearch;
	@FXML
	private TableView<Item> tblItem;
	@FXML
	private TableColumn<Item, Integer> colItemID;
	@FXML
	private TableColumn<Item, String> colName;
	@FXML
	private TableColumn<Item, String> colSupplier;
	@FXML
	private TableColumn<Item, String> colDescription;
	@FXML
	private TableColumn<Item, Double> colPrice;
	@FXML
	private TableColumn<Item, String> colUnit;
	@FXML
	private TableColumn<Item, Integer> colQuantity;
	@FXML
	private TableColumn<Item, String> colStatus;
	@FXML
	private JFXButton btnReport;
	
	private ObservableList<Item> list = FXCollections.observableArrayList();
	private static UpdateItemsController instance = new UpdateItemsController();
	private String tempName = "";
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			txtItemID.setEditable(false);
			Components.hideError(lblError);
			
			cbSearch.getItems().addAll("Name","Supplier","Description","Unit","Status");
			cbStatus.getItems().addAll("in stock", "out of stock");
			
			initTable();
			
			tblItem.setOnMouseClicked(e->{
				if(Bindings.isNotEmpty(tblItem.getItems()).get()) {
					Item i = tblItem.getFocusModel().getFocusedItem();
					
					tempName = i.getName();
					txtItemID.setText(i.getItemID()+"");
					txtName.setText(i.getName());
					cbSupplier.setValue(i.getSupplier());
					txtDescription.setText(i.getDescription());
					txtPrice.setText(i.getPrice()+"");
					cbUnit.setValue(i.getUnit());
					cbStatus.setValue(i.getStatus());
				}
			});
			
			
			txtName.textProperty().addListener((observable, oldValue, newValue) -> {
				if(newValue.length()>50)
					txtName.setText(oldValue);
			});
			
			txtPrice.textProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.matches("\\d*(\\.\\d{0,2})?"))
					txtPrice.setText(oldValue);
			});
			
			btnAdd.setOnAction(e->{
				Dialogs.getInstance().showUnit();
			});
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields())
					Components.showError(lblError,"Error: Please fill up required fields");
				else {
					if(isItemExisting(txtName.getText()))
						Components.showError(lblError, "Error: Item name already exists");
					else {
						Item i = new Item(Integer.parseInt(txtItemID.getText()), txtName.getText(), cbSupplier.getValue(), txtDescription.getText(), Double.parseDouble(txtPrice.getText()), cbUnit.getValue(), 0, cbStatus.getValue());
						
						int supplierID = SupplierDAOImpl.getInstance()
														.getAll()
														.parallelStream()
														.filter(s->s.getName().equals(i.getSupplier()))
														.findFirst()
														.get()
														.getSupplierID();
						
						int unitID = UnitDAOImpl.getInstance()
												.getAll()
												.parallelStream()
												.filter(u->u.getDescription().equals(i.getUnit()))
												.findFirst()
												.get()
												.getUnitID();
						
						String query = "UPDATE items SET name='"+i.getName()+"', supplier_ID="+supplierID+", description='"+i.getDescription()+"', price="+i.getPrice()+", unit_ID="+unitID+", status='"+i.getStatus()+"' WHERE item_ID="+i.getItemID();
						ItemDAOImpl.getInstance().update(query);
						Sessions.getInstance().audit("Updated an item: "+i.getItemID()+" - "+i.getName());
						initList();
						AddItemsController.getInstance().initList();
						clear();
					}
				}
			});
			
			btnClear.setOnAction(e->{
				clear();
			});
			
			btnRefresh.setOnAction(e->{
				initList();
			});
			
			btnReport.setOnAction(e->{
				Dialogs.getInstance().showReports_Item();
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initList() {
		try {
			cbSupplier.getItems().clear();
			SupplierDAOImpl.getInstance()
						.getAll()
						.stream()
						.forEach(s->{
							if(s.getStatus().equals("active"))
								cbSupplier.getItems().add(s.getName());
						});
			
			cbUnit.getItems().clear();
			UnitDAOImpl.getInstance()
					.getAll()
					.stream()
					.forEach(u->{
						cbUnit.getItems().add(u.getDescription());
					});
			
			list.clear();
			
			// Sorts the data from the database
			List<Item> sortedList = ItemDAOImpl.getInstance()
											.getAll()
											.parallelStream()
											.sorted((o1, o2) -> (o1.getItemID()>o2.getItemID())?1:-1)
											.collect(Collectors.toList());
			
			// Adds the sorted data to the list
			sortedList.stream()
					.forEach(e -> list.add(e));
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initTable() {
		try {
			initList();
			
			// Binds the table columns to the list
			colItemID.setCellValueFactory(cellData -> cellData.getValue().itemIDProperty().asObject());
			colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			colSupplier.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
			colDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
			colPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
			colUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
			colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
			colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<Item> filteredData = new FilteredList<>(list, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(i -> {
	                // If filter text is empty, display all data.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                switch(cbSearch.getValue()) {
	                case "Name": if ((i.getName().toLowerCase()).contains(lowerCaseFilter)) {
				                    return true; // Filter matches first name.
				                } 
	                			break;
	                case "Supplier": if ((i.getSupplier().toLowerCase()).contains(lowerCaseFilter)) {
				                    return true; // Filter matches first name.
				                } 
			        			break;
	                case "Description": if ((i.getDescription().toLowerCase()).contains(lowerCaseFilter)) {
				                    return true; // Filter matches first name.
				                } 
			        			break;
	                case "Unit": if ((i.getUnit().toLowerCase()).contains(lowerCaseFilter)) {
				                    return true; // Filter matches first name.
				                } 
			        			break;
	                case "Status": if ((i.getStatus().toLowerCase()).contains(lowerCaseFilter)) {
				                    return true; // Filter matches first name.
				                } 
			        			break;
	                }
	                
	                return false; // Does not match.
	            });
	            
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<Item> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblItem.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblItem.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtItemID.setText("");
		txtName.setText("");
		cbSupplier.setValue(null);
		txtDescription.setText("");
		txtPrice.setText("");
		cbUnit.setValue(null);
		cbStatus.setValue(null);
		tempName = "";
	}
	
	public boolean isItemExisting(String name) {
		boolean existing = ItemDAOImpl.getInstance()
								.getAll()
								.stream()
								.filter(i -> i.getName().equalsIgnoreCase(name))
								.findFirst()
								.isPresent();
		if(existing){
			return (name.equalsIgnoreCase(tempName))?false:true;
		}
		return false;
	}
	
	public boolean hasIncompleteFields() {
		if((txtName.getText()+"").equals("")) return true;
		if((cbSupplier.getValue()+"").equals("null")) return true;
		if((txtDescription.getText()+"").equals("")) return true;
		if((txtPrice.getText()+"").equals("")) return true;
		if((cbUnit.getValue()+"").equals("null")) return true;
		if((cbStatus.getValue()+"").equals("null")) return true;
		return false;
	}
	
	public static UpdateItemsController getInstance() {
		return instance;
	}
}
