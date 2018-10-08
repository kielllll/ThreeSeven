package application.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class ReportsController implements Initializable {
	@FXML
	private Tab tabDaily;
	
	private static ReportsController instance = new ReportsController();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		initScreens();
	}

	public void initScreens() {
		try {
			FXMLLoader ldDaily = new FXMLLoader();
			ldDaily.setLocation(this.getClass().getResource("DailyReports.fxml"));
			ldDaily.setController(DailyReportsController.getInstance());
			
			tabDaily.setContent(ldDaily.load());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ReportsController getInstance() {
		return instance;
	}
}
