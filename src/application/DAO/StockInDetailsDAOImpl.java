package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.StockInDetails;
import application.util.Database;

public class StockInDetailsDAOImpl implements StockInDetailsDAO{
	
	private static StockInDetailsDAOImpl instance = new StockInDetailsDAOImpl();
	
	@Override
	public List<StockInDetails> getAll() {
		// TODO Auto-generated method stub
		List<StockInDetails> list = new LinkedList<StockInDetails>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT s.stock_in_ID, i.name, s.quantity, i.price, (s.quantity*i.price) as subTotal FROM stock_in_transaction_details s INNER JOIN items i ON s.item_ID = i.item_ID");
			
			while(rs.next()) {
				list.add(new StockInDetails(rs.getInt("stock_in_ID"), rs.getString("name"), rs.getInt("quantity"), rs.getDouble("price"), rs.getDouble("subTotal")));
			}
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(StockInDetails s) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO stock_in_transaction_details VALUES"+s.toString());
			
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

	public static StockInDetailsDAOImpl getInstance() {
		return instance;
	}
}
