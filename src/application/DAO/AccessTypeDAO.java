package application.DAO;

import java.util.List;

import application.model.AccessType;

public interface AccessTypeDAO {
	public List<AccessType> getAll();
	public void insert(AccessType at);
	public void update(String query);
	public void delete(String query);
}
