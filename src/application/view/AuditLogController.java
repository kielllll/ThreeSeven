package application.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import application.DAO.AuditLogDAOImpl;
import application.model.AuditLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AuditLogController implements Initializable {
	
	@FXML
	private JFXButton btnBackUp;
	@FXML
    private JFXComboBox<String> cbSearch;
    @FXML
    private JFXTextField txtSearch;
    @FXML
    private JFXComboBox<String> cbAccessType;
    @FXML
    private TableView<AuditLog> tblAuditLog;
    @FXML
    private TableColumn<AuditLog, Integer> colAuditLogID;
    @FXML
    private TableColumn<AuditLog, LocalDate> colDate;
    @FXML
    private TableColumn<AuditLog, String> colTime;
    @FXML
    private TableColumn<AuditLog, Integer> colUserID;
    @FXML
    private TableColumn<AuditLog, String> colFirstname;
    @FXML
    private TableColumn<AuditLog, String> colLastname;
    @FXML
    private TableColumn<AuditLog, String> colAccessType;
    @FXML
    private TableColumn<AuditLog, String> colAction;
    
	private static AuditLogController instance = new AuditLogController();
	
	private ObservableList<AuditLog> list = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			initTable();
			
			cbSearch.getItems().addAll("Audit log ID","Name","User ID","Action");
			cbAccessType.getItems().addAll("All","Administrator","Employee");
			cbSearch.setValue("Audit log ID");
			
			cbAccessType.setOnAction(e->{
				if((cbAccessType.getValue()+"").equals("Administrator")) {
					list.clear();
					List<AuditLog> sortedList = AuditLogDAOImpl.getInstance()
															.getAll()
															.stream()
															.filter(a->a.getAccessType().equals("administrator"))
															.sorted((o1,o2)->(o1.getAuditLogID()>o2.getAuditLogID())?-1:1)
															.collect(Collectors.toList());
					sortedList.stream()
						.forEach(a->list.add(a));
				} else if((cbAccessType.getValue()+"").equals("All")) {
					initList();
				} else {
					list.clear();
					List<AuditLog> sortedList = AuditLogDAOImpl.getInstance()
															.getAll()
															.stream()
															.filter(a->a.getAccessType().equals("employee"))
															.sorted((o1,o2)->(o1.getAuditLogID()>o2.getAuditLogID())?-1:1)
															.collect(Collectors.toList());
					sortedList.stream()
						.forEach(a->list.add(a));
				}
			});
			
			btnBackUp.setOnAction(e->{
				Process p = null;
				
				try {
					Runtime runtime = Runtime.getRuntime();
				 	String executeCmd = "C:/xampp/mysql/bin/mysqldump -uroot --add-drop-database -B Three_Seven -r C:/kiel/ThreeSeven/ThreeSeven_"+LocalDate.now()+".sql";
//		            p = runtime.exec("mysqldump -uroot -p ThreeSeven > C:\\kiel\\ThreeSeven.sql");
				 	p = runtime.exec(executeCmd);
		            //change the dbpass and dbname with your dbpass and dbname
		            int processComplete = p.waitFor();

		            if (processComplete == 0) 
		            	System.out.println("Backup created successfully");
		            else System.out.println("Could not create backup");
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
			list.clear();
			
			List<AuditLog> sortedList = AuditLogDAOImpl.getInstance()
												.getAll()
												.stream()
												.sorted((o1,o2) -> (o1.getAuditLogID()<o2.getAuditLogID())?1:-1)
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
			
			colAuditLogID.setCellValueFactory(cellData -> cellData.getValue().auditLogIDProperty().asObject());
			colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
			colTime.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
			colUserID.setCellValueFactory(cellData -> cellData.getValue().userIDProperty().asObject());
			colFirstname.setCellValueFactory(cellData -> cellData.getValue().firstnameProperty());
			colLastname.setCellValueFactory(cellData -> cellData.getValue().lastnameProperty());
			colAccessType.setCellValueFactory(cellData -> cellData.getValue().accessTypeIDProperty());
			colAction.setCellValueFactory(cellData -> cellData.getValue().actionProperty());
			
			// 1. Wrap the ObservableListt in a FilteredList
			FilteredList<AuditLog> filteredData = new FilteredList<>(list, e-> true);
			
			// 2. Set the filter Predicate whenever the filter changes
			txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(s -> {
					//If filter text is empty, display all data
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					
					// Compare names of data with every filter
					String lowerCaseFilter = newValue.toLowerCase();
//					cbSearch.getItems().addAll("Audit log ID","Name","User ID");
					switch(cbSearch.getValue()) {
						case "Audit log ID":
											if((s.getAuditLogID()+"").contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
						case "Name":
											if(((s.getFirstname()+" "+s.getLastname()).toLowerCase()).contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
						case "User ID":
											if((s.getUserID()+"").contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
						case "Action":		if(((s.getAction()+"").toLowerCase()).contains(lowerCaseFilter)) {
												return true; //Filter matches
											}
											break;
					}
					
					return false; //Does not match
				});
			});
			
			// 3. Wrap the FilteredList in a SortedList
			SortedList<AuditLog> sortedData = new SortedList<>(filteredData);
			
			// 4. Bind the sortedList comparator to the table view comparator
			sortedData.comparatorProperty().bind(tblAuditLog.comparatorProperty());
			
			// 5. Add sorted (and filtered) data to the table.
			tblAuditLog.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public static AuditLogController getInstance() {
		return instance;
	}
}
