package lakehead.grouptwo.residence_management_server.messages;

import java.io.Serializable;


public class ServerRequestMessage implements Serializable{
	private static final long serialVersionUID = -222183859953107479L;
	//
	//public enum RequestMessageID {USER_FIRSTNAME, USER_LASTNAME};
	//
	private MessageDataID id;
	private Object object;
	//
	public ServerRequestMessage(MessageDataID _id, Object _object){
		id = _id;
		object = _object;
	}
	//
	public MessageDataID getID(){
		return id;
	}
	public Object getObject(){
		return object;
	}
}
