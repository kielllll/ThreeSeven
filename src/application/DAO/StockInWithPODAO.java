package application.DAO;

import java.util.List;

import application.model.StockInWithPO;

public interface StockInWithPODAO {
	public List<StockInWithPO> getAll();
	public void insert(StockInWithPO s);
	public void update(String query);
	public void delete(String query);
}
