package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.PreventiveMaintenance;
import application.util.Database;
import application.util.Date;

public class PreventiveMaintenanceDAOImpl implements PreventiveMaintenanceDAO {
	
	private static PreventiveMaintenanceDAOImpl instance = new PreventiveMaintenanceDAOImpl();
	
	@Override
	public List<PreventiveMaintenance> getAll() {
		// TODO Auto-generated method stub
		List<PreventiveMaintenance> list = new LinkedList<PreventiveMaintenance>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT p.maintenance_ID, v.plate_number, p.description, p.date FROM preventive_maintenance_transactions p INNER JOIN  vehicles v ON p.vehicle_ID = v.vehicle_ID");
			
			while(rs.next()) {
				list.add(new PreventiveMaintenance(rs.getInt("maintenance_ID"), rs.getString("plate_number"), rs.getString("description"), Date.parse(rs.getString("date"))));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(PreventiveMaintenance pm) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO preventive_maintenance_transactions VALUES "+pm.toString());
			
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

	public static PreventiveMaintenanceDAOImpl getInstance() {
		return instance;
	}
}
