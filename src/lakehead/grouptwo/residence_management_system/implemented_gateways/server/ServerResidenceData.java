package lakehead.grouptwo.residence_management_system.implemented_gateways.server;
//
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import lakehead.grouptwo.residence_management_server.messages.MessageDataID;
import lakehead.grouptwo.residence_management_server.messages.ServerCommandMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerRequestMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerSendMessage;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
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
	//
	public ServerResidenceData(ConnectionToServer _connectionToServer){
		connectionToServer = _connectionToServer;
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
	private Object getFromServer(MessageDataID dataID, Object option) throws IOException, AuthenticationException{
		try{
			connectionToServer.sendMessage(new ServerMessage(ServerMessage.MessageID.REQUESTING_DATA, new ServerRequestMessage(dataID, option), getUserID(), getAuthKey()));
		}catch(IOException e){
			e.printStackTrace();
			throw new IOException("Error while sending the request.");
		}
		//
		ServerMessage responseMessage;
		//
		try{
			responseMessage = connectionToServer.readMessage();
		}catch(IOException e){
			throw new IOException("Error while reading the response.");
		}
		//
		if(responseMessage.getID() == ServerMessage.MessageID.ERROR){
			throw new IOException("Server error while getting the response (1).");
		}
		if(responseMessage.getID() == ServerMessage.MessageID.AUTHENTICATION_ERROR){
			throw new AuthenticationException("The user id and auth_key were not valid.");
		}
		if(responseMessage.getID() == ServerMessage.MessageID.PERMISSION_ERROR){
			throw new AuthenticationException("Do not have permissions for this.");
		}
		//
		ServerSendMessage responseSendMessage = (ServerSendMessage)responseMessage.getObject();
		//
		if(responseSendMessage.getID() != dataID){
			throw new IOException("Server error while getting the response (2).");
		}
		//
		return responseSendMessage.getObject();
	}
	private void sendToServer(ServerCommandMessage.CommandMessageID dataID, Vector<Object> option) throws IOException, AuthenticationException{
		try{
			connectionToServer.sendMessage(new ServerMessage(ServerMessage.MessageID.EXECUTE_COMMAND, new ServerCommandMessage(dataID, option), getUserID(), getAuthKey()));
		}catch(IOException e){
			e.printStackTrace();
			throw new IOException("Error while sending the request.");
		}
		//
		ServerMessage responseMessage;
		//
		try{
			responseMessage = connectionToServer.readMessage();
		}catch(IOException e){
			throw new IOException("Client error while reading the response.");
		}
		//
		if(responseMessage.getID() == ServerMessage.MessageID.ERROR){
			throw new IOException("Server error while getting the response (1).");
		}else if(responseMessage.getID() == ServerMessage.MessageID.AUTHENTICATION_ERROR){
			throw new AuthenticationException("The user id and auth_key were not valid.");
		}else if(responseMessage.getID() == ServerMessage.MessageID.PERMISSION_ERROR){
			throw new AuthenticationException("Do not have permissions for this.");
		}else if(responseMessage.getID() == ServerMessage.MessageID.OKAY){
			return;
		}else{
			throw new IOException("Unknown error with the response. Request may or may not have gone through.");
		}
	}
	//
	@Override
	public Vector<RoomID> getAllAvailableRoomsInBuilding(BuildingID buildingID) throws Exception{
		return (Vector<RoomID>)getFromServer(MessageDataID.AVAILABLE_ROOMS_IN_BUILDING, buildingID);
	}
	//
	@Override
	public long getNumberOfAvailableRoomsInBuilding(BuildingID buildingID) throws Exception{
		return (long)getFromServer(MessageDataID.NUM_OF_AVAILABLE_ROOMS_IN_BUILDING, buildingID);
	}
	//
	@Override
	public Vector<UserID> getOccupantsOfRoom(RoomID roomID) throws Exception{
		return (Vector<UserID>)getFromServer(MessageDataID.OCCUPANTS_OF_ROOM, roomID);
	}
	//
	@Override
	public Vector<MessageID> getUsersReceivedMessages(UserID userID) throws Exception{
		return (Vector<MessageID>)getFromServer(MessageDataID.USERS_RECEIVED_MESSAGES, userID);
	}
	//
	@Override
	public Vector<MessageID> getUsersUnreadReceivedMessages(UserID userID) throws Exception{
		return (Vector<MessageID>)getFromServer(MessageDataID.USERS_UNREAD_RECEIVED_MESSAGES, userID);
	}
	//
	@Override
	public String getContentsOfMessage(MessageID messageID) throws Exception{
		return (String)getFromServer(MessageDataID.CONTENTS_OF_MESSAGE, messageID);
	}
	//
	@Override
	public boolean getReadStatusOfMessage(MessageID messageID) throws Exception{
		return (boolean)getFromServer(MessageDataID.READ_STATUS_OF_MESSAGE, messageID);
	}
	//
	@Override
	public void setReadStatusOfMessage(MessageID messageID, boolean setTo) throws Exception{
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(messageID);
		parameters.add(setTo);
		sendToServer(ServerCommandMessage.CommandMessageID.SET_MESSAGE_READ_STATUS, parameters);
	}
	//
	@Override
	public void sendMessage(UserID fromUser, UserID toUser, String contents) throws Exception{
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(fromUser);
		parameters.add(toUser);
		parameters.add(contents);
		sendToServer(ServerCommandMessage.CommandMessageID.SEND_MESSAGE_TO_USER, parameters);
	}
	//
	@Override
	public RoomID getRoomOccupiedByUser(UserID userID) throws Exception{
		return (RoomID)getFromServer(MessageDataID.ROOM_OCCUPIED_BY_USER, userID);
	}
	//
	@Override
	public BuildingID getBuildingThatContainsRoom(RoomID roomID) throws Exception{
		return (BuildingID)getFromServer(MessageDataID.BUILDING_THAT_CONTAINS_ROOM, roomID);
	}
	//
	@Override
	public UserID getManagerOfBuilding(BuildingID buildingID) throws Exception{
		return (UserID)getFromServer(MessageDataID.MANAGER_OF_BUILDING, buildingID);
	}
	@Override
	public Vector<BuildingID> getBuildingsManagedByUser(UserID userID) throws Exception{
		return (Vector<BuildingID>)getFromServer(MessageDataID.BUILDINGS_MANAGED_BY_USER, userID);
	}
	//
	@Override
	public Vector<RoomID> getAllRoomsInBuilding(BuildingID buildingID) throws Exception{
		return (Vector<RoomID>)getFromServer(MessageDataID.ALL_ROOMS_IN_BUILDING, buildingID);
	}
	//
	@Override
	public long getNumberOfRoomsInBuilding(BuildingID buildingID) throws Exception{
		return (long)getFromServer(MessageDataID.NUMBER_OF_ROOMS_IN_BUILDING, buildingID);
	}
	//
	@Override
	public void applyForResidence(UserID userID, int yearLevel, String requests) throws Exception{
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(userID);
		parameters.add(yearLevel);
		parameters.add(requests);
		sendToServer(ServerCommandMessage.CommandMessageID.APPLY_FOR_RESIDENCE, parameters);
	}
	//
	@Override
	public Vector<ApplicationID> getResidenceApplications(int numToFetch) throws Exception{
		return (Vector<ApplicationID>)getFromServer(MessageDataID.RESIDENCE_APPLICATIONS, numToFetch);
	}
	//
	@Override
	public UserID getApplicationUser(ApplicationID applicationID) throws Exception{
		return (UserID)getFromServer(MessageDataID.APPLICATION_USER, applicationID);
	}
	//
	@Override
	public int getApplicationYearLevel(ApplicationID applicationID) throws Exception{
		return (int)getFromServer(MessageDataID.APPLICATION_YEAR_LEVEL, applicationID);
	}
	//
	@Override
	public String getApplicationSpecialRequests(ApplicationID applicationID) throws Exception{
		return (String)getFromServer(MessageDataID.APPLICATION_SPECIAL_REQUESTS, applicationID);
	}
	//
	@Override
	public void setUserRoom(UserID userID, RoomID roomID) throws Exception{
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(userID);
		parameters.add(roomID);
		sendToServer(ServerCommandMessage.CommandMessageID.APPLY_FOR_RESIDENCE, parameters);
	}
	//
	@Override
	public void removeApplication(ApplicationID applicationID) throws Exception{
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(applicationID);
		sendToServer(ServerCommandMessage.CommandMessageID.REMOVE_APPLICATION, parameters);
	}
	//
	@Override
	public long getNumberOfApplications() throws Exception{
		return (long)getFromServer(MessageDataID.NUMBER_OF_APPLICATIONS, null);
	}
}
