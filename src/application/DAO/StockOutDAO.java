package application.DAO;

import java.util.List;

import application.model.StockOut;

public interface StockOutDAO {
	public List<StockOut> getAll();
	public void insert(StockOut s);
	public void update(String query);
	public void delete(String query);
}
