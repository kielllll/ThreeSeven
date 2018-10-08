package application.DAO;

import java.util.List;

import application.model.Representative;

public interface RepresentativeDAO {
	public List<Representative> getAll();
	public void insert(Representative rep);
	public void update(String query);
	public void delete(String query);
}
