package lakehead.grouptwo.residence_management_system.data;
//
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class User{
	public final UserID id;
	private IResidenceDataGateway residenceData;
	private IUserDataGateway userData;
	//
	public User(UserID _id, IResidenceDataGateway _residenceData, IUserDataGateway _userData){
		id = _id;
		residenceData = _residenceData;
		userData = _userData;
	}
	//
	public String getFirstName(){
		return userData.getUserFirstName(id);
	}
	public String getLastName(){
		return userData.getUserLastName(id);
	}
}