package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.VehicleModel;
import application.util.Database;

public class VehicleModelDAOImpl implements VehicleModelDAO {
	
	private static VehicleModelDAOImpl instance = new VehicleModelDAOImpl();
	
	@Override
	public List<VehicleModel> getAll() {
		// TODO Auto-generated method stub
		List<VehicleModel> list = new LinkedList<VehicleModel>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM models");
			
			while(rs.next()) {
				list.add(new VehicleModel(rs.getInt("model_ID"), rs.getString("description")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(VehicleModel v) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO models VALUES"+v.toString());
			
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
	
	public static VehicleModelDAOImpl getInstance() {
		return instance;
	}
}
