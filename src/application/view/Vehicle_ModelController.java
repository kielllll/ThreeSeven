package application.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.DAO.VehicleModelDAOImpl;
import application.model.VehicleModel;
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

public class Vehicle_ModelController implements Initializable {
	
	private static Vehicle_ModelController instance = new Vehicle_ModelController();
	
	@FXML
	private JFXTextField txtSearch;
    @FXML
    private JFXTextField txtModelID;
    @FXML
    private JFXTextField txtName;
    @FXML
    private TableView<VehicleModel> tblModel;
    @FXML
    private TableColumn<VehicleModel, Integer> colModelID;
    @FXML
    private TableColumn<VehicleModel, String> colName;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Label lblError;
	    
    private ObservableList<VehicleModel> list = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			Components.hideError(lblError);
			initTable();
			txtModelID.setEditable(false);
			txtModelID.setText((VehicleModelDAOImpl.getInstance().getAll().size()+1)+"");
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields())
					Components.showError(lblError, "Error: Please fill up required fields");
				else {
					if(existing(txtName.getText()))
						Components.showError(lblError, "Error: Model description already exists");
					else {
						VehicleModel v = new VehicleModel(Integer.parseInt(txtModelID.getText()), txtName.getText());
						
						VehicleModelDAOImpl.getInstance().insert(v);
						
						clear();
						list.add(v);
						Sessions.getInstance().audit("Added a new model of vehicle: "+v.getDescription());
						AddVehiclesController.getInstance().initList();
						UpdateVehiclesController.getInstance().initList();
					}
				}
			});
			
			btnClear.setOnAction(e->{
				clear();
			});
			
			btnCancel.setOnAction(e->{
				Dialogs.getInstance().close("Model");
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initList() {
		try {
			list.clear();
			
			List<VehicleModel> sortedList = VehicleModelDAOImpl.getInstance()
														.getAll()
														.parallelStream()
														.sorted((o1,o2) -> (o1.getModelID()+"").compareTo(o2.getModelID()+""))
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
			
			colModelID.setCellValueFactory(cellData -> cellData.getValue().modelIDProperty().asObject());
			colName.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<VehicleModel> filteredData = new FilteredList<>(list, e -> true);

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
	        SortedList<VehicleModel> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblModel.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblModel.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public boolean hasIncompleteFields() {
		return ((txtName.getText()+"").equals(""))?true:false;
	}
	
	public boolean existing(String description) {
		for(VehicleModel v : VehicleModelDAOImpl.getInstance().getAll()) {
			if(v.getDescription().equals(description))
				return true;
		}
		
		return false;
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtModelID.setText((VehicleModelDAOImpl.getInstance().getAll().size()+1)+"");
		txtName.setText("");
	}
	
	public static Vehicle_ModelController getInstance() {
		return instance;
	}
}
