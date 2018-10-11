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

public class Reports_VehicleController implements Initializable {
	private static Reports_VehicleController instance = new Reports_VehicleController();
	
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
			cbSelect.getItems().addAll("All","Active","Inactive");
			cbSelect.setValue("All");
			
			btnOK.setOnAction(e->{
				try {
					String filePath = "";
					switch(cbSelect.getValue()) {
					case "All": filePath = "Vehicle.jrxml";
						break;
					case "Active": filePath = "Vehicle_Active.jrxml";
						break;
					case "Inactive": filePath = "Vehicle_Inactive.jrxml";
						break;
					}
					File file = new File(filePath);
					ReportsUtil.getInstance().showReport(file);
				} catch(Exception err) {
					err.printStackTrace();
				}
			});
			
			btnCancel.setOnAction(e->{
				Dialogs.getInstance().close("Reports_Vehicle");
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}

	public static Reports_VehicleController getInstance() {
		return instance;
	}
}
