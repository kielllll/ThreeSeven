package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.Representative;
import application.util.Database;

public class RepresentativeDAOImpl implements RepresentativeDAO {
	
	private static RepresentativeDAOImpl instance = new RepresentativeDAOImpl();
	
	@Override
	public List<Representative> getAll() {
		// TODO Auto-generated method stub
		List<Representative> list = new LinkedList<Representative>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM supplier_representatives");
			
			while(rs.next()) {
				list.add(new Representative(rs.getInt("representative_ID"),rs.getString("name"),rs.getString("contact_number")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Representative rep) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO supplier_representatives VALUES"+rep.toString());
			
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

	public static RepresentativeDAOImpl getInstance() {
		return instance;
	}
}
