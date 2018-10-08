package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.PurchaseOrder;
import application.util.Database;
import application.util.Date;

public class PurchaseOrderDAOImpl implements PurchaseOrderDAO {
	private static PurchaseOrderDAOImpl instance = new PurchaseOrderDAOImpl();
	
	@Override
	public List<PurchaseOrder> getAll() {
		// TODO Auto-generated method stub
		List<PurchaseOrder> list = new LinkedList<PurchaseOrder>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT p.purchase_order_ID, p.purchase_order_date, s.name, p.date_needed, p.status FROM purchase_orders p INNER JOIN suppliers s ON s.supplier_ID=p.supplier_ID");
			
			while(rs.next()) {
				list.add(new PurchaseOrder(rs.getInt("purchase_order_ID"), Date.parse(rs.getString("purchase_order_date")), rs.getString("name"), Date.parse(rs.getString("date_needed")),rs.getString("status")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(PurchaseOrder p) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO purchase_orders VALUES"+p.toString());
			
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

	public static PurchaseOrderDAOImpl getInstance() {
		return instance;
	}
}
