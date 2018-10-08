package application.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class PreventiveMaintenanceController implements Initializable {
	@FXML
	private Tab tabCreate;
	@FXML
	private Tab tabUpdate;
	@FXML
	private JFXTextField txtSearch;
	
	private static PreventiveMaintenanceController instance = new PreventiveMaintenanceController();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		initScreens();
	}

	public void initScreens() {
		try {
			FXMLLoader ldAdd = new FXMLLoader();
			ldAdd.setLocation(this.getClass().getResource("AddPreventiveMaintenance.fxml"));
			ldAdd.setController(AddPreventiveMaintenanceController.getInstance());
			
			FXMLLoader ldUpdate = new FXMLLoader();
			ldUpdate.setLocation(this.getClass().getResource("UpdatePreventiveMaintenance.fxml"));
			ldUpdate.setController(UpdatePreventiveMaintenanceController.getInstance());
			
			tabCreate.setContent(ldAdd.load());
			tabUpdate.setContent(ldUpdate.load());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PreventiveMaintenanceController getInstance() {
		return instance;
	}
}
