package application.DAO;

import java.util.List;

import application.model.VehicleCategory;

public interface VehicleCategoryDAO {
	public List<VehicleCategory> getAll();
	public void insert(VehicleCategory v);
	public void update(String query);
	public void delete(String query);
}
