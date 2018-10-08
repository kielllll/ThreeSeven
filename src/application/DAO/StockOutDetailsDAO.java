package application.DAO;

import java.util.List;

import application.model.StockOutDetails;

public interface StockOutDetailsDAO {
	public List<StockOutDetails> getAll();
	public void insert(StockOutDetails s);
	public void update(String query);
	public void delete(String query);
}
