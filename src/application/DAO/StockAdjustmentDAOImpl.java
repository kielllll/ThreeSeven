package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.StockAdjustment;
import application.util.Database;
import application.util.Date;

public class StockAdjustmentDAOImpl implements StockAdjustmentDAO {
	
	private static StockAdjustmentDAOImpl instance = new StockAdjustmentDAOImpl();
	
	@Override
	public List<StockAdjustment> getAll() {
		// TODO Auto-generated method stub
		List<StockAdjustment> list = new LinkedList<StockAdjustment>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM stock_adjustment_transactions");
			
			while(rs.next()) {
				list.add(new StockAdjustment(rs.getInt("stock_adjustment_ID"), Date.parse(rs.getString("date_encoded")), rs.getString("comments"), rs.getString("status")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			
		}
		return list;
	}

	@Override
	public void insert(StockAdjustment s) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO stock_adjustment_transactions VALUES "+s.toString());
			
			st.close();
		} catch(Exception err) {
			
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
			
		}
	}

	@Override
	public void delete(String query) {
		// TODO Auto-generated method stub
		
	}
	
	public static StockAdjustmentDAOImpl getInstance() {
		return instance;
	}
}
