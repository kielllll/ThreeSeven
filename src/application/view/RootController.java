package application.view;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;

import application.DAO.AuditLogDAOImpl;
import application.model.AuditLog;
import application.model.User;
import application.util.Authorize;
import application.util.Components;
import application.util.Dialogs;
import application.util.Sessions;
import application.util.StageController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RootController implements Initializable {
	@FXML
	private JFXHamburger hb;
	@FXML
	private StackPane spRoot;
	@FXML
	private JFXDrawer drawer;
	
	private static RootController instance = new RootController();
	
	private VBox sideNav;
	private ObservableList<Node> children;
	private HashMap<String, FXMLLoader> container = new HashMap<>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			sideNav = FXMLLoader.load(getClass().getResource("SideNav.fxml"));
			
			initScreens();
			drawer.setSidePane(sideNav);
			drawer.setVisible(false);
			
			for(Node node : sideNav.getChildren()) {
				node.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
					switch(node.getAccessibleText()) {
						case "items":	Components.switchScreen(container.get("Items"),children); 
								break;
						case "stocks": Components.switchScreen(container.get("Stocks"), children);
								break;
						case "suppliers": Components.switchScreen(container.get("Suppliers"), children);
								break;
						case "Purchase Orders": Components.switchScreen(container.get("Purchase Orders"), children);
								break;
						case "reports": Components.switchScreen(container.get("Reports"), children);
								break;
						case "vehicles": Components.switchScreen(container.get("Vehicles"), children);
								break;
						case "Preventive Maintenance": Components.switchScreen(container.get("Preventive Maintenance"), children);
								break;
						case "users": Components.switchScreen(container.get("Users"), children);
								break;
						case "audit": Components.switchScreen(container.get("Audit Log"), children);
								break;
						case "logout": if(Dialogs.getInstance().showConfirm("Logout", "Confirm log out?")==0) {
											User u = Sessions.getInstance().getUser();
											SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
											String time = sdf.format(new Date());
											if(!Sessions.getInstance().getUser().getAccessType().equals("Master")) {
												AuditLog a = new AuditLog(AuditLogDAOImpl.getInstance().getAll().size()+1, LocalDate.now(), time, u.getUserID(), u.getFirstname(), u.getLastname(), u.getAccessType(), "Logged out");
												AuditLogDAOImpl.getInstance().insert(a);
											}
											StageController.getInstance().close("main");
										    StageController.getInstance().showLogin();
										    LoginController.getInstance().clear();
									   }
									   
									   break;
						default:
					}
				});
			}
	
			hb.setOnMousePressed(e->{
				if(drawer.isOpened()) {
					drawer.close();
				}
				else {
					drawer.setVisible(true);
					drawer.open();
				}
			});
			
			drawer.setOnDrawerClosed(e->{
				drawer.setVisible(false);
			});
		
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void initScreens() {
		try {
			FXMLLoader ldItems = new FXMLLoader();
			ldItems.setLocation(this.getClass().getResource("Items.fxml"));
			ldItems.setController(ItemsController.getInstance());
			
			FXMLLoader ldStocks = new FXMLLoader();
			ldStocks.setLocation(this.getClass().getResource("Stocks.fxml"));
			ldStocks.setController(StocksController.getInstance());
			
			FXMLLoader ldSuppliers = new FXMLLoader();
			ldSuppliers.setLocation(this.getClass().getResource("Suppliers.fxml"));
			ldSuppliers.setController(SuppliersController.getInstance());
			
			FXMLLoader ldPO = new FXMLLoader();
			ldPO.setLocation(this.getClass().getResource("PurchaseOrder.fxml"));
			ldPO.setController(PurchaseOrderController.getInstance());
			
			FXMLLoader ldReports = new FXMLLoader();
			ldReports.setLocation(this.getClass().getResource("Reports.fxml"));
			ldReports.setController(ReportsController.getInstance());
			
			FXMLLoader ldVehicles = new FXMLLoader();
			ldVehicles.setLocation(this.getClass().getResource("Vehicles.fxml"));
			ldVehicles.setController(VehiclesController.getInstance());
			
			FXMLLoader ldPM = new FXMLLoader();
			ldPM.setLocation(this.getClass().getResource("PreventiveMaintenance.fxml"));
			ldPM.setController(PreventiveMaintenanceController.getInstance());
			
			FXMLLoader ldUsers = new FXMLLoader();
			ldUsers.setLocation(this.getClass().getResource("Users.fxml"));
			ldUsers.setController(UsersController.getInstance());
			
			FXMLLoader ldAuditLog = new FXMLLoader();
			ldAuditLog.setLocation(this.getClass().getResource("AuditLog.fxml"));
			ldAuditLog.setController(AuditLogController.getInstance());
			
			children = spRoot.getChildren();
			
			container.put("Items", ldItems);
			container.put("Stocks", ldStocks);
			container.put("Suppliers", ldSuppliers);
			container.put("Purchase Orders", ldPO);
			container.put("Reports", ldReports);
			container.put("Vehicles", ldVehicles);
			container.put("Preventive Maintenance", ldPM);
			container.put("Users", ldUsers);
			container.put("Audit Log", ldAuditLog);
			
			Components.switchScreen(container.get("Items"), children);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void authorize() {
		if(!Sessions.getInstance().getUser().getAccessType().equals("Master")) {
			if(Authorize.getInstance().isEmployee(Sessions.getInstance().getUser().getAccessType())) {
				for(Node node : sideNav.getChildren()) {
					if(node.getAccessibleText().equals("users")||node.getAccessibleText().equals("audit")) {
						node.setDisable(true);
					}
				}
			}
		}
	}
	
	public static RootController getInstance() {
		return instance;
	}
}
