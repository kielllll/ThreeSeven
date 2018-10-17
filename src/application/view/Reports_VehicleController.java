package application.view;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import application.DAO.VehicleCategoryDAOImpl;
import application.DAO.VehicleDAOImpl;
import application.util.Dialogs;
import application.util.ReportsUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class Reports_VehicleController implements Initializable {
	private static Reports_VehicleController instance = new Reports_VehicleController();
	
	@FXML
    private JFXComboBox<String> cbSelect;
	@FXML
    private JFXComboBox<String> cbType;
	@FXML
    private JFXComboBox<String> cbEncumberedTo;
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
			
			cbType.getItems().add("All");
			cbEncumberedTo.getItems().add("All");
			
			VehicleCategoryDAOImpl.getInstance()
								.getAll()
								.stream()
								.sorted((o1,o2) -> o1.getDescription().compareTo(o2.getDescription()))
								.forEach(e->cbType.getItems().add(e.getDescription()));
			
			List<String> listEncumberedTo = VehicleDAOImpl.getInstance().getAllEncumberedTo();
			
			listEncumberedTo.stream()
						.forEach(e->cbEncumberedTo.getItems().add(e));
			
			cbType.setValue("All");
			cbEncumberedTo.setValue("All");
			
			btnOK.setOnAction(e->{
				try {
					String filePath = "Reports/Vehicle.jrxml";
					File file = new File(filePath);
					ReportsUtil.getInstance().showVehicleReport(file,cbSelect.getValue(),cbType.getValue(),cbEncumberedTo.getValue());
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
