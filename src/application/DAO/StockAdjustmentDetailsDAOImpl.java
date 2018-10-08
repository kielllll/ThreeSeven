package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.StockAdjustmentDetails;
import application.util.Database;

public class StockAdjustmentDetailsDAOImpl implements StockAdjustmentDetailsDAO {
	
	private static StockAdjustmentDetailsDAOImpl instance = new StockAdjustmentDetailsDAOImpl();
	
	@Override
	public List<StockAdjustmentDetails> getAll() {
		// TODO Auto-generated method stub
		List<StockAdjustmentDetails> list = new LinkedList<StockAdjustmentDetails>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT s.stock_adjustment_ID, i.name, s.system_quantity, s.new_quantity FROM items i INNER JOIN stock_adjustment_transaction_details s ON i.item_ID=s.item_ID");
			
			while(rs.next()) {
				list.add(new StockAdjustmentDetails(rs.getInt("stock_adjustment_ID"), rs.getString("name"), rs.getInt("system_quantity"), rs.getInt("new_quantity")));
			}
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(StockAdjustmentDetails s) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO stock_adjustment_transaction_details VALUES "+s.toString());
			
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

	public static StockAdjustmentDetailsDAOImpl getInstance() {
		return instance;
	}
}
