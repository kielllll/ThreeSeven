package application.DAO;

import java.util.List;

import application.model.PurchaseOrder;

public interface PurchaseOrderDAO {
	public List<PurchaseOrder> getAll();
	public void insert(PurchaseOrder p);
	public void update(String query);
	public void delete(String query);
}
