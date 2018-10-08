package application.DAO;

import java.util.List;

import application.model.AuditLog;

public interface AuditLogDAO {
	public List<AuditLog> getAll();
	public void insert(AuditLog a);
	public void update(String query);
	public void delete(String query);
}
