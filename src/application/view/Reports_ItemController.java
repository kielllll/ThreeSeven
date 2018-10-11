package application.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import application.util.Dialogs;
import application.util.ReportsUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class Reports_ItemController implements Initializable {
	private static Reports_ItemController instance = new Reports_ItemController();
	
	@FXML
    private JFXComboBox<String> cbSelect;
    @FXML
    private JFXButton btnOK;
    @FXML
    private JFXButton btnCancel;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			cbSelect.getItems().addAll("All","In Stock","Out of Stock");
			cbSelect.setValue("All");
			
			btnOK.setOnAction(e->{
				try {
					String filePath = "";
					switch(cbSelect.getValue()) {
					case "All": filePath = "Inventory.jrxml";
						break;
					case "In Stock": filePath = "Inventory_InStock.jrxml";
						break;
					case "Out of Stock": filePath = "Inventory_OutOfStock.jrxml";
						break;
					}
					File file = new File(filePath);
					ReportsUtil.getInstance().showReport(file);
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
