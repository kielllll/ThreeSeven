package application.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class SuppliersController implements Initializable {
	@FXML
	private Tab tabCreate;
	@FXML
	private Tab tabUpdate;
	@FXML
	private JFXTextField txtSearch;
	
	private static SuppliersController instance = new SuppliersController();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		initScreens();
	}
	
	public void initScreens() {
		try {
			FXMLLoader ldCreate = new FXMLLoader();
			ldCreate.setLocation(this.getClass().getResource("AddSuppliers.fxml"));
			ldCreate.setController(AddSuppliersController.getInstance());
			
			FXMLLoader ldUpdate = new FXMLLoader();
			ldUpdate.setLocation(this.getClass().getResource("UpdateSuppliers.fxml"));
			ldUpdate.setController(UpdateSuppliersController.getInstance());
			
			tabCreate.setContent(ldCreate.load());
			tabUpdate.setContent(ldUpdate.load());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SuppliersController getInstance() {
		return instance;
	}
}
