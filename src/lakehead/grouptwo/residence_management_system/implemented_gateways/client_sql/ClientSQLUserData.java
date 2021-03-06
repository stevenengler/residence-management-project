package lakehead.grouptwo.residence_management_system.implemented_gateways.client_sql;
//
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class ClientSQLUserData implements IUserDataGateway{
	private Connection dbConnection;
	//
	public ClientSQLUserData(Connection _dbConnection){
		dbConnection = _dbConnection;
	}
	//
	@Override
	public String getUserFirstName(UserID userID) throws SQLException{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT first_name FROM user_accounts WHERE user_id = ?");
		st.setLong(1, userID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return rs.getString(1);
		}else{
			throw new SQLException();
		}
	}
	//
	@Override
	public String getUserLastName(UserID userID) throws SQLException{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT last_name FROM user_accounts WHERE user_id = ?");
		st.setLong(1, userID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return rs.getString(1);
		}else{
			throw new SQLException();
		}
	}
	//
	@Override
	public int getUserPermissions(UserID userID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT permissions FROM user_accounts WHERE user_id = ?");
		st.setLong(1, userID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return rs.getInt(1);
		}else{
			throw new SQLException();
		}
	}
}