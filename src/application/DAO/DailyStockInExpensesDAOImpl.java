package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.DailyStockInExpenses;
import application.util.Database;
import application.util.Date;

public class DailyStockInExpensesDAOImpl implements DailyStockInExpensesDAO {
	
	private static DailyStockInExpensesDAOImpl instance = new DailyStockInExpensesDAOImpl();
	
	@Override
	public List<DailyStockInExpenses> getAll() {
		// TODO Auto-generated method stub
		List<DailyStockInExpenses> list = new LinkedList<DailyStockInExpenses>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT st.stock_in_ID, st.stock_in_date, s.name, st.invoice_num, st.invoice_date, (SELECT SUM(std.quantity*i.price) FROM stock_in_transaction_details std INNER JOIN items i ON std.item_ID = i.item_ID WHERE std.stock_in_ID = st.stock_in_ID) AS amount FROM suppliers s INNER JOIN stock_in_transactions st ON s.supplier_ID = st.supplier_ID");
			
			while(rs.next()) {
				list.add(new DailyStockInExpenses(rs.getInt("stock_in_ID"), Date.parse(rs.getString("stock_in_date")), rs.getString("name"), rs.getInt("invoice_num"), Date.parse(rs.getString("invoice_date")), rs.getDouble("amount")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}
	
	public static DailyStockInExpensesDAOImpl getInstance() {
		return instance;
	}
}
