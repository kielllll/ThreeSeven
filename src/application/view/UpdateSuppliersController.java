package application.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import application.DAO.RepresentativeDAOImpl;
import application.DAO.SupplierDAOImpl;
import application.model.Representative;
import application.model.Supplier;
import application.util.Components;
import application.util.Sessions;
import application.util.Validation;
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

public class UpdateSuppliersController implements Initializable {
	@FXML
	private JFXTextField txtSupplierID;
	@FXML
	private JFXTextField txtName;
	@FXML
	private JFXTextField txtRepName;
	@FXML
	private JFXTextField txtRepContact;
	@FXML
	private JFXComboBox<String> cbStatus;
	@FXML
	private JFXTextArea txtLocation;
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
	private TableView<Supplier> tblSupplier;
	@FXML
	private TableColumn<Supplier, Integer> colSupplierID;
	@FXML
	private TableColumn<Supplier, String> colCompName;
	@FXML
	private TableColumn<Supplier, String> colRepName;
	@FXML
	private TableColumn<Supplier, String> colRepContact;
	@FXML
	private TableColumn<Supplier, String> colLocation;
	@FXML
	private TableColumn<Supplier, String> colStatus;
	
	private static UpdateSuppliersController instance = new UpdateSuppliersController();
	
	ObservableList<Supplier> listSupplier = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			initTable();
			Components.hideError(lblError);
			
			// Sets fixed value on the combo box
			cbStatus.getItems().addAll("active","inactive");
			cbStatus.setValue("active");
			
			cbSearch.getItems().addAll("Supplier ID","Company Name","Representative Name","Representative Contact","Location","Status");
			cbSearch.setValue("Supplier ID");
					
			// Sets the text to a fixed value
			txtSupplierID.setEditable(false);
			
