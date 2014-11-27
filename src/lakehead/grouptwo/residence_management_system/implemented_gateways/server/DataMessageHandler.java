package lakehead.grouptwo.residence_management_system.implemented_gateways.server;
//
import java.io.IOException;
import java.util.Vector;

import lakehead.grouptwo.residence_management_server.messages.MessageDataID;
import lakehead.grouptwo.residence_management_server.messages.ServerCommandMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerRequestMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerSendMessage;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class DataMessageHandler{
	private ConnectionToServer connectionToServer;
	//
	public DataMessageHandler(ConnectionToServer _connectionToServer){
		connectionToServer = _connectionToServer;
	}
	//
	public Object getDataMessageFromServer(MessageDataID dataID, Object option, UserID userID, char[] authKey) throws IOException, AuthenticationException{
		//
		// Send a request to the server for the specified data:
		//
		try{
			connectionToServer.sendMessage(new ServerMessage(ServerMessage.MessageID.REQUESTING_DATA, new ServerRequestMessage(dataID, option), userID, authKey));
		}catch(IOException e){
			e.printStackTrace();
			throw new IOException("Error while sending the request.");
		}
		//
		// Get the response from the server:
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
		}else if(responseMessage.getID() == ServerMessage.MessageID.AUTHENTICATION_ERROR){
			throw new AuthenticationException("The user id and auth_key were not valid.");
		}else if(responseMessage.getID() == ServerMessage.MessageID.PERMISSION_ERROR){
			throw new AuthenticationException("Do not have permissions for this.");
		}else if(responseMessage.getID() == ServerMessage.MessageID.UNEXPECTED_MESSAGE_DATA_TYPE){
			throw new IOException("Server didn't recognize the data it was sent.");
		}
		//
		// Get the sub-message containing the data:
		//
		ServerSendMessage responseSendMessage = (ServerSendMessage)responseMessage.getObject();
		//
		if(responseSendMessage.getID() != dataID){
			throw new IOException("Server error while getting the response (2).");
		}
		//
		// Return the object in the sub-message:
		//
		return responseSendMessage.getObject();
	}
	//
	public void sendDataMessageToServer(ServerCommandMessage.CommandMessageID dataID, Vector<Object> option, UserID userID, char[] authKey) throws IOException, AuthenticationException{
		//
		// Send the data to the server as an execute command:
		//
		try{
			connectionToServer.sendMessage(new ServerMessage(ServerMessage.MessageID.EXECUTE_COMMAND, new ServerCommandMessage(dataID, option), userID, authKey));
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
		// Make sure the response was OKAY:
		//
		if(responseMessage.getID() == ServerMessage.MessageID.ERROR){
			throw new IOException("Server error while getting the response (1).");
		}else if(responseMessage.getID() == ServerMessage.MessageID.AUTHENTICATION_ERROR){
			throw new AuthenticationException("The user id and auth_key were not valid.");
		}else if(responseMessage.getID() == ServerMessage.MessageID.PERMISSION_ERROR){
			throw new AuthenticationException("Do not have permissions for this.");
		}else if(responseMessage.getID() == ServerMessage.MessageID.UNEXPECTED_MESSAGE_DATA_TYPE){
			throw new IOException("Server didn't recognize the data it was sent.");
		}else if(responseMessage.getID() == ServerMessage.MessageID.OKAY){
			return;
		}else{
			throw new IOException("Unknown error with the response ("+responseMessage.getID()+"). Request may or may not have gone through.");
		}
	}
}
