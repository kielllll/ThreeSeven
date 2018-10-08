package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.Stock;
import application.util.Database;

public class StockDAOImpl implements StockDAO {
	
	private static StockDAOImpl instance = new StockDAOImpl();
	
	@Override
	public List<Stock> getAll() {
		// TODO Auto-generated method stub
		List<Stock> list = new LinkedList<Stock>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT i.item_ID, i.name, s.quantity FROM items i INNER JOIN stocks s ON i.item_ID=s.item_ID");
			
			while(rs.next()) {
				list.add(new Stock(rs.getInt("item_ID"), rs.getString("name"), rs.getInt("quantity")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Stock s) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO stocks VALUES"+s.toString());
			
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
	
	public static StockDAOImpl getInstance() {
		return instance;
	}
}
