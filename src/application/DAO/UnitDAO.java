package application.DAO;

import java.util.List;

import application.model.Unit;

public interface UnitDAO {
	public List<Unit> getAll();
	public void insert(Unit u);
	public void update(String query);
	public void delete(String query);
}
