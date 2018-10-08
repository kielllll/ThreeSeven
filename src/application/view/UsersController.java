package application.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class UsersController implements Initializable {
	@FXML
	private Tab tabCreate;
	@FXML
	private Tab tabUpdate;
	
	private static UsersController instance = new UsersController();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			initScreens();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void initScreens() {
		try {
			FXMLLoader ldCreate = new FXMLLoader();
			ldCreate.setLocation(this.getClass().getResource("AddUsers.fxml"));
			ldCreate.setController(AddUsersController.getInstance());
			
			FXMLLoader ldUpdate = new FXMLLoader();
			ldUpdate.setLocation(this.getClass().getResource("UpdateUsers.fxml"));
			ldUpdate.setController(UpdateUsersController.getInstance());
			
			tabCreate.setContent(ldCreate.load());
			tabUpdate.setContent(ldUpdate.load());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static UsersController getInstance() {
		return instance;
	}
}
