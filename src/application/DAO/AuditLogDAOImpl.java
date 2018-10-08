package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.AuditLog;
import application.util.Database;
import application.util.Date;

public class AuditLogDAOImpl implements AuditLogDAO{
	
	private static AuditLogDAOImpl instance = new AuditLogDAOImpl();
	
	@Override
	public List<AuditLog> getAll() {
		// TODO Auto-generated method stub
		List<AuditLog> list = new LinkedList<AuditLog>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT a.audit_log_ID, a.date, a.time, u.user_ID, u.firstname, u.lastname, at.description, a.action FROM audit_logs a INNER JOIN users u ON a.user_ID = u.user_ID INNER JOIN access_types at ON u.access_type_ID = at.access_type_ID");
			
			while(rs.next()) {
				list.add(new AuditLog(rs.getInt("audit_log_ID"), Date.parse(rs.getString("date")), rs.getString("time"), rs.getInt("user_ID"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("description"), rs.getString("action")));
			}
			
			st.close();
			rs.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(AuditLog a) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO audit_logs VALUES "+a.toString());
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}

	@Override
	public void update(String query) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate(query);
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}

	@Override
	public void delete(String query) {
		// TODO Auto-generated method stub
		
	}
	
	public static AuditLogDAOImpl getInstance() {
		return instance;
	}
}
