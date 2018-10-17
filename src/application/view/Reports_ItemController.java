package application.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import application.DAO.SupplierDAOImpl;
import application.util.Dialogs;
import application.util.ReportsUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class Reports_ItemController implements Initializable {
	private static Reports_ItemController instance = new Reports_ItemController();
	
	@FXML
    private JFXComboBox<String> cbType;
	@FXML
	private JFXComboBox<String> cbSupplier;
    @FXML
    private JFXButton btnOK;
    @FXML
    private JFXButton btnCancel;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			cbType.getItems().addAll("All","In Stock","Out of Stock");
			cbType.setValue("All");
			
			cbSupplier.getItems().add("All");
			
			SupplierDAOImpl.getInstance()
						.getAll()
						.stream()
						.sorted((o1,o2) -> o1.getName().compareTo(o2.getName()))
						.forEach(e->cbSupplier.getItems().add(e.getName()));
			
			cbSupplier.setValue("All");
			
			btnOK.setOnAction(e->{
				try {
					String filePath = "Reports/Inventory.jrxml";
					File file = new File(filePath);
					ReportsUtil.getInstance().showItemReport(file,cbType.getValue(),cbSupplier.getValue());
				} catch(Exception err) {
					err.printStackTrace();
				}
			});
			
			btnCancel.setOnAction(e->{
				Dialogs.getInstance().close("Reports_Item");
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}

	public static Reports_ItemController getInstance() {
		return instance;
	}
}
