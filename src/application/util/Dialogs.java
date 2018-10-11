package application.util;

import java.util.HashMap;

import application.view.ConfirmDialogController;
import application.view.PO_ItemsDialogController;
import application.view.Reports_ItemController;
import application.view.Reports_VehicleController;
import application.view.SA_ItemsDialogController;
import application.view.SI_ItemsDialogController;
import application.view.SO_ItemsDialogController;
import application.view.UnitDialogController;
import application.view.Vehicle_CategoryController;
import application.view.Vehicle_ModelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Dialogs {
	private static Dialogs instance = new Dialogs();
	
	private Stage stgPO_Item;
	private Stage stgUnit;
	private Stage stgConfirm;
	private Stage stgModel;
	private Stage stgCategory;
	private Stage stgSI_Item;
	private Stage stgSO_Item;
	private Stage stgSA_Item;
	private Stage stgReports_Item;
	private Stage stgReports_Vehicle;
	
	private HashMap<String, Stage> container = new HashMap<>();
	
	private double xOffSet = 0;
	private double yOffSet = 0;
	private int num = 0;
	
	public Dialogs() {
		initStages();
	}
	
	public void initStages() {
		try {
			// for item dialog
			stgPO_Item = new Stage();
			configStage(stgPO_Item);
			
			// for unit dialog
			stgUnit = new Stage();
			configStage(stgUnit);
			
			// for confirm dialog
			stgConfirm = new Stage();
			configStage(stgConfirm);
			
			// for model dialog
			stgModel =  new Stage();
			configStage(stgModel);
			
			// for category dialog
			stgCategory = new Stage();
			configStage(stgCategory);
			
			// for stock in transaction dialog
			stgSI_Item = new Stage();
			configStage(stgSI_Item);
			
			// for stock out transaction dialog
			stgSO_Item = new Stage();
			configStage(stgSO_Item);
			
			// for stock adjustment transaction dialog
			stgSA_Item = new Stage();
			configStage(stgSA_Item);
			
			// for item reports dialog
			stgReports_Item = new Stage();
			configStage(stgReports_Item);
			
			// for vehicle reports dialog
			stgReports_Vehicle = new Stage();
			configStage(stgReports_Vehicle);
			
			container.put("PO_Item", stgPO_Item);
			container.put("Unit", stgUnit);
			container.put("Confirm", stgConfirm);
			container.put("Model", stgModel);
			container.put("Category", stgCategory);
			container.put("SI_Item", stgSI_Item);
			container.put("SO_Item", stgSO_Item);
			container.put("SA_Item", stgSA_Item);
			container.put("Reports_Item", stgReports_Item);
			container.put("Reports_Vehicle", stgReports_Vehicle);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showPO_Item() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("../view/PO_ItemsDialog.fxml"));
			loader.setController(PO_ItemsDialogController.getInstance());
			loadAndSetAction(stgPO_Item,loader);
			stgPO_Item.showAndWait();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void showUnit() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("../view/UnitDialog.fxml"));
			loader.setController(UnitDialogController.getInstance());
			loadAndSetAction(stgUnit,loader);
			stgUnit.showAndWait();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public int showConfirm(String header, String content) {
		num = 0;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("../view/ConfirmDialog.fxml"));
			loader.setController(ConfirmDialogController.getInstance());
			loadAndSetAction(stgConfirm, loader, 400, 250);
			ConfirmDialogController.getInstance().setHeader(header);
			ConfirmDialogController.getInstance().setContent(content);
			stgConfirm.showAndWait();
			num = ConfirmDialogController.getInstance().getObserver();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return num;
	}
	
	public void showModel() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("../view/Vehicle_Model.fxml"));
			loader.setController(Vehicle_ModelController.getInstance());
			loadAndSetAction(stgModel,loader);
			stgModel.showAndWait();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void showCategory() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("../view/Vehicle_Category.fxml"));
			loader.setController(Vehicle_CategoryController.getInstance());
			loadAndSetAction(stgCategory,loader);
			stgCategory.showAndWait();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void showSI_Item() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("../view/SI_ItemsDialog.fxml"));
			loader.setController(SI_ItemsDialogController.getInstance());
			loadAndSetAction(stgSI_Item,loader);
			stgSI_Item.showAndWait();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void showSO_Item() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("../view/SO_ItemsDialog.fxml"));
			loader.setController(SO_ItemsDialogController.getInstance());
			loadAndSetAction(stgSO_Item,loader);
			stgSO_Item.showAndWait();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void showSA_Item() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("../view/SA_ItemsDialog.fxml"));
			loader.setController(SA_ItemsDialogController.getInstance());
			loadAndSetAction(stgSA_Item,loader);
			stgSA_Item.showAndWait();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void showReports_Item() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("../view/Reports_Item.fxml"));
			loader.setController(Reports_ItemController.getInstance());
			loadAndSetAction(stgReports_Item,loader,400,250);
			stgReports_Item.showAndWait();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void showReports_Vehicle() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("../view/Reports_Vehicle.fxml"));
			loader.setController(Reports_VehicleController.getInstance());
			loadAndSetAction(stgReports_Vehicle,loader,400,250);
			stgReports_Vehicle.showAndWait();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void loadAndSetAction(Stage stg, FXMLLoader loader) {
		try {
			BorderPane bp = loader.load();
			
			bp.setOnMousePressed(e->{
				xOffSet = e.getSceneX();
                yOffSet = e.getSceneY();
			});
			
			bp.setOnMouseDragged(e->{
				stg.setX(e.getScreenX()-xOffSet);
				stg.setY(e.getScreenY()-yOffSet);
			});
			
			Scene scene = new Scene(bp,500,500);
			scene.setFill(Color.TRANSPARENT);
			
			stg.setScene(scene);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void loadAndSetAction(Stage stg, FXMLLoader loader, int width, int length) {
		try {
			BorderPane bp = loader.load();
			
			bp.setOnMousePressed(e->{
				xOffSet = e.getSceneX();
                yOffSet = e.getSceneY();
			});
			
			bp.setOnMouseDragged(e->{
				stg.setX(e.getScreenX()-xOffSet);
				stg.setY(e.getScreenY()-yOffSet);
			});
			
			Scene scene = new Scene(bp,width,length);
			scene.getStylesheets().add(getClass().getResource("../Root.css").toExternalForm());
			scene.setFill(Color.TRANSPARENT);
			
			stg.setScene(scene);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void configStage(Stage stg) {
		stg.setResizable(false);
		stg.initModality(Modality.WINDOW_MODAL);
		stg.initOwner(StageController.getInstance().get("main"));
		stg.initStyle(StageStyle.TRANSPARENT);
	}
	
	public void close(String stage) {
		container.get(stage).close();
		xOffSet = 0;
		yOffSet = 0;
	}
	
	public static Dialogs getInstance() {
		return instance;
	}
}
