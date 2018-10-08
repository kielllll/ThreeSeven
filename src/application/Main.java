package application;
	
import application.util.Database;
import application.util.StageController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Database.getInstance().DBSetConnection();
			StageController.getInstance().initStages();
			StageController.getInstance().showLogin();	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}