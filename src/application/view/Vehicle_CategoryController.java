package application.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.DAO.VehicleCategoryDAOImpl;
import application.model.VehicleCategory;
import application.util.Components;
import application.util.Dialogs;
import application.util.Sessions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Vehicle_CategoryController implements Initializable {
	
	private static Vehicle_CategoryController instance = new Vehicle_CategoryController();
	
	@FXML
    private JFXTextField txtSearch;
    @FXML
    private JFXTextField txtCategoryID;
    @FXML
    private JFXTextField txtName;
    @FXML
    private TableView<VehicleCategory> tblCategory;
    @FXML
    private TableColumn<VehicleCategory, Integer> colCategoryID;
    @FXML
    private TableColumn<VehicleCategory, String> colName;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Label lblError;
    
    private ObservableList<VehicleCategory> list = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			Components.hideError(lblError);
			initTable();
			txtCategoryID.setEditable(false);
			txtCategoryID.setText((VehicleCategoryDAOImpl.getInstance().getAll().size()+1)+"");
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields())
					Components.showError(lblError, "Error: Please fill up required fields");
				else {
					if(existing(txtName.getText()))
						Components.showError(lblError, "Error: Model description already exists");
					else {
						VehicleCategory v = new VehicleCategory(Integer.parseInt(txtCategoryID.getText()), txtName.getText());
						
						VehicleCategoryDAOImpl.getInstance().insert(v);
						Sessions.getInstance().audit("Added a new measurement unit: "+v.getCategoryID()+" - "+v.getDescription());
						
						clear();
						list.add(v);
						Sessions.getInstance().audit("Added a new type of vehicle: "+v.getCategoryID());
						AddVehiclesController.getInstance().initList();
						UpdateVehiclesController.getInstance().initList();
					}
				}
			});
			
			btnClear.setOnAction(e->{
				clear();
			});
			
			btnCancel.setOnAction(e->{
				Dialogs.getInstance().close("Category");
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initList() {
		try {
			list.clear();
			
			List<VehicleCategory> sortedList = VehicleCategoryDAOImpl.getInstance()
															.getAll()
															.parallelStream()
															.sorted((o1,o2)->(o1.getCategoryID()+"").compareTo(o2.getCategoryID()+""))
															.collect(Collectors.toList());
			sortedList.stream()
					.forEach(e->list.add(e));
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initTable() {
		try {
			initList();
			
			colCategoryID.setCellValueFactory(cellData -> cellData.getValue().categoryIDProperty().asObject());
			colName.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<VehicleCategory> filteredData = new FilteredList<>(list, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(i -> {
	                // If filter text is empty, display all data.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if ((i.getDescription().toLowerCase()).contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<VehicleCategory> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblCategory.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblCategory.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public boolean hasIncompleteFields() {
		return ((txtName.getText()+"").equals(""))?true:false;
	}
	
	public boolean existing(String description) {
		for(VehicleCategory v : VehicleCategoryDAOImpl.getInstance().getAll()) {
			if(v.getDescription().equals(description))
				return true;
		}
		
		return false;
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtCategoryID.setText((VehicleCategoryDAOImpl.getInstance().getAll().size()+1)+"");
		txtName.setText("");
	}
	
	public static Vehicle_CategoryController getInstance() {
		return instance;
	}
}
