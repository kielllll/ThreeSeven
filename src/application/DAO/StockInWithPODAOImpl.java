package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.StockInWithPO;
import application.util.Database;

public class StockInWithPODAOImpl implements StockInWithPODAO {
	
	private static StockInWithPODAOImpl instance = new StockInWithPODAOImpl();
	
	@Override
	public List<StockInWithPO> getAll() {
		// TODO Auto-generated method stub
		List<StockInWithPO> list = new LinkedList<StockInWithPO>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM stock_in_transactions_with_po");
			
			while(rs.next()) {
				list.add(new StockInWithPO(rs.getInt("stock_in_ID"), rs.getInt("purchase_order_ID")));
			}
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(StockInWithPO s) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO stock_in_transactions_with_po VALUES"+s.toString());
			
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
	
	public static StockInWithPODAOImpl getInstance() {
		return instance;
	}
}
