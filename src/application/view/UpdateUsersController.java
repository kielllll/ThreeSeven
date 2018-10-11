package application.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.DAO.AccessTypeDAOImpl;
import application.DAO.UserDAOImpl;
import application.model.User;
import application.util.Components;
import application.util.Encryption;
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

public class UpdateUsersController implements Initializable {
	@FXML
	private JFXTextField txtUserID;
	@FXML
	private JFXTextField txtFirstname;
	@FXML
	private JFXTextField txtLastname;
	@FXML
	private JFXPasswordField txtPass;
	@FXML
	private JFXPasswordField txtConfirm;
	@FXML
	private JFXComboBox<String> cbAccessType;
	@FXML
	private JFXComboBox<String> cbStatus;
	@FXML
	private JFXButton btnSave;
	@FXML
	private JFXButton btnClear;
	@FXML
	private Label lblError;
	
	@FXML
	private JFXButton btnRefresh;
	@FXML
	private JFXTextField txtSearch;
	@FXML
	private TableView<User> tblUser;
	@FXML
	private TableColumn<User, Integer> colUserID;
	@FXML
	private TableColumn<User, String> colFirstname;
	@FXML
	private TableColumn<User, String> colLastname;
	@FXML
	private TableColumn<User, String> colAccessType;
	@FXML
	private TableColumn<User, String> colStatus;
	
	private static UpdateUsersController instance = new UpdateUsersController();
	
	private ObservableList<User> list = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			initTable();
			Components.hideError(lblError);
			txtUserID.setEditable(false);
			txtPass.setDisable(true);
			txtConfirm.setDisable(true);
			
			AccessTypeDAOImpl.getInstance().getAll()
							.stream()
							.forEach(at -> cbAccessType.getItems().add(at.getDescription()));
			
			cbStatus.getItems().add("active");
			cbStatus.getItems().add("inactive");
			
			//Restricting user inputs to prevent anomalies in the data
			txtFirstname.textProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.matches("[\\sA-Za-z]*")) 
					txtFirstname.setText(oldValue);
				
				if(newValue.length()>50)
					txtFirstname.setText(oldValue);
			});
			
			txtLastname.textProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.matches("[\\sA-Za-z]*"))
					txtLastname.setText(oldValue);
				
				if(newValue.length()>50)
					txtLastname.setText(oldValue);
			});
			
//			txtConfirm.focusedProperty().addListener((observable, oldValue, newValue) -> {
//				if(newValue) {
//					hideError();
//				}
//				else {
//					if(!Validation.isPasswordValidated(txtPass.getText(), txtConfirm.getText()))
//						showError("Error: Your password did not match.");
//				}
//			});
			
			tblUser.setOnMouseClicked(e->{
				if(Bindings.isNotEmpty(tblUser.getItems()).get()) {
					User u = tblUser.getFocusModel().getFocusedItem();
					
					txtUserID.setText(u.getLoginID()+"");
					txtFirstname.setText(u.getFirstname());
					txtLastname.setText(u.getLastname());
					cbAccessType.setValue(u.getAccessType());
					cbStatus.setValue(u.getStatus());
				}
			});
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields()) { 
					Components.showError(lblError, "Error: Please fill up required fields");
				} else {
//					if(!Validation.isPasswordValidated(txtPass.getText(),txtConfirm.getText())) {
//						showError("Error: Your password did not match.");
//					} else {
//						
//					}
					User u = new User(Integer.parseInt(txtUserID.getText()),(10000+(Integer.parseInt(txtUserID.getText()))),txtFirstname.getText(),txtLastname.getText(),Encryption.hashPassword(txtPass.getText()),cbAccessType.getValue(),cbStatus.getValue());
					int accessType=(u.getAccessType().equalsIgnoreCase("administrator"))?1:2;
					
					String query = "UPDATE users SET firstname='"+u.getFirstname()+"', lastname='"+u.getLastname()+"', access_type_ID="+accessType+", status='"+u.getStatus()+"' WHERE user_ID="+Integer.parseInt(txtUserID.getText()+"");
					UserDAOImpl.getInstance().update(query);
					
					list.parallelStream()
						.filter(user -> user.getUserID()==u.getUserID())
						.forEach(user -> {
							user.setFirstname(u.getFirstname());
							user.setLastname(u.getLastname());
							user.setPassword(u.getPassword());
							user.setAccessType(u.getAccessType());
							user.setStatus(u.getStatus());
						});
					
					if(!Sessions.getInstance().getUser().getAccessType().equals("Master"))
						Sessions.getInstance().audit("Updated a user: "+u.getUserID()+" - "+u.getFirstname()+" "+u.getLastname());
					AddUsersController.getInstance().initList();
					clear();
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
	
	//Checks if there is/are some fields that has not been filled
	public boolean hasIncompleteFields() {
//		if((txtUserID.getText()+"").equals("")) return true;
		if((txtFirstname.getText()+"").equals("")) return true;
		if((txtLastname.getText()+"").equals("")) return true;
//		if((txtPass.getText()+"").equals("")) return true;
//		if((txtConfirm.getText()+"").equals("")) return true;
		if((cbAccessType.getValue()+"").equals("null")) return true;
		if((cbStatus.getValue()+"").equals("null")) return true;
		return false;
	}
	
	public void initList() {
		try {
			list.clear();
			
			//Sorts the data from the database
			List<User> sortedList = UserDAOImpl.getInstance()
					.getAll()
					.parallelStream()
					.sorted((o1,o2)->(o1.getUserID()>o2.getUserID())?1:-1)
					.collect(Collectors.toList());
			
			//Adds the sorted data to the list
			sortedList.stream()
					.forEach( e -> list.add(e));
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	//Initializes table
	public void initTable() {
		try {
			initList();
			
			//Bind the tablecolumns to the list
			colUserID.setCellValueFactory(cellData -> cellData.getValue().loginIDProperty().asObject());
			colFirstname.setCellValueFactory(cellData -> cellData.getValue().firstnameProperty());
			colLastname.setCellValueFactory(cellData -> cellData.getValue().lastnameProperty());
			colAccessType.setCellValueFactory(cellData -> cellData.getValue().accessTypeProperty());
			colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<User> filteredData = new FilteredList<>(list, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(u -> {
	                // If filter text is empty, display all persons.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                String name = u.getFirstname()+" "+u.getLastname();
	                if (name.toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<User> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblUser.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblUser.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtUserID.setText("");
		txtFirstname.setText("");
		txtLastname.setText("");
		txtPass.setText("");
		txtConfirm.setText("");
		cbAccessType.setValue(null);
		cbStatus.setValue(null);
	}
	
	public static UpdateUsersController getInstance() {
		return instance;
	}
}
