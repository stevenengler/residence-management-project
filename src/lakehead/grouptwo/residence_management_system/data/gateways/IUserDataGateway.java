package lakehead.grouptwo.residence_management_system.data.gateways;
import lakehead.grouptwo.residence_management_system.data.User;
//
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public interface IUserDataGateway{
	//public User getUserFromID(UserID userID);
	public String getUserFirstName(UserID userID);
	public String getUserLastName(UserID userID);
}