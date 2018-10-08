package application.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.DAO.ItemDAOImpl;
import application.DAO.StockDAOImpl;
import application.model.Item;
import application.model.Stock;
import application.util.Components;
import application.util.Dialogs;
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

public class SI_ItemsDialogController implements Initializable{
	
	private static SI_ItemsDialogController instance = new SI_ItemsDialogController();
	
	@FXML
	private JFXTextField txtSearch;
	@FXML
	private JFXTextField txtItemID;
	@FXML
	private JFXTextField txtQuantity;
	@FXML
	private TableView<Stock> tblItem;
	@FXML
	private TableColumn<Stock, Integer> colItemID;
	@FXML
	private TableColumn<Stock, String> colName;
	@FXML
	private TableColumn<Stock, Integer> colQuantity;
	@FXML
	private Label lblError;
	@FXML
	private JFXButton btnSave;
	@FXML
	private JFXButton btnClear;
	@FXML
	private JFXButton btnCancel;
	
	private ObservableList<Stock> list = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			Components.hideError(lblError);
			initTable();
			txtItemID.setEditable(false);
			
			tblItem.setOnMouseClicked(e->{
				if(Bindings.isNotEmpty(tblItem.getItems()).get()) {
					Stock s = tblItem.getFocusModel().getFocusedItem();
					
					txtItemID.setText((s.getItemID()+""));
				}
			});
			
			
			txtQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.matches("[0-9]*"))
					txtQuantity.setText(oldValue);
				if(newValue.length()>11)
					txtQuantity.setText(oldValue);
			});
			
			txtQuantity.focusedProperty().addListener((observable, oldValue, newValue)->{
				if(newValue)
					Components.hideError(lblError);
			});
			
			btnSave.setOnAction(e->{
				if(hasIncompleteFields())
					Components.showError(lblError, "Error: Please fill up required fields");
				else {
					Stock p = tblItem.getFocusModel().getFocusedItem();
					if(StockInController.getInstance().isItemExisting(p.getName())) {
						StockInController.getInstance().update(p.getItemID(), Integer.parseInt(txtQuantity.getText()));
						StockInController.getInstance().updateCost();
					} else {
						StockInController.getInstance().add(p.getItemID(), Integer.parseInt(txtQuantity.getText()));
						StockInController.getInstance().updateCost();
					}
				}
			});
			
			btnClear.setOnAction(e->{
				clear();
			});
			
			btnCancel.setOnAction(e->{
				Dialogs.getInstance().close("SI_Item");
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}

	public void initList() {
		try {
			list.clear();
			List<Item> tempList = ItemDAOImpl.getInstance()
											.getAll()
											.parallelStream()
											.filter(i->i.getSupplier().equals(StockInController.getInstance().getSupplier()))
											.sorted((o1,o2) -> (o1.getItemID()>o2.getItemID())?1:-1)
											.collect(Collectors.toList());
			
			List<Stock> sortedList = StockDAOImpl.getInstance()
											.getAll()
											.parallelStream()
											.sorted((o1,o2) -> (o1.getItemID()>o2.getItemID())?1:-1)
											.collect(Collectors.toList());
			
			sortedList.stream()
					.forEach(e->{
						tempList.forEach(t->{
							if(e.getItemID()==t.getItemID())
								list.add(e);
						});
					});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initTable() {
		try {
			initList();
			
			colItemID.setCellValueFactory(cellData -> cellData.getValue().itemIDProperty().asObject());
			colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<Stock> filteredData = new FilteredList<>(list, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(i -> {
	                // If filter text is empty, display all data.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if ((i.getName().toLowerCase()).contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<Stock> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblItem.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        tblItem.setItems(sortedData);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public boolean hasIncompleteFields() {
		return ((txtQuantity.getText()+"").equals(""))? true:false;
	}
	
	public void setSelectedItem(int itemID, int quantity) {
		txtItemID.setText(itemID+"");
		txtQuantity.setText(quantity+"");
	}
	
	public void clear() {
		Components.hideError(lblError);
		txtItemID.setText("");
		txtQuantity.setText("");
	}
	
	public static SI_ItemsDialogController getInstance() {
		return instance;
	}
}
