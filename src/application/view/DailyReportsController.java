package application.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import application.DAO.DailyStockInExpensesDAOImpl;
import application.model.DailyStockInExpenses;
import application.util.Components;
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

public class DailyReportsController implements Initializable {

	@FXML
    private JFXComboBox<String> cbSearch;
    @FXML
    private JFXTextField txtSearch;
    @FXML
    private JFXTextField txtTotal;
    @FXML
    private JFXButton btnView;
    @FXML
    private TableView<DailyStockInExpenses> tblExpenses;
    @FXML
    private TableColumn<DailyStockInExpenses, Integer> colID;
    @FXML
    private TableColumn<DailyStockInExpenses, LocalDate> colDate;
    @FXML
    private TableColumn<DailyStockInExpenses, String> colSupplier;
    @FXML
    private TableColumn<DailyStockInExpenses, Integer> colInvoice;
    @FXML
    private TableColumn<DailyStockInExpenses, LocalDate> colInvoiceDate;
    @FXML
    private TableColumn<DailyStockInExpenses, Double> colAmount;
    @FXML
    private Label lblError;
    
	private static DailyReportsController instance = new DailyReportsController();
	
	private ObservableList<DailyStockInExpenses> list = FXCollections.observableArrayList();
	private double totalExpenses = 0.00;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			Components.hideError(lblError);
			initTable();
			cbSearch.getItems().addAll("Stock in ID","Transaction Date","Supplier","Invoice Number","Invoice Date","Amount");
			cbSearch.setValue("Stock in ID");
			txtTotal.setEditable(false);
			txtTotal.setText(getTotal()+"");
			
 		} catch(Exception err) {
 			err.printStackTrace();
 		}
	}

	public void initList() {
		try {
			list.clear();
			
			List<DailyStockInExpenses> sortedList = DailyStockInExpensesDAOImpl.getInstance()
																		.getAll()
																		.stream()
																		.sorted((o1,o2) -> (o1.getStockInID()<o2.getStockInID())?1:-1)
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
			
			colID.setCellValueFactory(cellData -> cellData.getValue().stockInIDProperty().asObject());
			colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
			colSupplier.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
			colInvoice.setCellValueFactory(cellData -> cellData.getValue().invoiceNumberProperty().asObject());
			colInvoiceDate.setCellValueFactory(cellData -> cellData.getValue().invoiceDateProperty());
			colAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<DailyStockInExpenses> filteredData = new FilteredList<>(list, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(i -> {
	                // If filter text is empty, display all data.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();
//	                cbSearch.getItems().addAll("Stock in ID","Transaction Date","Supplier","Invoice Number","Invoice Date","Amount");
	                switch((cbSearch.getValue()+"")) {
	                case "Stock in ID": if (((i.getStockInID()+"").toLowerCase()).contains(lowerCaseFilter)) {
						                    return true; // Filter matches first name.
						                }
						                break;
	                case "Transaction Date": if (((i.getDate()+"").toLowerCase()).contains(lowerCaseFilter)) {
							                    return true; // Filter matches first name.
							                }
						                	break;
	                case "Supplier": if (((i.getSupplier()+"").toLowerCase()).contains(lowerCaseFilter)) {
					                    return true; // Filter matches first name.
					                }
				                		break;
	                case "Invoice Number": if (((i.getInvoiceNumber()+"").toLowerCase()).contains(lowerCaseFilter)) {
							                    return true; // Filter matches first name.
							                }
						                		break;
	                case "Invoice Date": if (((i.getInvoiceDate()+"").toLowerCase()).contains(lowerCaseFilter)) {
						                    return true; // Filter matches first name.
						                }
					                		break;
	                case "Amount": if (((i.getAmount()+"").toLowerCase()).contains(lowerCaseFilter)) {
					                    return true; // Filter matches first name.
					                }
				                		break;
	                }
	                return false; // Does not match.
	            });
	            
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<DailyStockInExpenses> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblExpenses.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblExpenses.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public double getTotal() {
		totalExpenses = 0.0;
		if(Bindings.isNotEmpty(tblExpenses.getItems()).get()) {
			tblExpenses.getItems()
					.stream()
					.forEach(i->totalExpenses+=i.getAmount());
		}
		return totalExpenses;
	}
	
	public static DailyReportsController getInstance() {
		return instance;
	}
}
