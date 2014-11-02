package lakehead.grouptwo.residence_management_system.implemented_gateways.dummy;
//
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class DummyAccountData implements IAccountData{
	@Override
	public UserID getThisUserID() {
		return new UserID(1);
	}
}
