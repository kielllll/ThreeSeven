package application.DAO;

import java.util.List;

import application.model.PODetails;

public interface PODetailsDAO {
	public List<PODetails> getAll();
	public void insert(PODetails po);
	public void update(String query);
	public void delete(String query);
}
