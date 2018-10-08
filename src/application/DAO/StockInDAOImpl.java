package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.StockIn;
import application.util.Database;
import application.util.Date;

public class StockInDAOImpl implements StockInDAO {
	
	private static StockInDAOImpl instance = new StockInDAOImpl();
	
	@Override
	public List<StockIn> getAll() {
		// TODO Auto-generated method stub
		List<StockIn> list = new LinkedList<StockIn>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT si.stock_in_ID, si.stock_in_date, s.name, si.invoice_num, si.invoice_date FROM stock_in_transactions si INNER JOIN suppliers s ON si.supplier_ID=s.supplier_ID");
			
			while(rs.next()) {
				list.add(new StockIn(rs.getInt("stock_in_ID"), Date.parse(rs.getString("stock_in_date")), rs.getString("name"), rs.getInt("invoice_num"), Date.parse(rs.getString("invoice_date"))));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(StockIn s) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO stock_in_transactions VALUES"+s.toString());
			
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
	
	public static StockInDAOImpl getInstance() {
		return instance;
	}
}
