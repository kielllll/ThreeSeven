package application.DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.model.User;
import application.util.Database;
import application.util.Encryption;

public class UserDAOImpl implements UserDAO {
	private static UserDAOImpl instance = new UserDAOImpl();
	
	@Override
	public List<User> getAll() {
		List<User> list = new LinkedList<User>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT u.user_ID, u.login_ID, u.firstname, u.lastname, u.password, at.description, u.status FROM access_types at INNER JOIN users u ON at.access_type_ID = u.access_type_ID");
			
			while(rs.next()) {
				list.add(new User(rs.getInt("user_ID"), rs.getInt("login_ID"), rs.getString("firstname"),rs.getString("lastname"),rs.getString("password"),rs.getString("description"),rs.getString("status")));
			}
			
			st.close();
			rs.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public void insert(User user) {
		// TODO Auto-generated method stub
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO users VALUES"+user.toString());
			
			st.close();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}

	@Override
	public void update(String query) {
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
	
	public boolean checkData(int userID, String password) {
		boolean found = false;
		
		try {
			for(User u : getAll()) {
				if(u.getStatus().equalsIgnoreCase("inactive"))
					continue;
				if(userID==u.getLoginID()) {
					if(Encryption.validatePassword(password, u.getPassword())) {
						found = true;
						break;
					}
				} else found = false;
					
			}
		} catch(Exception e) {
			
		}
		
		return found;
	}
	
	public static UserDAOImpl getInstance() {
		return instance;
	}
}
