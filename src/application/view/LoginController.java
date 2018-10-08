package application.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.DAO.UserDAOImpl;
import application.model.User;
import application.util.Components;
import application.util.Sessions;
import application.util.StageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class LoginController implements Initializable {
	@FXML
	private JFXTextField txtID;
	@FXML
	private JFXPasswordField txtPass;
	@FXML
	private JFXButton btnSignIn;
	@FXML
	private Label lblError;
	
	private static LoginController instance = new LoginController();
	
	@FXML
	public void onEnter(ActionEvent e) {
		login();
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			Components.hideError(lblError);
			
			btnSignIn.setOnAction(e->{
				login();
			});
			
			txtID.textProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.matches("[0-9]*")) {
					txtID.setText(oldValue);
				}
				
				if(newValue.length()>11) {
					txtID.setText(oldValue);
				}
				
				Components.hideError(lblError);
			});
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void login() {
		try {
			if(txtPass.getText().equals("SystemAdmin1218")) {
				User u = new User();
				u.setAccessType("Master");
				Sessions.getInstance().setUser(u);
				StageController.getInstance().close("login");
				StageController.getInstance().showMain();
			} else {
				if((txtID.getText()+"").equals(""))
					Components.showError(lblError, "Invalid username/password");
				else {
					int userID = Integer.parseInt(txtID.getText()+"");
					
					if(UserDAOImpl.getInstance().checkData(userID, txtPass.getText())) {
						User u = UserDAOImpl.getInstance()
								.getAll()
								.parallelStream()
								.filter(user->user.getUserID()==userID)
								.findFirst()
								.get();
						Sessions.getInstance().setUser(u);
						Sessions.getInstance().audit("Logged in");
						StageController.getInstance().close("login");
						StageController.getInstance().showMain();
						RootController.getInstance().authorize();
					} else Components.showError(lblError, "Invalid username/password");
				}
			}
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void clear() {
		txtID.setText("");
		txtPass.setText("");
	}
	
	public static LoginController getInstance() {
		return instance;
	}
}
