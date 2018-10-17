package application.DAO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import application.model.Vehicle;
import application.util.Database;
import application.util.Date;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class VehicleDAOImpl implements VehicleDAO {
	
	private static VehicleDAOImpl instance = new VehicleDAOImpl();
	
	@Override
	public List<Vehicle> getAll() {
		// TODO Auto-generated method stub
		List<Vehicle> list = new LinkedList<Vehicle>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID");
			
			while(rs.next()) {
				Blob blob = rs.getBlob("image");
				InputStream is = blob.getBinaryStream(1, (int)blob.length());
				BufferedImage bf = ImageIO.read(is);
				Image img = SwingFXUtils.toFXImage(bf, null);
				
				list.add(new Vehicle(rs.getInt("vehicle_ID"), rs.getString("plate_number"), rs.getString("mv_number"), rs.getString("engine_number"), rs.getString("chassis_number"),rs.getString("model_name"), rs.getString("category"), rs.getString("encumbered_to"), rs.getDouble("amount"), Date.parse(rs.getString("maturity_date")), rs.getString("status"), img));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Vehicle v) {
		// TODO Auto-generated method stub
		try {
			int modelID = VehicleModelDAOImpl.getInstance()
					.getAll()
					.parallelStream()
					.filter(m-> m.getDescription().equals(v.getModel()))
					.findFirst()
					.get()
					.getModelID();

			int categoryID = VehicleCategoryDAOImpl.getInstance()
					.getAll()
					.parallelStream()
					.filter(c->c.getDescription().equals(v.getType()))
					.findFirst()
					.get()
					.getCategoryID();


			PreparedStatement st = Database.getInstance().getDBConn().prepareStatement("INSERT INTO vehicle(vehicle_ID,plate_number,mv_number,engine_number,chassis_number,model_ID,category_ID,status,image) VALUES(?,?,?,?,?,?,?,?,?)");
			
			st.setString(1, v.getPlateNumber());
			st.setString(2, v.getMvNumber());
			st.setString(3, v.getEngineNumber());
			st.setString(4, v.getChassisNumber());
			st.setInt(5, modelID);
			st.setInt(6, categoryID);
			st.setString(7, v.getStatus());
//			st.setBlob(8, v.getImage());
			st.executeUpdate();
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	@Override
	public void insert(Vehicle v, File file) {
		// TODO Auto-generated method stub
		try {
			int modelID = VehicleModelDAOImpl.getInstance()
					.getAll()
					.parallelStream()
					.filter(m-> m.getDescription().equals(v.getModel()))
					.findFirst()
					.get()
					.getModelID();

			int categoryID = VehicleCategoryDAOImpl.getInstance()
					.getAll()
					.parallelStream()
					.filter(c->c.getDescription().equals(v.getType()))
					.findFirst()
					.get()
					.getCategoryID();

			FileInputStream fis = new FileInputStream(file);
			
			PreparedStatement st = Database.getInstance().getDBConn().prepareStatement("INSERT INTO vehicles(vehicle_ID,plate_number,mv_number,engine_number,chassis_number,model_ID,category_ID,encumbered_to,amount,maturity_date,status,image) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			
			st.setInt(1, v.getVehicleID());
			st.setString(2, v.getPlateNumber());
			st.setString(3, v.getMvNumber());
			st.setString(4, v.getEngineNumber());
			st.setString(5, v.getChassisNumber());
			st.setInt(6, modelID);
			st.setInt(7, categoryID);
			st.setString(8, v.getEncumberedTo());
			st.setDouble(9, v.getAmount());
			st.setString(10, v.getMaturityDate().toString());
			st.setString(11, v.getStatus());
			st.setBinaryStream(12, (InputStream) fis, (int)file.length());
			st.executeUpdate();
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	@Override
	public void update(String query) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate(query);
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void update(int vehicleID, String plateNumber, String mvNumber, String engineNumber, String chassisNumber, int modelID, int categoryID, String encumberedTo, String insuranceFrom, String insuranceTo, String status, File file) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement st = Database.getInstance().getDBConn().prepareStatement("UPDATE vehicles SET plate_number= ?, mv_number= ?, engine_number= ?, chassis_number= ?, model_ID= ?, category_ID= ?, encumbered_to= ?, insurance_from= ?, insurance_to= ?, status= ?, image= ? WHERE vehicle_ID= ?");
			
			FileInputStream fis = new FileInputStream(file);
			
			st.setString(1, plateNumber);
			st.setString(2, mvNumber);
			st.setString(3, engineNumber);
			st.setString(4, chassisNumber);
			st.setInt(5, modelID);
			st.setInt(6, categoryID);
			st.setString(7, encumberedTo);
			st.setString(8, insuranceFrom);
			st.setString(9, insuranceTo);
			st.setString(10, status);
			st.setBinaryStream(11, (InputStream) fis, (int)file.length());
			st.setInt(12, vehicleID);
			st.executeUpdate();
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public List<String> getAllEncumberedTo() {
		// TODO Auto-generated method stub
		List<String> list = new LinkedList<String>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT DISTINCT v.encumbered_to FROM vehicles v ORDER BY v.encumbered_to ASC");
			
			while(rs.next()) {
				list.add(rs.getString("v.encumbered_to"));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}
	
	@Override
	public void delete(String query) {
		// TODO Auto-generated method stub
		
	}
	
	public static VehicleDAOImpl getInstance() {
		return instance;
	}
}
