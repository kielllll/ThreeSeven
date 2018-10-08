package application.DAO;

import java.util.List;

import application.model.Item;

public interface ItemDAO {
	public List<Item> getAll();
	public void insert(Item i);
	public void update(String query);
	public void delete(String query);
}
