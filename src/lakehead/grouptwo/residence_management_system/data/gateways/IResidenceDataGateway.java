package lakehead.grouptwo.residence_management_system.data.gateways;
//
import java.util.Vector;
//
import lakehead.grouptwo.residence_management_system.data.identifiers.ApplicationID;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.MessageID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public interface IResidenceDataGateway{
	// these are all the methods that are available for the residence management system
	// there are a lot of methods in this interface, and it should possibly be split up in the future
	//
	// building methods:
	public Vector<RoomID> getAllAvailableRoomsInBuilding(BuildingID buildingID) throws Exception;
	public long getNumberOfAvailableRoomsInBuilding(BuildingID buildingID) throws Exception;
	public Vector<RoomID> getAllRoomsInBuilding(BuildingID buildingID) throws Exception;
	public long getNumberOfRoomsInBuilding(BuildingID buildingID) throws Exception;
	public UserID getManagerOfBuilding(BuildingID buildingID) throws Exception;
	public Vector<BuildingID> getAllBuildings() throws Exception;
	public String getNameOfBuilding(BuildingID buildingID) throws Exception;
	//
	// room methods:
	public Vector<UserID> getOccupantsOfRoom(RoomID roomID) throws Exception;
	public BuildingID getBuildingThatContainsRoom(RoomID roomID) throws Exception;
	public int getCapacityOfRoom(RoomID roomID) throws Exception;
	public String getDevicesInRoom(RoomID roomID) throws Exception;
	public void setUserRoom(UserID userID, RoomID roomID) throws Exception;
	//
	// messages methods:
	public String getContentsOfMessage(MessageID messageID) throws Exception;
	public boolean getReadStatusOfMessage(MessageID messageID) throws Exception;
	public void setReadStatusOfMessage(MessageID messageID, boolean setTo) throws Exception;
	public void sendMessage(UserID fromUser, UserID toUser, String contents) throws Exception;
	//
	// user methods:
	public Vector<MessageID> getUsersReceivedMessages(UserID userID) throws Exception;
	public Vector<MessageID> getUsersUnreadReceivedMessages(UserID userID) throws Exception;
	public RoomID getRoomOccupiedByUser(UserID userID) throws Exception;
	public Vector<BuildingID> getBuildingsManagedByUser(UserID userID) throws Exception;
	//
	// application methods:
	public void applyForResidence(UserID userID, int yearLevel, String requests) throws Exception;
	public Vector<ApplicationID> getResidenceApplications(int numToFetch) throws Exception;
	public UserID getApplicationUser(ApplicationID applicationID) throws Exception;
	public int getApplicationYearLevel(ApplicationID applicationID) throws Exception;
	public String getApplicationSpecialRequests(ApplicationID applicationID) throws Exception;
	public void removeApplication(ApplicationID applicationID) throws Exception;
	public long getNumberOfApplications() throws Exception;
}