package lakehead.grouptwo.residence_management_server.messages;

import java.io.Serializable;

public class ServerDataMessage implements Serializable{
	private static final long serialVersionUID = 6429306714448859130L;
	//
	private MessageDataID id;
	private Object object;
	//
	public ServerDataMessage(MessageDataID _id, Object _object){
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
