package application.DAO;

import java.util.List;

import application.model.PreventiveMaintenance;

public interface PreventiveMaintenanceDAO {
	public List<PreventiveMaintenance> getAll();
	public void insert(PreventiveMaintenance pm);
	public void update(String query);
	public void delete(String query);
}
