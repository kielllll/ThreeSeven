package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.AccessType;
import application.util.Database;

public class AccessTypeDAOImpl implements AccessTypeDAO {
	
	private static AccessTypeDAOImpl instance = new AccessTypeDAOImpl();
	
	@Override
	public List<AccessType> getAll() {
		List<AccessType> list = new LinkedList<AccessType>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM access_types");
			
			while(rs.next()) {
				list.add(new AccessType(rs.getInt("access_type_ID"),rs.getString("description")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(AccessType at) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String query) {
		// TODO Auto-generated method stub
		
	}
	
	public static AccessTypeDAOImpl getInstance() {
		return instance;
	}
}
