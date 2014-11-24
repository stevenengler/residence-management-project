package lakehead.grouptwo.residence_management_server.messages;

import java.io.Serializable;

public class ServerLoginRequestMessage implements Serializable{
	private static final long serialVersionUID = 4754047671052384974L;
	//
	private String username;
	private char[] password;
	private String encryptionMethod;
	//
	public ServerLoginRequestMessage(String _username, char[] _password, String _encryptionMethod){
		username = _username;
		password = _password;
		encryptionMethod = _encryptionMethod;
	}
	//
	public String getUsername(){
		return username;
	}
	public char[] getPassword(){
		return password;
	}
	public String getEncryptionMethod(){
		return encryptionMethod;
	}
}
