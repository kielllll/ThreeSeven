package application.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import application.DAO.AuditLogDAOImpl;
import application.model.AuditLog;
import application.model.User;

public class Sessions {

	private static Sessions instance = new Sessions();
	
	private User u;
	
	public Sessions() {
		u = new User();
	}
	
	public void setUser(User u) {
		this.u=u;
	}
	
	public User getUser() {
		return u;
	}
	
	public void audit(String action) {
		AuditLog a = new AuditLog();
		User u = Sessions.getInstance().getUser();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		if(u.getAccessType().equals("Master")) {
			a = new AuditLog(AuditLogDAOImpl.getInstance().getAll().size()+1, LocalDate.now(), time, 0, "Super", "Admin", u.getAccessType(), action);
		}
		else {
			a = new AuditLog(AuditLogDAOImpl.getInstance().getAll().size()+1, LocalDate.now(), time, u.getUserID(), u.getFirstname(), u.getLastname(), u.getAccessType(), action);
		}
		AuditLogDAOImpl.getInstance().insert(a);
	}
	
	public static Sessions getInstance() {
		return instance;
	}
}
