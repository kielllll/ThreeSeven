package application.util;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import application.DAO.ItemDAOImpl;
import application.DAO.StockDAOImpl;
import application.DAO.VehicleDAOImpl;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class Components {
	
	public static void switchScreen(int node, ObservableList<Node> children) {
		for(Node n : children) {
			n.setVisible(false);
		}
		
		children.get(node).setVisible(true);
	}
	
	public static void switchScreen(FXMLLoader loader, ObservableList<Node> children) {
		try {
			Task<Parent> loadTask = new Task<Parent>() {

				@Override
				protected Parent call() throws Exception {
					// TODO Auto-generated method stub
					FXMLLoader ld = new FXMLLoader();
					ld.setLocation(loader.getLocation());
					ld.setController(loader.getController());
					return ld.load();
				}
				
			};
			
			loadTask.setOnSucceeded(e -> {
				children.clear();
				children.add(loadTask.getValue());
			});
			
			loadTask.setOnFailed(e -> loadTask.getException().printStackTrace());
			
			Thread thread = new Thread(loadTask);
			thread.start();
			
//			children.add(ld.load());
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public static void showError(Label lblError, String error) {
		lblError.setText(error);
		lblError.setVisible(true);
	}
	
	public static void hideError(Label lblError) {
		lblError.setVisible(false);
	}
	
	public static boolean isItemExisting(String name) {
		return ItemDAOImpl.getInstance()
				.getAll()
				.stream()
				.filter(i->i.getName().equalsIgnoreCase(name))
				.findFirst()
				.isPresent();
	}
	
	public static boolean isPlateNumExisting(String plateNumber) {
		return VehicleDAOImpl.getInstance()
				.getAll()
				.stream()
				.filter(i->i.getPlateNumber().equalsIgnoreCase(plateNumber))
				.findFirst()
				.isPresent();
	}
	
	public static int getItemID(String name) {
		return ItemDAOImpl.getInstance()
						.getAll()
						.stream()
						.filter(i->i.getName().equals(name))
						.findFirst()
						.get()
						.getItemID();
	}
	
	public static boolean isEmpty(String name) {
		return (StockDAOImpl.getInstance()
							.getAll()
							.stream()
							.filter(i->i.getName().equals(name))
							.findFirst()
							.get()
							.getQuantity()==0)?true:false;
	}
	
	public static Image getImage(String filePath) {
		BufferedImage bf = null;
		try {
			bf = ImageIO.read( Paths.get(filePath).toRealPath().toFile());
		} catch(Exception err) {
			err.printStackTrace();
		}
		return SwingFXUtils.toFXImage(bf, null);
	}
}
