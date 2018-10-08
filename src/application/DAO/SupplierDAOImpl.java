package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.Supplier;
import application.util.Database;

public class SupplierDAOImpl implements SupplierDAO {

	private static SupplierDAOImpl instance = new SupplierDAOImpl();
	@Override
	public List<Supplier> getAll() {
		// TODO Auto-generated method stub
		List<Supplier> list = new LinkedList<Supplier>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT s.supplier_ID, s.name, r.name as rep_name, r.contact_number, s.location, s.status FROM supplier_representatives r INNER JOIN suppliers s ON r.representative_ID=s.representative_ID");
			
			while(rs.next()) {
				list.add(new Supplier(rs.getInt("supplier_ID"),rs.getString("name"),rs.getString("rep_name"),rs.getString("contact_number"),rs.getString("location"),rs.getString("status")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Supplier s) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO suppliers VALUES"+s.toString());
			
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
	
	public static SupplierDAOImpl getInstance() {
		return instance;
	}
}
