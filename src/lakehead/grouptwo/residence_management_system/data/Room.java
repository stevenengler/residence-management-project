package lakehead.grouptwo.residence_management_system.data;
//
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
//
public class Room{
	public final RoomID id;
	private IResidenceDataGateway residenceData;
	private IUserDataGateway userData;
	//
	public Room(RoomID _id, IResidenceDataGateway _residenceData, IUserDataGateway _userData){
		id = _id;
		residenceData = _residenceData;
		userData = _userData;
	}
	//
}