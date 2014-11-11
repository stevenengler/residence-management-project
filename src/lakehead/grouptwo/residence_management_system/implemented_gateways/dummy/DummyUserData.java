package lakehead.grouptwo.residence_management_system.implemented_gateways.dummy;
//
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class DummyUserData implements IUserDataGateway{
	@Override
	public String getUserFirstName(UserID userID){
		return "Steven";
	}
	//
	@Override
	public String getUserLastName(UserID userID){
		return "Engler";
	}
	@Override
	public int getUserPermissions(UserID userID) throws Exception{
		return 1;
	}
}