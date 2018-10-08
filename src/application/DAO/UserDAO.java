package application.DAO;

import java.util.List;

import application.model.User;

public interface UserDAO {
	public List<User> getAll();
	public void insert(User user);
	public void update(String query);
	public void delete(String query);
}
