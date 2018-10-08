package application.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class StocksController implements Initializable {
	@FXML
	private Tab tabStockIn;
	@FXML
	private Tab tabStockOut;
	@FXML
	private Tab tabStockAdj;
	@FXML
	private JFXTextField txtSearch;
	
	private static StocksController instance = new StocksController();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		initScreens();
	}
	
	public void initScreens() {
		try {
			FXMLLoader ldStockIn = new FXMLLoader();
			ldStockIn.setLocation(this.getClass().getResource("StockIn.fxml"));
			ldStockIn.setController(StockInController.getInstance());
			
			FXMLLoader ldStockOut = new FXMLLoader();
			ldStockOut.setLocation(this.getClass().getResource("StockOut.fxml"));
			ldStockOut.setController(StockOutController.getInstance());
			
			FXMLLoader ldStockAdj = new FXMLLoader();
			ldStockAdj.setLocation(this.getClass().getResource("StockAdjustment.fxml"));
			ldStockAdj.setController(StockAdjustmentController.getInstance());
			
			tabStockIn.setContent(ldStockIn.load());
			tabStockOut.setContent(ldStockOut.load());
			tabStockAdj.setContent(ldStockAdj.load());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static StocksController getInstance() {
		return instance;
	}
}
