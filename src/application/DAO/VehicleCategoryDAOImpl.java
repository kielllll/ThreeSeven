package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.VehicleCategory;
import application.util.Database;

public class VehicleCategoryDAOImpl implements VehicleCategoryDAO {
	
	private static VehicleCategoryDAOImpl instance = new VehicleCategoryDAOImpl();
	
	@Override
	public List<VehicleCategory> getAll() {
		// TODO Auto-generated method stub
		List<VehicleCategory> list = new LinkedList<VehicleCategory>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM vehicle_categories");
			
			while(rs.next()) {
				list.add(new VehicleCategory(rs.getInt("category_ID"), rs.getString("description")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(VehicleCategory v) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO vehicle_categories VALUES"+v.toString());
			
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

	@Override
	public void delete(String query) {
		// TODO Auto-generated method stub
		
	}
	
	public static VehicleCategoryDAOImpl getInstance() {
		return instance;
	}
}
