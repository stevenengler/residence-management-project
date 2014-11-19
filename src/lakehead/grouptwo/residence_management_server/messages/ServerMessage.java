package lakehead.grouptwo.residence_management_server.messages;

import java.io.Serializable;

import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;

public class ServerMessage implements Serializable{
	private static final long serialVersionUID = -6152139352819626427L;
	//
	public enum MessageID {REQUESTING_DATA, SENDING_DATA, EXECUTE_COMMAND, LOGIN_REQUEST, LOGIN_RESPONSE, CLOSE_CONNECTION, OKAY, ERROR, AUTHENTICATION_ERROR, PERMISSION_ERROR};
	//
	private MessageID id;
	private Object object;
	private char[] authKey;
	private UserID userID;
	//
	public ServerMessage(MessageID _id, Object _object, UserID _userID, char[] _authKey){
		id = _id;
		object = _object;
		authKey = _authKey;
		userID = _userID;
	}
	//
	public MessageID getID(){
		return id;
	}
	public Object getObject(){
		return object;
	}
	public char[] getAuthKey(){
		return authKey;
	}
	public UserID getUserID(){
		return userID;
	}
}
