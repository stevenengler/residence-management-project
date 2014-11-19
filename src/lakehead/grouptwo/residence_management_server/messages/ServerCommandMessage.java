package lakehead.grouptwo.residence_management_server.messages;

import java.io.Serializable;
import java.util.Vector;

public class ServerCommandMessage implements Serializable{
	private static final long serialVersionUID = -1635178988480831574L;
	//
	public enum CommandMessageID {
									SET_MESSAGE_READ_STATUS, SEND_MESSAGE_TO_USER, APPLY_FOR_RESIDENCE,
									SET_USER_ROOM, REMOVE_APPLICATION
									};
	//
	private CommandMessageID id;
	private Vector<Object> objects;
	//
	public ServerCommandMessage(CommandMessageID _id, Vector<Object> _objects){
		id = _id;
		objects = _objects;
	}
	//
	public CommandMessageID getID(){
		return id;
	}
	public Vector<Object> getObjects(){
		return objects;
	}
}
