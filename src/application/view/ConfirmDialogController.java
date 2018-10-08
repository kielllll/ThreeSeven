package application.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.util.Dialogs;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ConfirmDialogController implements Initializable {
	@FXML
    private Label lblHeader;
    @FXML
    private Label lblContent;
    @FXML
    private JFXButton btnOK;
    @FXML
    private JFXButton btnCancel;
    
    private int observer = 0;
    private static ConfirmDialogController instance = new ConfirmDialogController();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		btnOK.setOnAction(e->{
			observer = 0;
			Dialogs.getInstance().close("Confirm");
		});
		
		btnCancel.setOnAction(e->{
			observer = 1;
			Dialogs.getInstance().close("Confirm");
		});

	}
	
	public void setHeader(String header) {
		lblHeader.setText(header);
	}
	
	public void setContent(String content) {
		lblContent.setText(content);
	}
	
	public JFXButton getBtnOK() {
		return btnOK;
	}
	
	public JFXButton getBtnCancel() {
		return btnCancel;
	}
	
	public int getObserver() {
		return observer;
	}
	
	public static ConfirmDialogController getInstance() {
		return instance;
	}
}
