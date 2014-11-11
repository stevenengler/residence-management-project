package lakehead.grouptwo.residence_management_system.data;
//
import java.util.Vector;

import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.MessageID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
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
	public String getFirstName() throws Exception{
		return userData.getUserFirstName(id);
	}
	public String getLastName() throws Exception{
		return userData.getUserLastName(id);
	}
	public int getPermissions() throws Exception{
		return userData.getUserPermissions(id);
	}
	public Vector<MessageID> getReceivedMessages() throws Exception{
		return residenceData.getUsersReceivedMessages(id);
	}
	public Vector<MessageID> getUnreadReceivedMessages() throws Exception{
		return residenceData.getUsersUnreadReceivedMessages(id);
	}
	public Room getResidenceRoom() throws Exception{
		RoomID room = residenceData.getRoomOccupiedByUser(id);
		if(room != null){
			return new Room(room, residenceData, userData);
		}else{
			return null;
		}
	}
	public Vector<Building> getManagedBuildings() throws Exception{
		Vector<BuildingID> ids = residenceData.getBuildingsManagedByUser(id);
		Vector<Building> buildings = new Vector<Building>();
		for(int i=0; i<ids.size(); i++){
			buildings.add(new Building(ids.elementAt(i), residenceData, userData));
		}
		return buildings;
	}
}