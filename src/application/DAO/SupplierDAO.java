package application.DAO;

import java.util.List;

import application.model.Supplier;

public interface SupplierDAO {
	public List<Supplier> getAll();
	public void insert(Supplier s);
	public void update(String query);
	public void delete(String query);
}
