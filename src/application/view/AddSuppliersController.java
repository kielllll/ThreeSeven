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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AddSuppliersController implements Initializable {
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
	
	private static AddSuppliersController instance = new AddSuppliersController();
	
	ObservableList<Supplier> listSupplier = FXCollections.observableArrayList();
			
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			initTable();
			Components.hideError(lblError);
			
			// Sets fixed value on the combo box
			cbStatus.getItems().add("active");
			cbStatus.getItems().add("inactive");
			
			// Sets the text to a fixed value
			txtSupplierID.setEditable(false);
			txtSupplierID.setText((SupplierDAOImpl.getInstance().getAll().size()+1)+"");
			
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
			
			btnSave.setOnAction(e-> {
				if(hasIncompleteFields()) {
					Components.showError(lblError, "Error: Please fill up required fields");
				} else {
					Representative r = new Representative(RepresentativeDAOImpl.getInstance().getAll().size()+1,txtRepName.getText(),txtRepContact.getText());
					RepresentativeDAOImpl.getInstance().insert(r);
					
					Supplier s = new Supplier(SupplierDAOImpl.getInstance().getAll().size()+1, txtName.getText(), r.getRepName(), r.getRepContact(), txtLocation.getText(), cbStatus.getValue());
					SupplierDAOImpl.getInstance().insert(s);
					
					Sessions.getInstance().audit("Added a supplier: "+s.getSupplierID()+" - "+s.getName());
					
					listSupplier.add(s);
					UpdateSuppliersController.getInstance().initList();
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
			
			// 1. Wrap the ObservableListt in a FilteredList
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
					
					if(s.getName().toLowerCase().contains(lowerCaseFilter)) {
						return true; //Filter matches
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
	
	public void clear() {
		Components.hideError(lblError);
		txtSupplierID.setText((SupplierDAOImpl.getInstance().getAll().size()+1)+"");
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
	
	public static AddSuppliersController getInstance() {
		return instance;
	}
}
