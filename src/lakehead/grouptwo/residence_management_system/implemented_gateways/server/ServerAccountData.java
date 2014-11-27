package lakehead.grouptwo.residence_management_system.implemented_gateways.server;
//
import java.io.IOException;
//
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
	// the authentication key is stored in a char array so that it can be cleared from memory (for security purposes), whereas a String can't be
	//
	public ServerAccountData(ConnectionToServer _connectionToServer, String username, char[] password) throws AuthenticationException{
		connectionToServer = _connectionToServer;
		//
		// Send the login request:
		//
		try{
			connectionToServer.sendMessage(new ServerMessage(ServerMessage.MessageID.LOGIN_REQUEST, new ServerLoginRequestMessage(username, password, ""), null, null));
		}catch(IOException e){
			throw new AuthenticationException("Error while sending the login request.");
		}
		//
		// Read the login reply:
		//
		ServerMessage responseMessage;
		//
		try{
			responseMessage = connectionToServer.readMessage();
		}catch(IOException e){
			throw new AuthenticationException("Error while reading the login response.");
		}
		//
		// Check for errors in the login reply:
		//
		if(responseMessage.getID() == ServerMessage.MessageID.ERROR){
			throw new AuthenticationException("Server error while getting the login response.");
		}else if(responseMessage.getID() == ServerMessage.MessageID.AUTHENTICATION_ERROR){
			throw new AuthenticationException("The username and password combination is not in the system.");
		}else if(responseMessage.getID() == ServerMessage.MessageID.UNEXPECTED_MESSAGE_DATA_TYPE){
			throw new AuthenticationException("The server did not understand the request.");
		}
		//
		if(responseMessage.getID() != ServerMessage.MessageID.LOGIN_RESPONSE){
			throw new AuthenticationException("Server error while getting the login response (2).");
		}
		//
		// Save the authentication key:
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
		//
		// Ask the server if the user is logged in:
		//
		try{
			connectionToServer.sendMessage(new ServerMessage(ServerMessage.MessageID.REQUESTING_DATA, new ServerRequestMessage(MessageDataID.USER_LOGGED_IN, null), id, authKey));
		}catch(IOException e){
			throw new AuthenticationException("Error while sending the request.");
		}
		//
		// Read the server's reply:
		//
		ServerMessage responseMessage;
		//
		try{
			responseMessage = connectionToServer.readMessage();
		}catch(IOException e){
			throw new AuthenticationException("Error while reading the response.");
		}
		//
		// Check for errors in the server's reply:
		//
		if(responseMessage.getID() == ServerMessage.MessageID.ERROR){
			throw new AuthenticationException("Server error while getting the response (1).");
		}
		if(((ServerSendMessage)responseMessage.getObject()).getID() != MessageDataID.USER_LOGGED_IN){
			throw new AuthenticationException("Server error while getting the response (2).");
		}
		//
		// Get the boolean returned from the user:
		//
		return (boolean)((ServerSendMessage)responseMessage.getObject()).getObject();
	}
	//
	public void logOut(){
		for(int i=0; i<authKey.length; i++){
			// clear every byte of the authentication key individually
			authKey[i] = 0;
		}
	}
}
