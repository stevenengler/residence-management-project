package lakehead.grouptwo.residence_management_server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import jbcrypt.BCrypt;

import lakehead.grouptwo.residence_management_server.messages.MessageDataID;
import lakehead.grouptwo.residence_management_server.messages.ServerCommandMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerLoginRequestMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerLoginResponseMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerRequestMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerSendMessage;
import lakehead.grouptwo.residence_management_server.networking.ConnectionToClient;
import lakehead.grouptwo.residence_management_server.sql_commands.ResidenceDataSQL;
import lakehead.grouptwo.residence_management_server.sql_commands.UserDataSQL;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.ApplicationID;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.MessageID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;

public class ResidenceManagementServerProtocol{
	private Connection dbConnection;
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	//
	public ResidenceManagementServerProtocol(Connection _dbConnection){
		dbConnection = _dbConnection;
		//
		residenceDataGateway = new ResidenceDataSQL(dbConnection);
		userDataGateway = new UserDataSQL(dbConnection);
	}
	//
	public void processFromClient(ServerMessage message, ConnectionToClient connection){
		ServerMessage.MessageID messageID = message.getID();
		//
		boolean messageObjectWasCorrectType = true;
		//
		if(messageID == ServerMessage.MessageID.REQUESTING_DATA){
			// client is requesting data from the server
			if(message.getObject() instanceof ServerRequestMessage){
				handleDataRequest((ServerRequestMessage)message.getObject(), connection, message.getUserID(), message.getAuthKey());
			}else{
				messageObjectWasCorrectType = false;
			}
		}else if(messageID == ServerMessage.MessageID.SENDING_DATA){
			// client is sending data to the server
			if(message.getObject() instanceof ServerSendMessage){
				handleSentData((ServerSendMessage)message.getObject(), connection, message.getUserID(), message.getAuthKey());
			}else{
				messageObjectWasCorrectType = false;
			}
		}else if(messageID == ServerMessage.MessageID.EXECUTE_COMMAND){
			// client wants to execute a command on the data
			if(message.getObject() instanceof ServerCommandMessage){
				handleCommand((ServerCommandMessage)message.getObject(), connection, message.getUserID(), message.getAuthKey());
			}else{
				messageObjectWasCorrectType = false;
			}
		}else if(messageID == ServerMessage.MessageID.LOGIN_REQUEST){
			//client wants to login and get the key
			if(message.getObject() instanceof ServerLoginRequestMessage){
				handleLoginRequest((ServerLoginRequestMessage)message.getObject(), connection);
			}else{
				messageObjectWasCorrectType = false;
			}
		}else if(messageID == ServerMessage.MessageID.CLOSE_CONNECTION){
			connection.close();
		}
		
		if(!messageObjectWasCorrectType){
			try{
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.UNEXPECTED_MESSAGE_DATA_TYPE, null, null, null));
			}catch(IOException e1){
				// nothing can be done here, the connection's probably closed
			}
		}
	}
	//
	private void handleDataRequest(ServerRequestMessage message, ConnectionToClient connection, UserID userID, char[] authKey){
		//
		// CURRENT ISSUES:
		//	1)	Need to set permissions so logged in user can't get any user's information such as full name (and only their own full name)
		//	2)	Need to prevent crashes caused by incorrect data types being sent and then casted to the wrong type
		//
		MessageDataID messageID = message.getID();
		ServerSendMessage messageToSend = null;
		//
		boolean sendErrorMessage = false;
		boolean sendAuthenticationErrorMessage = false;
		boolean sendPermissionErrorMessage = false;
		//
		try{
			if(messageID == MessageDataID.USER_FIRSTNAME){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, userDataGateway.getUserFirstName((UserID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.USER_LASTNAME){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, userDataGateway.getUserLastName((UserID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.USER_LOGGED_IN){
				if(getClientPermissions(userID, authKey) > 0){
					// logged in
					messageToSend = new ServerSendMessage(messageID, true);
				}else{
					// not logged in or error with authentication
					messageToSend = new ServerSendMessage(messageID, false);
				}
			}else if(messageID == MessageDataID.USER_PERMISSIONS){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, userDataGateway.getUserPermissions((UserID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.AVAILABLE_ROOMS_IN_BUILDING){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getAllAvailableRoomsInBuilding((BuildingID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.NUM_OF_AVAILABLE_ROOMS_IN_BUILDING){
				// no permission checking necessary since anyone can view this
				messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getNumberOfAvailableRoomsInBuilding((BuildingID)message.getObject()));
			}else if(messageID == MessageDataID.OCCUPANTS_OF_ROOM){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getOccupantsOfRoom((RoomID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.USERS_RECEIVED_MESSAGES){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getUsersReceivedMessages((UserID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.USERS_UNREAD_RECEIVED_MESSAGES){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getUsersUnreadReceivedMessages((UserID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.CONTENTS_OF_MESSAGE){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getContentsOfMessage((MessageID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.READ_STATUS_OF_MESSAGE){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getReadStatusOfMessage((MessageID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.ROOM_OCCUPIED_BY_USER){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getRoomOccupiedByUser((UserID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.BUILDING_THAT_CONTAINS_ROOM){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getBuildingThatContainsRoom((RoomID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.MANAGER_OF_BUILDING){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getManagerOfBuilding((BuildingID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.BUILDINGS_MANAGED_BY_USER){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getBuildingsManagedByUser((UserID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.ALL_ROOMS_IN_BUILDING){
				if(getClientPermissions(userID, authKey) > 0){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getAllRoomsInBuilding((BuildingID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.NUMBER_OF_ROOMS_IN_BUILDING){
				// no permission checking necessary since anyone can view this
				messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getNumberOfRoomsInBuilding((BuildingID)message.getObject()));
			}else if(messageID == MessageDataID.RESIDENCE_APPLICATIONS){
				if(getClientPermissions(userID, authKey) == 2){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getResidenceApplications((int)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.APPLICATION_USER){
				if(getClientPermissions(userID, authKey) == 2){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getApplicationUser((ApplicationID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.APPLICATION_YEAR_LEVEL){
				if(getClientPermissions(userID, authKey) == 2){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getApplicationYearLevel((ApplicationID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.APPLICATION_SPECIAL_REQUESTS){
				if(getClientPermissions(userID, authKey) == 2){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getApplicationSpecialRequests((ApplicationID)message.getObject()));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == MessageDataID.NUMBER_OF_APPLICATIONS){
				if(getClientPermissions(userID, authKey) == 2){
					messageToSend = new ServerSendMessage(messageID, residenceDataGateway.getNumberOfApplications());
				}else{
					sendPermissionErrorMessage = true;
				}
			}
		}catch(Exception e){
			sendErrorMessage = true;
		}
		//
		if(sendPermissionErrorMessage){
			// we only want to check if the user's credentials were correct if there was an error with the permissions
			// if there was no error with the permissions, then it doesn't matter if the user's credentials were okay
			if(getClientPermissions(userID, authKey) == -1){
				sendAuthenticationErrorMessage = true;
			}
		}
		//
		try{
			if(sendErrorMessage){
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.ERROR, null, null, null));
			}else if(sendAuthenticationErrorMessage){
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.AUTHENTICATION_ERROR, null, null, null));
			}else if(sendPermissionErrorMessage){
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.PERMISSION_ERROR, null, null, null));
			}else if(messageToSend == null){
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.ERROR, null, null, null));
			}else{
				// everything's good, sending the data
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.SENDING_DATA, messageToSend, null, null));
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void handleSentData(ServerSendMessage message, ConnectionToClient connection, UserID userID, char[] authKey){
		// this isn't needed yet, but it is likely that the client will need to send data in the future
	}
	private void handleCommand(ServerCommandMessage message, ConnectionToClient connection, UserID userID, char[] authKey){
		/*
		// CURRENT ISSUES:
		//	1)	Need to set permissions so logged in user can't get any user's information such as full name (and only their own full name)
		//	2)	Need to prevent crashes caused by incorrect data types being sent and then casted to the wrong type
		//
		NOTE ABOUT READING FROM VECTOR:
			Elements from the messageObjects vector are removed from the beginning when they are passed to
			the gateway function. This way each parameter can use messageObjects.get(0) to get the next
			parameter for the function. This is simply for simplicity (don't have to specify get(0), get(1),
			get(2), etc for each parameter). They are guaranteed to be processed in the left->right order as
			explained in the Java Language Specification - Steven Nov 2014
			
			15.7.4 Argument Lists are Evaluated Left-to-Right
				In a method or constructor invocation or class instance creation expression,
				argument expressions may appear within the parentheses, separated by commas.
				Each argument expression appears to be fully evaluated before any part of any
				argument expression to its right.
		*/
		//
		ServerCommandMessage.CommandMessageID messageID = message.getID();
		Vector<Object> messageObjects = message.getObjects();
		//
		boolean sendErrorMessage = false;
		boolean sendWrongDataTypeErrorMessage = false;
		boolean sendAuthenticationErrorMessage = false;
		boolean sendPermissionErrorMessage = false;
		//
		try{
			if(messageID == ServerCommandMessage.CommandMessageID.SET_MESSAGE_READ_STATUS){
				if(getClientPermissions(userID, authKey) > 0){
					residenceDataGateway.setReadStatusOfMessage((MessageID)messageObjects.remove(0), (boolean)messageObjects.remove(0));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == ServerCommandMessage.CommandMessageID.SEND_MESSAGE_TO_USER){
				if(getClientPermissions(userID, authKey) > 0){
					residenceDataGateway.sendMessage((UserID)messageObjects.remove(0), (UserID)messageObjects.remove(0), (String)messageObjects.remove(0));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == ServerCommandMessage.CommandMessageID.APPLY_FOR_RESIDENCE){
				if(getClientPermissions(userID, authKey) > 0){
					residenceDataGateway.applyForResidence((UserID)messageObjects.remove(0), (int)messageObjects.remove(0), (String)messageObjects.remove(0));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == ServerCommandMessage.CommandMessageID.SET_USER_ROOM){
				if(getClientPermissions(userID, authKey) > 0){
					residenceDataGateway.setUserRoom((UserID)messageObjects.remove(0), (RoomID)messageObjects.remove(0));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == ServerCommandMessage.CommandMessageID.SET_USER_ROOM){
				if(getClientPermissions(userID, authKey) > 0){
					residenceDataGateway.setUserRoom((UserID)messageObjects.remove(0), (RoomID)messageObjects.remove(0));
				}else{
					sendPermissionErrorMessage = true;
				}
			}else if(messageID == ServerCommandMessage.CommandMessageID.REMOVE_APPLICATION){
				if(getClientPermissions(userID, authKey) > 0){
					residenceDataGateway.removeApplication((ApplicationID)messageObjects.remove(0));
				}else{
					sendPermissionErrorMessage = true;
				}
			}
		}catch(ClassCastException e){
			// there was a problem casting one of the received objects
			sendWrongDataTypeErrorMessage = true;
		}catch(Exception e){
			sendErrorMessage = true;
		}
		//
		if(sendPermissionErrorMessage){
			// we only want to check if the user's credentials were correct if there was an error with the permissions
			// if there was no error with the permissions, then it doesn't matter if the user's credentials were okay
			if(getClientPermissions(userID, authKey) == -1){
				sendAuthenticationErrorMessage = true;
			}
		}
		//
		try{
			if(sendErrorMessage){
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.ERROR, null, null, null));
			}else if(sendWrongDataTypeErrorMessage){
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.UNEXPECTED_MESSAGE_DATA_TYPE, null, null, null));
			}else if(sendAuthenticationErrorMessage){
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.AUTHENTICATION_ERROR, null, null, null));
			}else if(sendPermissionErrorMessage){
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.PERMISSION_ERROR, null, null, null));
			}else{
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.OKAY, null, null, null));
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void handleLoginRequest(ServerLoginRequestMessage message, ConnectionToClient connection){
		PreparedStatement st;
		try{
			st = dbConnection.prepareStatement("SELECT user_id, auth_key, password FROM user_accounts WHERE username = ? LIMIT 1");
			st.setString(1, message.getUsername());
		}catch(SQLException e){
			//throw new AuthenticationException("Something went really wrong :(");
			try{
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.ERROR,null,null,null));
			}catch(IOException e1){
				e1.printStackTrace();
			}
			e.printStackTrace();
			return;
		}
		//
		ResultSet rs = null;
		try{
			rs = st.executeQuery();
			//
			if(rs.next()){
				if(BCrypt.checkpw(new String(message.getPassword()), rs.getString(3))){
					//NOTE: Since using Strings for passwords should be avoided, a different hashing algorithm should be used in the future
					UserID userID = new UserID(rs.getLong(1));
					char[] authKey = rs.getString(2).toCharArray();
					try{
						connection.sendMessage(new ServerMessage(ServerMessage.MessageID.LOGIN_RESPONSE, new ServerLoginResponseMessage(userID, authKey), null, null));
					}catch(IOException e){
						e.printStackTrace();
					}
				}else{
					//throw new AuthenticationException("Username and password did not match.");
					try{
						connection.sendMessage(new ServerMessage(ServerMessage.MessageID.AUTHENTICATION_ERROR,null,null,null));
					}catch(IOException e){
						e.printStackTrace();
					}
					return;
				}
			}else{
				try{
					connection.sendMessage(new ServerMessage(ServerMessage.MessageID.AUTHENTICATION_ERROR,null,null,null));
				}catch(IOException e){
					e.printStackTrace();
				}
				return;
			}
		}catch(SQLException e){
			try{
				connection.sendMessage(new ServerMessage(ServerMessage.MessageID.ERROR,null,null,null));
			}catch(IOException e1){
				e1.printStackTrace();
			}
			e.printStackTrace();
			return;
		}
	}
	//
	private int getClientPermissions(UserID userID, char[] authKey){
		// returns:
		//   -1	: error with authentication
		//   0	: not logged in (null userID supplied)
		//   >0	: return from table
		final int ERROR_CODE = -1;
		final int NOT_LOGGED_IN_CODE = 0;
		//
		if(userID == null){
			return NOT_LOGGED_IN_CODE;
		}
		
		try{
			if(validateAuthenticationKey(userID, authKey) == false){
				return ERROR_CODE;
			}
		}catch(AuthenticationException e){
			e.printStackTrace();
			return ERROR_CODE;
		}
		//
		PreparedStatement st;
		try{
			st = dbConnection.prepareStatement("SELECT permissions FROM user_accounts WHERE user_id = ? LIMIT 1");
			st.setLong(1, userID.id);
		}catch(SQLException e){
			e.printStackTrace();
			return ERROR_CODE;
		}
		//
		ResultSet rs = null;
		try{
			rs = st.executeQuery();
			//
			if(rs.next()){
				return rs.getInt(1);
			}else{
				return ERROR_CODE;
			}
		}catch(SQLException e){
			e.printStackTrace();
			return ERROR_CODE;
		}
	}
	//
	private boolean validateAuthenticationKey(UserID userID, char[] authKey) throws AuthenticationException{
		PreparedStatement st;
		try{
			st = dbConnection.prepareStatement("SELECT auth_key FROM user_accounts WHERE user_id = ? LIMIT 1");
			st.setLong(1, userID.id);
		}catch(SQLException e){
			throw new AuthenticationException("There shouldn't be an error here.", e);
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
			throw new AuthenticationException("Check the Authentication SQL code.", e);
		}
	}
}
