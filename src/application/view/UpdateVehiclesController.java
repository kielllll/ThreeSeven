package application.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import application.DAO.VehicleCategoryDAOImpl;
import application.DAO.VehicleDAOImpl;
import application.DAO.VehicleModelDAOImpl;
import application.model.Vehicle;
import application.util.Components;
import application.util.Dialogs;
import application.util.ReportsUtil;
import application.util.Sessions;
import application.util.StageController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class UpdateVehiclesController implements Initializable {
	@FXML
    private JFXTextField txtPlateNumber;
    @FXML
    private JFXTextField txtMVNumber;
    @FXML
    private JFXTextField txtEngineNumber;
    @FXML
    private JFXTextField txtChassisNumber;
    @FXML
    private JFXComboBox<String> cbModel;
    @FXML
    private JFXButton btnAddModel;
    @FXML
    private JFXComboBox<String> cbType;
    @FXML
    private JFXButton btnAddType;
    @FXML
    private JFXTextField txtEncumberedTo;
    @FXML
    private JFXComboBox<String> cbStatus;
    @FXML
    private JFXTextField txtAmount;
    @FXML
    private JFXDatePicker dpMaturity;
    @FXML
    private ImageView imgVehicle;
    @FXML
    private JFXButton btnAddImage;
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
    private TableView<Vehicle> tblVehicle;
    @FXML
    private TableColumn<Vehicle, String> colPlateNumber;
    @FXML
    private TableColumn<Vehicle, String> colMVNumber;
    @FXML
    private TableColumn<Vehicle, String> colEngineNumber;
    @FXML
    private TableColumn<Vehicle, String> colChassisNumber;
    @FXML
    private TableColumn<Vehicle, String> colModel;
    @FXML
    private TableColumn<Vehicle, String> colType;
    @FXML
    private TableColumn<Vehicle, String> colEncumberedTo;
    @FXML
    private TableColumn<Vehicle, Double> colAmount;
    @FXML
    private TableColumn<Vehicle, LocalDate> colMaturity;
    @FXML
    private TableColumn<Vehicle, String> colStatus;
	@FXML
	private JFXButton btnReport;
	
	private static UpdateVehiclesController instance = new UpdateVehiclesController(); 
	
	private ObservableList<Vehicle> list = FXCollections.observableArrayList();
	private FileChooser fileChooser;
	private File filePath;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			Components.hideError(lblError);
			initTable();
			
			cbStatus.getItems().addAll("Active","Inactive");
			cbSearch.getItems().addAll("Plate No.","MV No.","Engine No.","Chassis No.","Model","Type","Encumbered To");
			cbSearch.setValue("Plate No.");
			
			tblVehicle.setOnMouseReleased(e->{
				if(Bindings.isNotEmpty(tblVehicle.getItems()).get()) {
					Vehicle v = tblVehicle.getFocusModel().getFocusedItem();
					v.setVehicleID(VehicleDAOImpl.getInstance()
											.getAll()
											.stream()
											.filter(i->i.getMvNumber().equals(v.getMvNumber()))
											.findFirst()
											.get()
											.getVehicleID());	
					
					v.setImage(VehicleDAOImpl.getInstance()
							.getAll()
							.parallelStream()
							.filter(i->i.getMvNumber().equals(v.getMvNumber()))
							.findFirst()
							.get()
							.getImage());
					
					txtPlateNumber.setText(v.getPlateNumber());
					txtMVNumber.setText(v.getMvNumber());
					txtEngineNumber.setText(v.getEngineNumber());
					txtChassisNumber.setText(v.getChassisNumber());
					cbModel.setValue(v.getModel());
					cbType.setValue(v.getType());
					txtEncumberedTo.setText(v.getEmcumberedTo());
					txtAmount.setText(v.getAmount()+"");
					dpMaturity.setValue(v.getMaturityDate());
					cbStatus.setValue(v.getStatus());
					imgVehicle.setPreserveRatio(false);
					imgVehicle.setFitHeight(100);
					imgVehicle.setFitWidth(200);
					imgVehicle.setImage(v.getImage());
					
					Components.hideError(lblError);
				}
			});
			
			txtPlateNumber.textProperty().addListener((observable, oldValue, newValue) -> {
				if(Components.isPlateNumExisting(newValue)) {
					Components.showError(lblError, "Error: Plate number already exists");
				}
				else Components.hideError(lblError);
			});
			
			btnAddModel.setOnAction(e->{
				Dialogs.getInstance().showModel();
			});
			
			btnAddType.setOnAction(e->{
				Dialogs.getInstance().showCategory();
			});
			
			btnAddImage.setOnAction(e->{
				filePath = chooseImage();
			});
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields())
					Components.showError(lblError, "Error: Please fill up required fields");
				else {
					if(filePath!=null) {
						int vehicleID = VehicleDAOImpl.getInstance()
												.getAll()
												.stream()
												.filter(i->i.getPlateNumber().equals(txtPlateNumber.getText()))
												.findFirst()
												.get()
												.getVehicleID();
						
						Vehicle v = new Vehicle(vehicleID,txtPlateNumber.getText(), txtMVNumber.getText(), txtEngineNumber.getText(), txtChassisNumber.getText(), cbModel.getValue(), cbType.getValue(), txtEncumberedTo.getText(), Double.parseDouble(txtAmount.getText()), dpMaturity.getValue(), cbStatus.getValue(), imgVehicle.getImage());
						
						int modelID = VehicleModelDAOImpl.getInstance()
												.getAll()
												.stream()
												.filter(i->i.getDescription().equals(v.getModel()))
												.findFirst()
												.get()
												.getModelID();
						
						int typeID = VehicleCategoryDAOImpl.getInstance()
												.getAll()
												.stream()
												.filter(i->i.getDescription().equals(v.getType()))
												.findFirst()
												.get()
												.getCategoryID();
						
						VehicleDAOImpl.getInstance().update(v.getVehicleID(), v.getPlateNumber(), v.getMvNumber(), v.getEngineNumber(), v.getChassisNumber(), modelID, typeID, v.getEmcumberedTo(), txtAmount.getText(), v.getMaturityDate().toString(), v.getStatus(), filePath);
						Sessions.getInstance().audit("Updated a vehicle: "+v.getPlateNumber());
						clear();
						initList();
					} else if(filePath==null) {
						int vehicleID = VehicleDAOImpl.getInstance()
								.getAll()
								.stream()
								.filter(i->i.getPlateNumber().equals(txtPlateNumber.getText()))
								.findFirst()
								.get()
								.getVehicleID();
						
						Vehicle v = new Vehicle(vehicleID,txtPlateNumber.getText(), txtMVNumber.getText(), txtEngineNumber.getText(), txtChassisNumber.getText(), cbModel.getValue(), cbType.getValue(), txtEncumberedTo.getText(), Double.parseDouble(txtAmount.getText()), dpMaturity.getValue(), cbStatus.getValue(), imgVehicle.getImage());
						
						int modelID = VehicleModelDAOImpl.getInstance()
												.getAll()
												.stream()
												.filter(i->i.getDescription().equals(v.getModel()))
												.findFirst()
												.get()
												.getModelID();
						
						int typeID = VehicleCategoryDAOImpl.getInstance()
												.getAll()
												.stream()
												.filter(i->i.getDescription().equals(v.getType()))
												.findFirst()
												.get()
												.getCategoryID();
						
						VehicleDAOImpl.getInstance().update("UPDATE vehicles SET plate_number='"+v.getPlateNumber()+"', mv_number='"+v.getMvNumber()+"', engine_number='"+v.getEngineNumber()+"', chassis_number='"+v.getChassisNumber()+"', model_ID="+modelID+", category_ID="+typeID+", encumbered_to='"+v.getEmcumberedTo()+"', amount="+v.getAmount()+", maturity_date='"+v.getMaturityDate()+"', status='"+v.getStatus()+"' WHERE vehicle_ID="+v.getVehicleID());
						Sessions.getInstance().audit("Updated a vehicle: "+v.getPlateNumber());
						clear();
						initList();
					} else Components.showError(lblError, "Error: file is null");
					AddVehiclesController.getInstance().initList();
				}
			});
			
			btnClear.setOnAction(e->{
				clear();
			});
			
			btnRefresh.setOnAction(e->{
				initList();
			});
			
			btnReport.setOnAction(e->{
				try {
					File file = Paths.get("lib/reports/VehicleInfo.jrxml").toRealPath().toFile();
					ReportsUtil.getInstance().showReport(file);
				} catch(Exception err) {
					err.printStackTrace();
				}
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	
	
	public void initList() {
		try {
			cbModel.getItems().clear();
			VehicleModelDAOImpl.getInstance()
			   .getAll()
			   .stream()
			   .forEach(e->{
				   cbModel.getItems().add(e.getDescription());
			   });

			cbType.getItems().clear();
			VehicleCategoryDAOImpl.getInstance()
				.getAll()
				.stream()
				.forEach(e->{
					cbType.getItems().add(e.getDescription());
				});

			list.clear();
			
//			 Sorts the data from the database
			List<Vehicle> sortedList = VehicleDAOImpl.getInstance()
											.getAll()
											.parallelStream()
											.sorted((o1, o2) -> (o1.getVehicleID()>o2.getVehicleID())?1:-1)
											.collect(Collectors.toList());
			
			// Add the sorted data to the list
			sortedList.stream()
					.forEach(e -> {
						list.add(e);
					});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initTable() {
		try {
			initList();
			
			// Bind the table columns to the list
			colPlateNumber.setCellValueFactory(cellData -> cellData.getValue().plateNumberProperty());
			colMVNumber.setCellValueFactory(cellData -> cellData.getValue().mvNumberProperty());
			colEngineNumber.setCellValueFactory(cellData -> cellData.getValue().engineNumberProperty());
			colChassisNumber.setCellValueFactory(cellData -> cellData.getValue().chassisNumberProperty());
			colModel.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
			colType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
			colEncumberedTo.setCellValueFactory(cellData -> cellData.getValue().encumberedToProperty());
			colAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
			colMaturity.setCellValueFactory(cellData -> cellData.getValue().maturityDateProperty());
			colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
			
			// 1. Wrap the ObservableListt in a FilteredList
			FilteredList<Vehicle> filteredData = new FilteredList<>(list, e-> true);
			
			// 2. Set the filter Predicate whenever the filter changes
			txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(s -> {
					//If filter text is empty, display all data
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					
					// Compare names of data with every filter
					String lowerCaseFilter = newValue.toLowerCase();
					switch((cbSearch.getValue()+"")) {
						case "Plate No.":	if(((s.getPlateNumber()+"").toLowerCase()).contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
						case "MV No.":		if(((s.getMvNumber()+"").toLowerCase()).contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
						case "Engine No.":	if(((s.getEngineNumber()+"").toLowerCase()).contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
						case "Chassis No.":	if(((s.getChassisNumber()+"").toLowerCase()).contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
						case "Model":		if(((s.getModel()+"").toLowerCase()).contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
						case "Type":		if(((s.getType()+"").toLowerCase()).contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
						case "Encumbered To":	if(((s.getEmcumberedTo()+"").toLowerCase()).contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
					}
					
					return false; //Does not match
				});
			});
			
			// 3. Wrap the FilteredList in a SortedList
			SortedList<Vehicle> sortedData = new SortedList<>(filteredData);
			
			// 4. Bind the sortedList comparator to the table view comparator
			sortedData.comparatorProperty().bind(tblVehicle.comparatorProperty());
			
			// 5. Add sorted (and filtered) data to the table.
			tblVehicle.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public File chooseImage() {
		try {
			fileChooser = new FileChooser();
			fileChooser.setTitle("Open Image");
			
			filePath = fileChooser.showOpenDialog(StageController.getInstance().get("main"));
			
			if(filePath!=null) {
				BufferedImage bf = ImageIO.read(filePath);
				Image img = SwingFXUtils.toFXImage(bf, null);
				imgVehicle.setImage(img);
			}
		} catch(Exception err) {
			err.printStackTrace();
		}
		return filePath;
	} 
	
	public void clear() {
		Components.hideError(lblError);	
		txtPlateNumber.setText("");
		txtMVNumber.setText("");
		txtEngineNumber.setText("");
		txtChassisNumber.setText("");
		cbModel.setValue(null);
		cbType.setValue(null);
		txtEncumberedTo.setText("");
		txtAmount.setText("");
		dpMaturity.setValue(null);
		cbStatus.setValue(null);
		imgVehicle.setImage(null);
	}
	
	public boolean hasIncompleteFields() {
		if((txtPlateNumber.getText()+"").equals("")) return true;
		if((txtMVNumber.getText()+"").equals("")) return true;
		if((txtEngineNumber.getText()+"").equals("")) return true;
		if((txtChassisNumber.getText()+"").equals("")) return true;
		if((cbModel.getValue()+"").equals("null")) return true;
		if((cbType.getValue()+"").equals("null")) return true;
		if((txtEncumberedTo.getText()+"").equals("")) return true;
		if((txtAmount.getText()+"").equals("")) return true;
		if((dpMaturity.getValue()+"").equals("null")) return true;
		if((cbStatus.getValue()+"").equals("null")) return true;
		if(imgVehicle.getImage().equals(null)) return true;
		return false;
	}
	
	public static UpdateVehiclesController getInstance() {
		return instance;
	}
}
