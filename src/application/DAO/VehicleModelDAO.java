package application.DAO;

import java.util.List;

import application.model.VehicleModel;

public interface VehicleModelDAO {
	public List<VehicleModel> getAll();
	public void insert(VehicleModel v);
	public void update(String query);
	public void delete(String query);
}
