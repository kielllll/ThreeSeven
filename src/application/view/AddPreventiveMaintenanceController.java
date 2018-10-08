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

import application.DAO.PreventiveMaintenanceDAOImpl;
import application.DAO.VehicleDAOImpl;
import application.model.PreventiveMaintenance;
import application.util.Components;
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

public class AddPreventiveMaintenanceController implements Initializable {
	@FXML
    private JFXTextField txtPMID;
    @FXML
    private JFXComboBox<String> cbPlateNumber;
    @FXML
    private JFXTextArea txtDescription;
    @FXML
    private JFXDatePicker dpDate;
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
    private TableView<PreventiveMaintenance> tblPM;
    @FXML
    private TableColumn<PreventiveMaintenance, Integer> colPM;
    @FXML
    private TableColumn<PreventiveMaintenance, String> colPlateNumber;
    @FXML
    private TableColumn<PreventiveMaintenance, String> colDescription;
    @FXML
    private TableColumn<PreventiveMaintenance, LocalDate> colDate;
    
	private static AddPreventiveMaintenanceController instance = new AddPreventiveMaintenanceController();
	
	private ObservableList<PreventiveMaintenance> list = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			initTable();
			
			Components.hideError(lblError);
			txtPMID.setText(PreventiveMaintenanceDAOImpl.getInstance().getAll().size()+1+"");
			txtPMID.setEditable(false);
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields())
					Components.showError(lblError, "Error: Please fill required fields");
				else {
					PreventiveMaintenance pm = new PreventiveMaintenance(PreventiveMaintenanceDAOImpl.getInstance().getAll().size()+1,cbPlateNumber.getValue(),txtDescription.getText(),dpDate.getValue());
					
					PreventiveMaintenanceDAOImpl.getInstance().insert(pm);
					Sessions.getInstance().audit("Added a new preventive maintenance with id = "+pm.getPmID()+" on plate number: "+pm.getPlateNumber());
					clear();
					initList();
					UpdatePreventiveMaintenanceController.getInstance().initList();
				}
			});
			
			btnClear.setOnAction(e->{
				clear();
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
			cbPlateNumber.getItems().clear();
			
			VehicleDAOImpl.getInstance()
					.getAll()
					.stream()
					.sorted((o1,o2) -> o1.getPlateNumber().compareTo(o2.getPlateNumber()))
					.forEach(e->{
						cbPlateNumber.getItems().add(e.getPlateNumber());
					});
			
			list.clear();
			
			List<PreventiveMaintenance> sortedList = PreventiveMaintenanceDAOImpl.getInstance()
																			.getAll()
																			.stream()
																			.sorted((o1,o2) -> (o1.getPmID()<o2.getPmID())?1:-1)
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
			
			colPM.setCellValueFactory(cellData -> cellData.getValue().pmIDProperty().asObject());
			colPlateNumber.setCellValueFactory(cellData -> cellData.getValue().plateNumberProperty());
			colDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
			colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
			
			// 1. Wrap the ObservableListt in a FilteredList
			FilteredList<PreventiveMaintenance> filteredData = new FilteredList<>(list, e-> true);
			
			// 2. Set the filter Predicate whenever the filter changes
			txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(s -> {
					//If filter text is empty, display all data
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					
					// Compare names of data with every filter
					String lowerCaseFilter = newValue.toLowerCase();
					
					if(((s.getPlateNumber()+"").toLowerCase()).contains(lowerCaseFilter)) {
						return true; //Filter matches
					}
					
					return false; //Does not match
				});
			});
			
			// 3. Wrap the FilteredList in a SortedList
			SortedList<PreventiveMaintenance> sortedData = new SortedList<>(filteredData);
			
			// 4. Bind the sortedList comparator to the table view comparator
			sortedData.comparatorProperty().bind(tblPM.comparatorProperty());
			
			// 5. Add sorted (and filtered) data to the table.
			tblPM.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public boolean hasIncompleteFields() {
		if((cbPlateNumber.getValue()+"").equals("null")) return true;
		if((txtDescription.getText()+"").equals("")) return true;
		if((dpDate.getValue()+"").equals("null")) return true;
		return false;
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtPMID.setText(PreventiveMaintenanceDAOImpl.getInstance().getAll().size()+1+"");
		cbPlateNumber.setValue(null);
		txtDescription.setText("");
		dpDate.setValue(null);
	}
	
	public static AddPreventiveMaintenanceController getInstance() {
		return instance;
	}
}
