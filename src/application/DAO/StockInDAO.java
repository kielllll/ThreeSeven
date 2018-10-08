package application.DAO;

import java.util.List;

import application.model.StockIn;

public interface StockInDAO {
	public List<StockIn> getAll();
	public void insert(StockIn s);
	public void update(String query);
	public void delete(String query);
}