			// Filters special characters and numbers to avoid data anomalies
			txtRepName.textProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.matches("[\\sA-Za-z]*"))
					txtRepName.setText(oldValue);
				
				if(newValue.length()>50)
					txtRepName.setText(oldValue);
			});
			
			txtRepContact.textProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.matches("[0-9]*"))
					txtRepContact.setText(oldValue);
				
				if(newValue.length()>11)
					txtRepContact.setText(oldValue);
			});
			
			txtRepContact.focusedProperty().addListener((observable, oldValue, newValue) -> {
				if(newValue)
					Components.hideError(lblError);
				else {
					if(!Validation.isContactValidated(txtRepContact.getText()))
						Components.showError(lblError, "Error: Invalid contact number. It must contain 11 digits.");
				}
			});
			
			tblSupplier.setOnMouseClicked(e-> {
				if(Bindings.isNotEmpty(tblSupplier.getItems()).get()) {
					showDetails(tblSupplier.getFocusModel().getFocusedItem());
				}
			});
			
			tblSupplier.setOnKeyPressed(e->{
				if(Bindings.isNotEmpty(tblSupplier.getItems()).get()) {
					String keyCode = e.getCode().toString();
					
					switch(keyCode) {
					case "UP": showDetails(tblSupplier.getFocusModel().getFocusedItem());
						break;
					case "DOWN": showDetails(tblSupplier.getFocusModel().getFocusedItem());
						break;
					}
				}
			});
			
			btnSave.setOnAction(e-> {
				if(hasIncompleteFields()) {
					Components.showError(lblError, "Error: Please fill up required fields");
				} else {
					Representative r = new Representative(Integer.parseInt(txtSupplierID.getText()),txtRepName.getText(),txtRepContact.getText());
					String query = "UPDATE supplier_representatives SET name='"+r.getRepName()+"', contact_number='"+r.getRepContact()+"' WHERE representative_ID="+r.getRepID();
					RepresentativeDAOImpl.getInstance().update(query);
					
					Supplier s = new Supplier(Integer.parseInt(txtSupplierID.getText()), txtName.getText(), r.getRepName(), r.getRepContact(), txtLocation.getText(), cbStatus.getValue());
					query = "UPDATE suppliers SET name='"+s.getName()+"', representative_ID="+r.getRepID()+", location='"+s.getLocation()+"', status='"+s.getStatus()+"' WHERE supplier_ID="+s.getSupplierID();
					SupplierDAOImpl.getInstance().update(query);
					listSupplier.parallelStream()
							.filter(supplier -> supplier.getSupplierID() == s.getSupplierID())
							.forEach(supplier -> {
								supplier.setName(s.getName());
								supplier.setRepName(r.getRepName());
								supplier.setRepContact(r.getRepContact());
								supplier.setLocation(s.getLocation());
								supplier.setStatus(s.getStatus());
							});
					
					Sessions.getInstance().audit("Updated a supplier: "+s.getSupplierID()+" - "+s.getName());
					
					AddSuppliersController.getInstance().initList();
					clear();
				}
			});
			
			btnClear.setOnAction(e-> {
				clear();
			});
			
			btnRefresh.setOnAction(e-> {
				initList();
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initList() {
		try {
			listSupplier.clear();
			
			//Sorts the data from the database
			List<Supplier> slSupplier = SupplierDAOImpl.getInstance()
												.getAll()
												.parallelStream()
												.sorted((o1,o2) -> (o1.getSupplierID()>o2.getSupplierID())?1:-1)
												.collect(Collectors.toList());
			
			//Add the sorted data to the list
			slSupplier.stream()
					.forEach(e -> listSupplier.add(e));
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initTable() {
		try {
			initList();
			
			//Bind the table columns to the list
			colSupplierID.setCellValueFactory(cellData -> cellData.getValue().supplierIDProperty().asObject());
			colCompName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			colRepName.setCellValueFactory(cellData -> cellData.getValue().repNameProperty());
			colRepContact.setCellValueFactory(cellData -> cellData.getValue().repContactProperty());
			colLocation.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
			colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
			
			// !. Wrap the ObservableListt in a FilteredList
			FilteredList<Supplier> filteredData = new FilteredList<>(listSupplier, e-> true);
			
			// 2. Set the filter Predicate whenever the filter changes
			txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(s -> {
					//If filter text is empty, display all data
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					
					// Compare names of data with every filter
					String lowerCaseFilter = newValue.toLowerCase();
					
					switch(cbSearch.getValue()) {
					case "Supplier ID": if((s.getSupplierID()+"").contains(lowerCaseFilter)) {
											return true; //Filter matches
										}
											break;
					case "Company Name": if(s.getName().toLowerCase().contains(lowerCaseFilter)) {
											return true; //Filter matches
										}
											break;
					case "Representative Name": if(s.getRepName().toLowerCase().contains(lowerCaseFilter)) {
											return true; //Filter matches
										}
											break;
					case "Representative Contact": if(s.getRepContact().contains(lowerCaseFilter)) {
											return true; //Filter matches
										}
											break;
					case "Location": if(s.getLocation().toLowerCase().contains(lowerCaseFilter)) {
											return true; //Filter matches
										}
											break;
					case "Status": if(s.getStatus().toLowerCase().contains(lowerCaseFilter)) {
											return true; //Filter matches
										}
											break;
					}
					
					return false; //Does not match
				});
			});
			
			// 3. Wrap the FilteredList in a SortedList
			SortedList<Supplier> sortedData = new SortedList<>(filteredData);
			
			// 4. Bind the sortedList comparator to the table view comparator
			sortedData.comparatorProperty().bind(tblSupplier.comparatorProperty());
			
			// 5. Add sorted (and filtered) data to the table.
			tblSupplier.setItems(sortedData);
		} catch(Exception err) {
			
		}
	}
	
	public void showDetails(Supplier s) {
		try {
			txtSupplierID.setText(s.getSupplierID()+"");
			txtName.setText(s.getName());
			txtRepName.setText(s.getRepName());
			txtRepContact.setText(s.getRepContact());
			txtLocation.setText(s.getLocation());
			cbStatus.setValue(s.getStatus());
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtSupplierID.clear();
		txtName.setText("");
		txtRepName.setText("");
		txtRepContact.setText("");
		txtLocation.setText("");
		cbStatus.setValue(null);
	}
	
	public boolean hasIncompleteFields() {
		if((txtName.getText()+"").equals("")) return true;
		if((txtRepName.getText()+"").equals("")) return true;
		if((txtRepContact.getText()+"").equals("")) return true;
		if((txtLocation.getText()+"").equals("")) return true;
		if((cbStatus.getValue()+"").equals("null")) return true;
		return false;
	}
	
	public static UpdateSuppliersController getInstance() {
		return instance;
	}
}
