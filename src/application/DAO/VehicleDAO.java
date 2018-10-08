package application.DAO;

import java.io.File;
import java.util.List;

import application.model.Vehicle;

public interface VehicleDAO {
	public List<Vehicle> getAll();
	public void insert(Vehicle v);
	public void insert(Vehicle v, File file);
	public void update(String query);
	public void delete(String query);
}
