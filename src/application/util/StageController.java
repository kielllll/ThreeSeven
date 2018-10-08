package application.util;

import java.util.HashMap;

import application.view.LoginController;
import application.view.RootController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageController {
	
	private static StageController instance = new StageController();
	
	private Stage stgLogin;
	private Stage stgMain;
	
	private HashMap<String, Stage> container = new HashMap<>();
	
	public void initStages() {
		try {
			stgLogin = new Stage();
			stgLogin.setResizable(false);
			stgLogin.setTitle("Three Seven Equipment Rental Inventory Management and Information System");
			
			stgMain = new Stage();
			stgMain.setResizable(false);
			stgMain.setTitle("Three Seven Equipment Rental Inventory Management and Information System");
			
			stgLogin.setOnCloseRequest(e->{
				try {
					Database.getInstance().DBCloseConnection();
				} catch(Exception err) {
					err.printStackTrace();
				}
			});
			
			stgMain.setOnCloseRequest(e->{
				try {
					if(Dialogs.getInstance().showConfirm("Exit Application", "Close the application?")==0) {
						if(!Sessions.getInstance().getUser().getAccessType().equals("Master"))
							Sessions.getInstance().audit("Logged out");
						Database.getInstance().DBCloseConnection();
					}
					else e.consume();
				} catch(Exception err) {
					err.printStackTrace();
				}
			});
			
			container.put("login", stgLogin);
			container.put("main", stgMain);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showLogin() {
		try {
			FXMLLoader ldLogin = new FXMLLoader();
			ldLogin.setLocation(this.getClass().getResource("../view/Login.fxml"));
			ldLogin.setController(LoginController.getInstance());
			Scene scene = new Scene(ldLogin.load(),1000,500);
			scene.getStylesheets().add(getClass().getResource("../Root.css").toExternalForm());
			stgLogin.setScene(scene);
		
			stgLogin.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showMain() {
		try {
			FXMLLoader ldMain = new FXMLLoader();
			ldMain.setLocation(this.getClass().getResource("../view/Root.fxml"));
			ldMain.setController(RootController.getInstance());
			Scene scene = new Scene(ldMain.load(), 1300, 700);
			scene.getStylesheets().add(getClass().getResource("../Root.css").toExternalForm());
			stgMain.setScene(scene);
			
			stgMain.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close(String stage) {
		container.get(stage).close();
	}
	
	public Stage get(String stage) {
		return container.get(stage);
	}
	
	public static StageController getInstance() {
		return instance;
	}
}
