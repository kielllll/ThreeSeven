package application.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.DAO.UnitDAOImpl;
import application.model.Unit;
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

public class UnitDialogController implements Initializable {
	@FXML
    private JFXTextField txtSearch;
	@FXML
	private JFXTextField txtUnitID;
    @FXML
    private JFXTextField txtDescription;
    @FXML
    private TableView<Unit> tblUnit;
    @FXML
    private TableColumn<Unit, Integer> colUnitID;
    @FXML
    private TableColumn<Unit, String> colDescription;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Label lblError;
    
    private static UnitDialogController instance = new UnitDialogController();
    
    private ObservableList<Unit> list = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			initTable();
			txtUnitID.setEditable(false);
			txtUnitID.setText((UnitDAOImpl.getInstance().getAll().size()+1)+"");
			
			Components.hideError(lblError);
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields())
					Components.showError(lblError, "Error: Please fill up required fields");
				else {
					if(existing(txtDescription.getText()))
						Components.showError(lblError, "Error: Unit description already exists.");
					else {
						Unit u = new Unit(Integer.parseInt(txtUnitID.getText()), txtDescription.getText());
						
						UnitDAOImpl.getInstance()
								.insert(u);
						
						Sessions.getInstance().audit("Added a new measurement unit: "+u.getUnitID()+" - "+u.getDescription());
						
						clear();
						list.add(u);
						AddItemsController.getInstance().initList();
						UpdateItemsController.getInstance().initList();
					}
				}
			});
			
			btnClear.setOnAction(e->{
				clear();
			});
			
			btnCancel.setOnAction(e->{
				Dialogs.getInstance().close("Unit");
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}

	public void initList() {
		try {
			list.clear();
			
			List<Unit> sortedList = UnitDAOImpl.getInstance()
											  .getAll()
											  .parallelStream()
											  .sorted((o1,o2) -> (o1.getUnitID()+"").compareTo(o2.getUnitID()+""))
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
			
			colUnitID.setCellValueFactory(cellData -> cellData.getValue().unitIDProperty().asObject());
			colDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<Unit> filteredData = new FilteredList<>(list, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(i -> {
	                // If filter text is empty, display all data.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (i.getDescription().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<Unit> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblUnit.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblUnit.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public boolean hasIncompleteFields() {
		return ((txtDescription.getText()+"").equals(""))?true:false;
	}
	
	public boolean existing(String description) {
		for(Unit u : UnitDAOImpl.getInstance().getAll()) {
			if(u.getDescription().equals(description))
				return true;
		}
		
		return false;
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtUnitID.setText((UnitDAOImpl.getInstance().getAll().size()+1)+"");
		txtDescription.setText("");
	}
	
	public static UnitDialogController getInstance() {
		return instance;
	}
}
