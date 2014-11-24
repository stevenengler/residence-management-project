package lakehead.grouptwo.residence_management_server.messages;

public class ServerRequestMessage extends ServerDataMessage{
	private static final long serialVersionUID = 2117684582672806615L;
	//
	public ServerRequestMessage(MessageDataID id, Object object){
		super(id, object);
	}
}
