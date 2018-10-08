package application.DAO;

import java.util.List;

import application.model.StockAdjustmentDetails;

public interface StockAdjustmentDetailsDAO {
	public List<StockAdjustmentDetails> getAll();
	public void insert(StockAdjustmentDetails s);
	public void update(String query);
	public void delete(String query);
}
