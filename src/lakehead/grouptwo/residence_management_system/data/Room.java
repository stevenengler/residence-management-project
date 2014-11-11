package lakehead.grouptwo.residence_management_system.data;
//
import java.util.Vector;

import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
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
	public Building getContainingBuilding() throws Exception{
		return new Building(residenceData.getBuildingThatContainsRoom(id), residenceData, userData);
	}
	public Vector<User> getOccupyingUsers() throws Exception{
		Vector<UserID> ids = residenceData.getOccupantsOfRoom(id);
		Vector<User> users = new Vector<User>();
		for(int i=0; i<ids.size(); i++){
			users.add(new User(ids.elementAt(i), residenceData, userData));
		}
		return users;
	}
}