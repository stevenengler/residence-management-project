package lakehead.grouptwo.residence_management_server.sql_commands;
//
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.ApplicationID;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.MessageID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class ResidenceDataSQL implements IResidenceDataGateway{
	private Connection dbConnection;
	//
	public ResidenceDataSQL(Connection _dbConnection){
		dbConnection = _dbConnection;
	}
	//
	@Override
	public Vector<RoomID> getAllAvailableRoomsInBuilding(BuildingID buildingID) throws Exception{
		Vector<RoomID> rooms = new Vector<RoomID>();
		//
		PreparedStatement st;
		st = dbConnection.prepareStatement(	"SELECT DISTINCT(rooms.room_id)"+
											"FROM rooms"+
											"LEFT JOIN user_accounts"+
											"ON rooms.room_id=user_accounts.residence_room"+
											"WHERE (user_accounts.residence_room IN ("+
											"     SELECT user_accounts.residence_room"+
											"     FROM user_accounts"+
											"     GROUP BY user_accounts.residence_room"+
											"     HAVING COUNT(user_accounts.residence_room) < rooms.room_capacity"+
											")"+
											"OR user_accounts.residence_room is NULL)"+
											"AND in_building = ?");
		st.setLong(1, buildingID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		while(rs.next()) {
			rooms.add(new RoomID(rs.getLong(1)));
		}
		//
		return rooms;
	}
	//
	@Override
	public long getNumberOfAvailableRoomsInBuilding(BuildingID buildingID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement(	"SELECT COUNT(DISTINCT(rooms.room_id)) "+
											"FROM rooms "+
											"LEFT JOIN user_accounts "+
											"ON rooms.room_id=user_accounts.residence_room "+
											"WHERE (user_accounts.residence_room IN ( "+
											"     SELECT user_accounts.residence_room "+
											"     FROM user_accounts "+
											"     GROUP BY user_accounts.residence_room "+
											"     HAVING COUNT(user_accounts.residence_room) < rooms.room_capacity "+
											") "+
											"OR user_accounts.residence_room is NULL) "+
											"AND in_building = ? ");
		st.setLong(1, buildingID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return rs.getLong(1);
		}else{
			throw new SQLException();
		}
	}
	//
	@Override
	public Vector<UserID> getOccupantsOfRoom(RoomID roomID) throws Exception{
		Vector<UserID> occupants = new Vector<UserID>();
		//
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT user_id FROM user_accounts WHERE residence_room = ?");
		st.setLong(1, roomID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		while(rs.next()) {
			occupants.add(new UserID(rs.getLong(1)));
		}
		//
		return occupants;
	}
	//
	@Override
	public Vector<MessageID> getUsersReceivedMessages(UserID userID) throws Exception{
		Vector<MessageID> messages = new Vector<MessageID>();
		//
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT message_id FROM messages WHERE target_user = ?");
		st.setLong(1, userID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		while(rs.next()) {
			messages.add(new MessageID(rs.getLong(1)));
		}
		//
		return messages;
	}
	//
	@Override
	public Vector<MessageID> getUsersUnreadReceivedMessages(UserID userID) throws Exception{
		Vector<MessageID> messages = new Vector<MessageID>();
		//
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT message_id FROM messages WHERE target_user = ? AND has_been_read = FALSE");
		st.setLong(1, userID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		while(rs.next()) {
			messages.add(new MessageID(rs.getLong(1)));
		}
		//
		return messages;
	}
	//
	@Override
	public String getContentsOfMessage(MessageID messageID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT message_contents FROM messages WHERE message_id = ? LIMIT 1");
		st.setLong(1, messageID.id);
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
	public boolean getReadStatusOfMessage(MessageID messageID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT has_been_read FROM messages WHERE message_id = ? LIMIT 1");
		st.setLong(1, messageID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return rs.getBoolean(1);
		}else{
			throw new SQLException();
		}
	}
	//
	@Override
	public void setReadStatusOfMessage(MessageID messageID, boolean setTo) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("UPDATE messages SET has_been_read = ? WHERE message_id = ?");
		st.setBoolean(1, setTo);
		st.setLong(2, messageID.id);
		//
		st.executeUpdate();
	}
	//
	@Override
	public void sendMessage(UserID fromUser, UserID toUser, String contents) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("INSERT INTO messages (message_contents, target_user, from_user) VALUES (?,?,?)");
		//st.setBoolean(1, setTo);
		//st.setLong(2, messageID.id);
		st.setString(1, contents);
		st.setLong(2, toUser.id);
		st.setLong(3, fromUser.id);
		//
		st.executeUpdate();
	}
	//
	@Override
	public RoomID getRoomOccupiedByUser(UserID userID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT residence_room FROM user_accounts WHERE user_id = ? LIMIT 1");
		st.setLong(1, userID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			RoomID roomID = new RoomID(rs.getLong(1));
			if(rs.wasNull()){
				return null;
		    }
			return roomID;
		}else{
			throw new SQLException();
		}
	}
	//
	@Override
	public BuildingID getBuildingThatContainsRoom(RoomID roomID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT in_building FROM rooms WHERE room_id = ? LIMIT 1");
		st.setLong(1, roomID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return new BuildingID(rs.getLong(1));
		}else{
			throw new SQLException();
		}
	}
	//
	@Override
	public UserID getManagerOfBuilding(BuildingID buildingID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT manager_id FROM buildings WHERE building_id = ? LIMIT 1");
		st.setLong(1, buildingID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return new UserID(rs.getLong(1));
		}else{
			throw new SQLException();
		}
	}
	@Override
	public Vector<BuildingID> getBuildingsManagedByUser(UserID userID) throws Exception{
		Vector<BuildingID> buildings = new Vector<BuildingID>();
		//
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT building_id FROM buildings WHERE manager_id = ?");
		st.setLong(1, userID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		while(rs.next()) {
			buildings.add(new BuildingID(rs.getLong(1)));
		}
		//
		return buildings;
	}
	//
	@Override
	public Vector<RoomID> getAllRoomsInBuilding(BuildingID buildingID) throws Exception{
		Vector<RoomID> rooms = new Vector<RoomID>();
		//
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT room_id FROM rooms WHERE in_building = ?");
		st.setLong(1, buildingID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		while(rs.next()) {
			rooms.add(new RoomID(rs.getLong(1)));
		}
		//
		return rooms;
	}
	//
	@Override
	public long getNumberOfRoomsInBuilding(BuildingID buildingID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT COUNT(*) FROM rooms WHERE in_building = ?");
		st.setLong(1, buildingID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return rs.getLong(1);
		}else{
			throw new SQLException();
		}
	}
	//
	@Override
	public void applyForResidence(UserID userID, int yearLevel, String requests) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("INSERT INTO residence_applications (for_user, year_level, special_requests) VALUES (?,?,?)");
		st.setLong(1, userID.id);
		st.setInt(2, yearLevel);
		st.setString(3, requests);
		//
		st.executeUpdate();
	}
	//
	@Override
	public Vector<ApplicationID> getResidenceApplications(int numToFetch) throws Exception{
		Vector<ApplicationID> applications = new Vector<ApplicationID>();
		//
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT application_id FROM residence_applications LIMIT ?");
		st.setInt(1, numToFetch);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		while(rs.next()) {
			applications.add(new ApplicationID(rs.getLong(1)));
		}
		//
		return applications;
	}
	//
	@Override
	public UserID getApplicationUser(ApplicationID applicationID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT for_user FROM residence_applications WHERE application_id = ? LIMIT 1");
		st.setLong(1, applicationID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return new UserID(rs.getLong(1));
		}else{
			throw new SQLException();
		}
	}
	//
	@Override
	public int getApplicationYearLevel(ApplicationID applicationID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT year_level FROM residence_applications WHERE application_id = ? LIMIT 1");
		st.setLong(1, applicationID.id);
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return rs.getInt(1);
		}else{
			throw new SQLException();
		}
	}
	//
	@Override
	public String getApplicationSpecialRequests(ApplicationID applicationID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT special_requests FROM residence_applications WHERE application_id = ? LIMIT 1");
		st.setLong(1, applicationID.id);
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
	public void setUserRoom(UserID userID, RoomID roomID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("UPDATE user_accounts SET residence_room = ? WHERE user_id = ?");
		st.setLong(1, roomID.id);
		st.setLong(2, userID.id);
		//
		st.executeUpdate();
	}
	//
	@Override
	public void removeApplication(ApplicationID applicationID) throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("DELETE FROM user_accounts WHERE application_id = ?");
		st.setLong(1, applicationID.id);
		//
		st.executeUpdate();
	}
	//
	@Override
	public long getNumberOfApplications() throws Exception{
		PreparedStatement st;
		st = dbConnection.prepareStatement("SELECT COUNT(application_id) FROM residence_applications");
		//
		ResultSet rs = null;
		rs = st.executeQuery();
		if(rs.next()){
			return rs.getLong(1);
		}else{
			throw new SQLException();
		}
	}
}
