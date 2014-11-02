package lakehead.grouptwo.residence_management_system.implemented_gateways.dummy;
//
import java.util.Vector;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public class DummyResidenceData implements IResidenceDataGateway{
	@Override
	public Vector<RoomID> getAllAvailableRoomsInBuilding(BuildingID buidlingID) {
		return new Vector<RoomID>();
	}
	//
	@Override
	public int getNumberOfAvailableRoomsInBuilding(BuildingID buidlingID) {
		// TODO Auto-generated method stub
		return 0;
	}
	//
	@Override
	public Vector<UserID> getOccupantsOfRoom(RoomID roomID) {
		// TODO Auto-generated method stub
		return null;
	}
}
