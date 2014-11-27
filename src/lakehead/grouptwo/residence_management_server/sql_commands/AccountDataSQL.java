package lakehead.grouptwo.residence_management_server.sql_commands;
//
import java.sql.*;
//
import jbcrypt.BCrypt;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class AccountDataSQL implements IAccountData{
	private Connection dbConnection;
	private UserID id;
	private char[] authKey;
	//
	public AccountDataSQL(Connection _dbConnection, String username, char[] password) throws AuthenticationException{
		dbConnection = _dbConnection;
		//
		PreparedStatement st;
		try{
			// get the user id, auth key, and password for the user with the specified username
			st = dbConnection.prepareStatement("SELECT user_id, auth_key, password FROM user_accounts WHERE username = ? LIMIT 1");
			st.setString(1, username);
		}catch(SQLException e){
			throw new AuthenticationException("This should never cause an error. Make sure the database is still connected.");
		}
		//
		ResultSet rs = null;
		try{
			rs = st.executeQuery();
			//
			if(rs.next()){
				if(BCrypt.checkpw(new String(password), rs.getString(3))){
					//NOTE: Since using Strings for passwords should be avoided, a different hashing algorithm should be used in the future
					id = new UserID(rs.getLong(1));
					authKey = rs.getString(2).toCharArray();
				}else{
					// password did not match the password in the database
					throw new AuthenticationException("Username and password did not match.");
				}
			}else{
				// username wasn't found in the database
				throw new AuthenticationException("Username and password did not match.");
			}
		}catch(SQLException e){
			throw new AuthenticationException("Check the SQL statement for errors.");
		}
	}
	//
	@Override
	public UserID getThisUserID() {
		return id;
	}
	//
	public boolean checkIfLoggedIn() throws AuthenticationException{
		PreparedStatement st;
		try{
			st = dbConnection.prepareStatement("SELECT auth_key FROM user_accounts WHERE user_id = ? LIMIT 1");
			st.setLong(1, id.id);
		}catch(SQLException e){
			throw new AuthenticationException("This should never cause an error. Make sure the database is still connected.");
		}
		//
		ResultSet rs = null;
		try{
			rs = st.executeQuery();
			//
			if(rs.next()){
				if(new String(authKey).compareTo(rs.getString(1)) == 0){
					// the authentication key was correct
					return true;
				}else{
					return false;
				}
			}else{
				// user was not found in the database
				return false;
			}
		}catch(SQLException e){
			throw new AuthenticationException("Check the SQL statement for errors.");
		}
	}
	//
	public void logOut(){
		for(int i=0; i<authKey.length; i++){
			authKey[i] = 0;
		}
	}
}
