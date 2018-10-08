package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.PODetails;
import application.util.Database;

public class PODetailsDAOImpl implements PODetailsDAO {

	private static PODetailsDAOImpl instance = new PODetailsDAOImpl();
	
	@Override
	public List<PODetails> getAll() {
		// TODO Auto-generated method stub
		List<PODetails> list = new LinkedList<PODetails>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT pod.purchase_order_ID, i.name, pod.quantity, i.price, (pod.quantity*i.price) AS subTotal FROM purchase_order_details pod INNER JOIN items i ON pod.item_ID=i.item_ID");
			
			while(rs.next()) {
				list.add(new PODetails(rs.getInt("purchase_order_ID"), rs.getString("name"), rs.getInt("quantity"), rs.getDouble("price"), rs.getDouble("subTotal")));
			}
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(PODetails po) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO purchase_order_details VALUES"+po.toString());
			
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
	
	public static PODetailsDAOImpl getInstance() {
		return instance;
	}
}
