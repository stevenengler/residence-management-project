package lakehead.grouptwo.residence_management_server.messages;

import java.io.Serializable;

import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;

public class ServerLoginResponseMessage implements Serializable{
	private static final long serialVersionUID = 4754047671052384974L;
	//
	//public enum RequestMessageID {USER_FIRSTNAME, USER_LASTNAME};
	//
	private UserID userID;
	private char[] authKey;
	//
	public ServerLoginResponseMessage(UserID _userID, char[] _authKey){
		userID = _userID;
		authKey = _authKey;
	}
	//
	public UserID getUserID(){
		return userID;
	}
	public char[] getAuthKey(){
		return authKey;
	}
}
