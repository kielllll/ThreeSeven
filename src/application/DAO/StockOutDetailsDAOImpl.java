package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.StockOutDetails;
import application.util.Database;

public class StockOutDetailsDAOImpl implements StockOutDetailsDAO {
	
	private static StockOutDetailsDAOImpl instance = new StockOutDetailsDAOImpl();
	
	@Override
	public List<StockOutDetails> getAll() {
		// TODO Auto-generated method stub
		List<StockOutDetails> list = new LinkedList<StockOutDetails>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT s.stock_out_ID, i.name, s.quantity FROM stock_out_transaction_details s INNER JOIN items i ON i.item_ID = s.item_ID");
			
			while(rs.next()) {
				list.add(new StockOutDetails(rs.getInt("stock_out_ID"), rs.getString("name"), rs.getInt("quantity")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(StockOutDetails s) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO stock_out_transaction_details VALUES "+s.toString());
			
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
	
	public static StockOutDetailsDAOImpl getInstance() {
		return instance;
	}
}
