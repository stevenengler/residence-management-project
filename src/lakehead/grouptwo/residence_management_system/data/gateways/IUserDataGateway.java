package lakehead.grouptwo.residence_management_system.data.gateways;
//
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public interface IUserDataGateway{
	// these are all the methods that are available for the users' information
	//
	public String getUserFirstName(UserID userID) throws Exception;
	public String getUserLastName(UserID userID) throws Exception;
	public int getUserPermissions(UserID userID) throws Exception;
}