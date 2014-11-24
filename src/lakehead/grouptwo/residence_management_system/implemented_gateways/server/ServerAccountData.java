package lakehead.grouptwo.residence_management_system.implemented_gateways.server;
//
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

import jbcrypt.BCrypt;
import lakehead.grouptwo.residence_management_server.messages.MessageDataID;
import lakehead.grouptwo.residence_management_server.messages.ServerLoginRequestMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerLoginResponseMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerRequestMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerSendMessage;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class ServerAccountData implements IAccountData{
	private ConnectionToServer connectionToServer;
	private UserID id;
	private char[] authKey;
	//
	public ServerAccountData(ConnectionToServer _connectionToServer, String username, char[] password) throws AuthenticationException{
		connectionToServer = _connectionToServer;
		//
		try{
			connectionToServer.sendMessage(new ServerMessage(ServerMessage.MessageID.LOGIN_REQUEST, new ServerLoginRequestMessage(username, password, ""), null, null));
		}catch(IOException e){
			throw new AuthenticationException("Error while sending the login request.");
		}
		
		ServerMessage responseMessage;
		
		try{
			responseMessage = connectionToServer.readMessage();
		}catch(IOException e){
			throw new AuthenticationException("Error while reading the login response.");
		}
		
		if(responseMessage.getID() == ServerMessage.MessageID.ERROR){
			throw new AuthenticationException("Server error while getting the login response.");
		}
		if(responseMessage.getID() == ServerMessage.MessageID.AUTHENTICATION_ERROR){
			throw new AuthenticationException("The username and password combination is not in the system.");
		}
		if(responseMessage.getID() == ServerMessage.MessageID.UNEXPECTED_MESSAGE_DATA_TYPE){
			throw new AuthenticationException("The server did not understand the request.");
		}
		//
		id = ((ServerLoginResponseMessage)responseMessage.getObject()).getUserID();
		authKey = ((ServerLoginResponseMessage)responseMessage.getObject()).getAuthKey();
	}
	//
	@Override
	public UserID getThisUserID() {
		return id;
	}
	//
	public char[] getAuthKey() {
		return authKey;
	}
	//
	public boolean checkIfLoggedIn() throws AuthenticationException{
		try{
			connectionToServer.sendMessage(new ServerMessage(ServerMessage.MessageID.REQUESTING_DATA, new ServerRequestMessage(MessageDataID.USER_LOGGED_IN, null), id, authKey));
		}catch(IOException e){
			throw new AuthenticationException("Error while sending the request.");
		}
		
		ServerMessage responseMessage;
		
		try{
			responseMessage = connectionToServer.readMessage();
		}catch(IOException e){
			throw new AuthenticationException("Error while reading the response.");
		}
		
		if(responseMessage.getID() == ServerMessage.MessageID.ERROR){
			throw new AuthenticationException("Server error while getting the response (1).");
		}
		if(((ServerSendMessage)responseMessage.getObject()).getID() != MessageDataID.USER_LOGGED_IN){
			throw new AuthenticationException("Server error while getting the response (2).");
		}
		
		return (boolean)((ServerSendMessage)responseMessage.getObject()).getObject();
	}
	//
	public void logOut(){
		for(int i=0; i<authKey.length; i++){
			authKey[i] = 0;
		}
	}
}
