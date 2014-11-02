package lakehead.grouptwo.residence_management_system.data;
//
import java.util.Vector;

import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
//
public class Building{
	public final BuildingID id;
	private IResidenceDataGateway residenceData;
	private IUserDataGateway userData;
	//
	public Building(BuildingID _id, IResidenceDataGateway _residenceData, IUserDataGateway _userData){
		id = _id;
		residenceData = _residenceData;
		userData = _userData;
	}
	//
	public int getNumberOfAvailableRooms(){
		return residenceData.getNumberOfAvailableRoomsInBuilding(id);
	}
	public Vector<Room> getAllAvailableRooms(){
		Vector<RoomID> ids = residenceData.getAllAvailableRoomsInBuilding(id);
		Vector<Room> rooms = new Vector<Room>();
		for(int i=0; i<ids.size(); i++){
			rooms.add(new Room(ids.elementAt(i), residenceData, userData));
		}
		return rooms;
	}
}