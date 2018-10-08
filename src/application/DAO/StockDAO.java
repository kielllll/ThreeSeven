package application.DAO;

import java.util.List;

import application.model.Stock;

public interface StockDAO {
	public List<Stock> getAll();
	public void insert(Stock s);
	public void update(String query);
	public void delete(String query);
}
