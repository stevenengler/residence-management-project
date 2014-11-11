package lakehead.grouptwo.residence_management_system.implemented_gateways.client_sql;
//
import java.sql.*;
import java.util.Arrays;

import jbcrypt.BCrypt;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class ClientSQLAccountData implements IAccountData{
	private Connection dbConnection;
	private UserID id;
	private char[] authKey;
	//
	public ClientSQLAccountData(Connection _dbConnection, String username, char[] password) throws AuthenticationException{
		dbConnection = _dbConnection;
		//
		PreparedStatement st;
		try{
			st = dbConnection.prepareStatement("SELECT user_id, auth_key, password FROM user_accounts WHERE username = ? LIMIT 1");
			st.setString(1, username);
		}catch(SQLException e){
			throw new AuthenticationException("Something went really wrong :(");
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
					throw new AuthenticationException("Username and password did not match.");
				}
			}else{
				throw new AuthenticationException("Username and password did not match.");
			}
		}catch(SQLException e){
			throw new AuthenticationException("Check yo SQL!");
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
			throw new AuthenticationException("Something went really wrong :(");
		}
		//
		ResultSet rs = null;
		try{
			rs = st.executeQuery();
			//
			if(rs.next()){
				if(new String(authKey).compareTo(rs.getString(1)) == 0){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(SQLException e){
			throw new AuthenticationException("Check yo SQL!");
		}
	}
	//
	public void logOut(){
		for(int i=0; i<authKey.length; i++){
			authKey[i] = 0;
		}
	}
}
