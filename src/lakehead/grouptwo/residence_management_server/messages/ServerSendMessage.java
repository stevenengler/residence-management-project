package lakehead.grouptwo.residence_management_server.messages;

public class ServerSendMessage extends ServerDataMessage{
	private static final long serialVersionUID = -5296540432873918300L;
	//
	public ServerSendMessage(MessageDataID id, Object object){
		super(id, object);
	}
}
