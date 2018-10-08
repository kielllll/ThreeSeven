package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.Unit;
import application.util.Database;

public class UnitDAOImpl implements UnitDAO {
	
	private static UnitDAOImpl instance = new UnitDAOImpl();
	
	@Override
	public List<Unit> getAll() {
		// TODO Auto-generated method stub
		List<Unit> list = new LinkedList<Unit>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM unit_of_measurements");
			
			while(rs.next()) {
				list.add(new Unit(rs.getInt("unit_ID"), rs.getString("description")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Unit u) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO unit_of_measurements VALUES"+u.toString());
			
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

	public static UnitDAOImpl getInstance() {
		return instance;
	}
}
