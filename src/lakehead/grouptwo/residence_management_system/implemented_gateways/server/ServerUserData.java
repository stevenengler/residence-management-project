package lakehead.grouptwo.residence_management_system.implemented_gateways.server;
//
import java.io.IOException;
//
import lakehead.grouptwo.residence_management_server.messages.MessageDataID;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class ServerUserData implements IUserDataGateway{
	private ConnectionToServer connectionToServer;
	private ServerAccountData accountData = null;
	private DataMessageHandler dataMessageHandler;
	//
	public ServerUserData(ConnectionToServer _connectionToServer){
		connectionToServer = _connectionToServer;
		dataMessageHandler = new DataMessageHandler(connectionToServer);
	}
	//
	public void setAccountData(ServerAccountData _accountData){
		accountData = _accountData;
	}
	//
	private UserID getUserID(){
		if(accountData != null){
			return accountData.getThisUserID();
		}else{
			return null;
		}
	}
	private char[] getAuthKey(){
		if(accountData != null){
			return accountData.getAuthKey();
		}else{
			return null;
		}
	}
	//
	@Override
	public String getUserFirstName(UserID userID) throws IOException, AuthenticationException{
		return (String)dataMessageHandler.getDataMessageFromServer(MessageDataID.USER_FIRSTNAME, userID, getUserID(), getAuthKey());
	}
	//
	@Override
	public String getUserLastName(UserID userID) throws IOException, AuthenticationException{
		return (String)dataMessageHandler.getDataMessageFromServer(MessageDataID.USER_LASTNAME, userID, getUserID(), getAuthKey());
	}
	//
	@Override
	public int getUserPermissions(UserID userID) throws IOException, AuthenticationException{
		return (int)dataMessageHandler.getDataMessageFromServer(MessageDataID.USER_PERMISSIONS, userID, getUserID(), getAuthKey());
	}
}