package application.DAO;

import java.util.List;

import application.model.StockInDetails;

public interface StockInDetailsDAO {
	public List<StockInDetails> getAll();
	public void insert(StockInDetails s);
	public void update(String query);
	public void delete(String query);
}
