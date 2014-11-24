package lakehead.grouptwo.residence_management_system.implemented_gateways.server;
//
import java.io.InvalidClassException;
import java.util.Vector;
//
import lakehead.grouptwo.residence_management_server.messages.MessageDataID;
import lakehead.grouptwo.residence_management_server.messages.ServerCommandMessage;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.ApplicationID;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.MessageID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class ServerResidenceData implements IResidenceDataGateway{
	private ConnectionToServer connectionToServer;
	private ServerAccountData accountData = null;
	private DataMessageHandler dataMessageHandler;
	//
	public ServerResidenceData(ConnectionToServer _connectionToServer){
		connectionToServer = _connectionToServer;
		dataMessageHandler = new DataMessageHandler(connectionToServer);
	}
	//
	public void setAccountData(ServerAccountData _accountData){
		accountData = _accountData;
	}
	//
	private UserID getUserID(){
		if(accountData != null){
			return accountData.getThisUserID();
		}else{
			return null;
		}
	}
	private char[] getAuthKey(){
		if(accountData != null){
			return accountData.getAuthKey();
		}else{
			return null;
		}
	}
	//
	@Override
	public Vector<RoomID> getAllAvailableRoomsInBuilding(BuildingID buildingID) throws Exception{
		return (Vector<RoomID>)dataMessageHandler.getDataMessageFromServer(MessageDataID.AVAILABLE_ROOMS_IN_BUILDING, buildingID, getUserID(), getAuthKey());
	}
	//
	@Override
	public long getNumberOfAvailableRoomsInBuilding(BuildingID buildingID) throws Exception{
		return (long)dataMessageHandler.getDataMessageFromServer(MessageDataID.NUM_OF_AVAILABLE_ROOMS_IN_BUILDING, buildingID, getUserID(), getAuthKey());
	}
	//
	@Override
	public Vector<UserID> getOccupantsOfRoom(RoomID roomID) throws Exception{
		return (Vector<UserID>)dataMessageHandler.getDataMessageFromServer(MessageDataID.OCCUPANTS_OF_ROOM, roomID, getUserID(), getAuthKey());
	}
	//
	@Override
	public Vector<MessageID> getUsersReceivedMessages(UserID userID) throws Exception{
		return (Vector<MessageID>)dataMessageHandler.getDataMessageFromServer(MessageDataID.USERS_RECEIVED_MESSAGES, userID, getUserID(), getAuthKey());
	}
	//
	@Override
	public Vector<MessageID> getUsersUnreadReceivedMessages(UserID userID) throws Exception{
		return (Vector<MessageID>)dataMessageHandler.getDataMessageFromServer(MessageDataID.USERS_UNREAD_RECEIVED_MESSAGES, userID, getUserID(), getAuthKey());
	}
	//
	@Override
	public String getContentsOfMessage(MessageID messageID) throws Exception{
		return (String)dataMessageHandler.getDataMessageFromServer(MessageDataID.CONTENTS_OF_MESSAGE, messageID, getUserID(), getAuthKey());
	}
	//
	@Override
	public boolean getReadStatusOfMessage(MessageID messageID) throws Exception{
		return (boolean)dataMessageHandler.getDataMessageFromServer(MessageDataID.READ_STATUS_OF_MESSAGE, messageID, getUserID(), getAuthKey());
	}
	//
	@Override
	public void setReadStatusOfMessage(MessageID messageID, boolean setTo) throws Exception{
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(messageID);
		parameters.add(setTo);
		dataMessageHandler.sendDataMessageToServer(ServerCommandMessage.CommandMessageID.SET_MESSAGE_READ_STATUS, parameters, getUserID(), getAuthKey());
	}
	//
	@Override
	public void sendMessage(UserID fromUser, UserID toUser, String contents) throws Exception{
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(fromUser);
		parameters.add(toUser);
		parameters.add(contents);
		dataMessageHandler.sendDataMessageToServer(ServerCommandMessage.CommandMessageID.SEND_MESSAGE_TO_USER, parameters, getUserID(), getAuthKey());
	}
	//
	@Override
	public RoomID getRoomOccupiedByUser(UserID userID) throws Exception{
		return (RoomID)dataMessageHandler.getDataMessageFromServer(MessageDataID.ROOM_OCCUPIED_BY_USER, userID, getUserID(), getAuthKey());
	}
	//
	@Override
	public BuildingID getBuildingThatContainsRoom(RoomID roomID) throws Exception{
		return (BuildingID)dataMessageHandler.getDataMessageFromServer(MessageDataID.BUILDING_THAT_CONTAINS_ROOM, roomID, getUserID(), getAuthKey());
	}
	//
	@Override
	public UserID getManagerOfBuilding(BuildingID buildingID) throws Exception{
		return (UserID)dataMessageHandler.getDataMessageFromServer(MessageDataID.MANAGER_OF_BUILDING, buildingID, getUserID(), getAuthKey());
	}
	@Override
	public Vector<BuildingID> getBuildingsManagedByUser(UserID userID) throws Exception{
		return (Vector<BuildingID>)dataMessageHandler.getDataMessageFromServer(MessageDataID.BUILDINGS_MANAGED_BY_USER, userID, getUserID(), getAuthKey());
	}
	//
	@Override
	public Vector<RoomID> getAllRoomsInBuilding(BuildingID buildingID) throws Exception{
		return (Vector<RoomID>)dataMessageHandler.getDataMessageFromServer(MessageDataID.ALL_ROOMS_IN_BUILDING, buildingID, getUserID(), getAuthKey());
	}
	//
	@Override
	public long getNumberOfRoomsInBuilding(BuildingID buildingID) throws Exception{
		return (long)dataMessageHandler.getDataMessageFromServer(MessageDataID.NUMBER_OF_ROOMS_IN_BUILDING, buildingID, getUserID(), getAuthKey());
	}
	//
	@Override
	public void applyForResidence(UserID userID, int yearLevel, String requests) throws Exception{
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(userID);
		parameters.add(yearLevel);
		parameters.add(requests);
		dataMessageHandler.sendDataMessageToServer(ServerCommandMessage.CommandMessageID.APPLY_FOR_RESIDENCE, parameters, getUserID(), getAuthKey());
	}
	//
	@Override
	public Vector<ApplicationID> getResidenceApplications(int numToFetch) throws Exception{
		return (Vector<ApplicationID>)dataMessageHandler.getDataMessageFromServer(MessageDataID.RESIDENCE_APPLICATIONS, numToFetch, getUserID(), getAuthKey());
	}
	//
	@Override
	public UserID getApplicationUser(ApplicationID applicationID) throws Exception{
		return (UserID)dataMessageHandler.getDataMessageFromServer(MessageDataID.APPLICATION_USER, applicationID, getUserID(), getAuthKey());
	}
	//
	@Override
	public int getApplicationYearLevel(ApplicationID applicationID) throws Exception{
		return (int)dataMessageHandler.getDataMessageFromServer(MessageDataID.APPLICATION_YEAR_LEVEL, applicationID, getUserID(), getAuthKey());
	}
	//
	@Override
	public String getApplicationSpecialRequests(ApplicationID applicationID) throws Exception{
		return (String)dataMessageHandler.getDataMessageFromServer(MessageDataID.APPLICATION_SPECIAL_REQUESTS, applicationID, getUserID(), getAuthKey());
	}
	//
	@Override
	public void setUserRoom(UserID userID, RoomID roomID) throws Exception{
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(userID);
		parameters.add(roomID);
		dataMessageHandler.sendDataMessageToServer(ServerCommandMessage.CommandMessageID.APPLY_FOR_RESIDENCE, parameters, getUserID(), getAuthKey());
	}
	//
	@Override
	public void removeApplication(ApplicationID applicationID) throws Exception{
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(applicationID);
		dataMessageHandler.sendDataMessageToServer(ServerCommandMessage.CommandMessageID.REMOVE_APPLICATION, parameters, getUserID(), getAuthKey());
	}
	//
	@Override
	public long getNumberOfApplications() throws Exception{
		return (long)dataMessageHandler.getDataMessageFromServer(MessageDataID.NUMBER_OF_APPLICATIONS, null, getUserID(), getAuthKey());
	}
}
