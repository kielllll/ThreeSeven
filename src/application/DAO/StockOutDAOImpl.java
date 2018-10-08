package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.StockOut;
import application.util.Database;
import application.util.Date;

public class StockOutDAOImpl implements StockOutDAO {
	
	private static StockOutDAOImpl instance = new StockOutDAOImpl();
	
	@Override
	public List<StockOut> getAll() {
		// TODO Auto-generated method stub
		List<StockOut> list = new LinkedList<StockOut>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM stock_out_transactions");
			
			while(rs.next()) {
				list.add(new StockOut(rs.getInt("stock_out_ID"), Date.parse(rs.getString("date")), rs.getString("released_to"), rs.getString("remarks"), rs.getString("status")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(StockOut s) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO stock_out_transactions VALUES"+s.toString());
			
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

	public static StockOutDAOImpl getInstance() {
		return instance;
	}
}
