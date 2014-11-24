package lakehead.grouptwo.residence_management_system.implemented_gateways.server;
//
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import lakehead.grouptwo.residence_management_server.messages.MessageDataID;
import lakehead.grouptwo.residence_management_server.messages.ServerMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerRequestMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerSendMessage;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class ServerUserData implements IUserDataGateway{
	private ConnectionToServer connectionToServer;
	private ServerAccountData accountData = null;
	//
	public ServerUserData(ConnectionToServer _connectionToServer){
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
		if(responseMessage.getID() == ServerMessage.MessageID.UNEXPECTED_MESSAGE_DATA_TYPE){
			throw new IOException("Server didn't recognize the data it was sent.");
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
	//
	@Override
	public String getUserFirstName(UserID userID) throws IOException, AuthenticationException{
		return (String)getFromServer(MessageDataID.USER_FIRSTNAME, userID);
	}
	//
	@Override
	public String getUserLastName(UserID userID) throws IOException, AuthenticationException{
		return (String)getFromServer(MessageDataID.USER_LASTNAME, userID);
	}
	//
	@Override
	public int getUserPermissions(UserID userID) throws IOException, AuthenticationException{
		return (int)getFromServer(MessageDataID.USER_PERMISSIONS, userID);
	}
}