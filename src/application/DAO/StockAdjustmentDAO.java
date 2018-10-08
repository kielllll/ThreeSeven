package application.DAO;

import java.util.List;

import application.model.StockAdjustment;

public interface StockAdjustmentDAO {
	public List<StockAdjustment> getAll();
	public void insert(StockAdjustment s);
	public void update(String query);
	public void delete(String query);
}
