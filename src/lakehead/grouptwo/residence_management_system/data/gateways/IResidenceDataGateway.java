package lakehead.grouptwo.residence_management_system.data.gateways;
//
import java.util.Vector;

import lakehead.grouptwo.residence_management_system.data.Building;
import lakehead.grouptwo.residence_management_system.data.Room;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public interface IResidenceDataGateway{
	//public Room getRoomFromID(RoomID roomID);
	//public Building getBuildingFromID(BuildingID buildingID);
	public Vector<RoomID> getAllAvailableRoomsInBuilding(BuildingID buidlingID);
	public int getNumberOfAvailableRoomsInBuilding(BuildingID buidlingID);
	public Vector<UserID> getOccupantsOfRoom(RoomID roomID);
}