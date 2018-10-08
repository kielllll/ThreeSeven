package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.Item;
import application.util.Database;

public class ItemDAOImpl implements ItemDAO {
	private static ItemDAOImpl instance = new ItemDAOImpl();
	
	@Override
	public List<Item> getAll() {
		// TODO Auto-generated method stub
		List<Item> list = new LinkedList<Item>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT i.item_ID, i.name, s.name AS supplier, i.description, i.price, u.description AS unit, st.quantity, i.status FROM suppliers s INNER JOIN items i ON s.supplier_ID = i.supplier_ID INNER JOIN unit_of_measurements u ON i.unit_ID = u.unit_ID INNER JOIN stocks st ON st.item_ID = i.item_id");
			
			while(rs.next()) {
				list.add(new Item(rs.getInt("item_ID"), rs.getString("name"), rs.getString("supplier"), rs.getString("description"), rs.getDouble("price"), rs.getString("unit"), rs.getInt("quantity"), rs.getString("status")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Item i) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO items VALUES"+i.toString());
			
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

	public static ItemDAOImpl getInstance() {
		return instance;
	}
}
