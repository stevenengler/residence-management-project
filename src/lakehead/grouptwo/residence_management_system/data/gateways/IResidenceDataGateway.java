package lakehead.grouptwo.residence_management_system.data.gateways;
//
import java.util.Vector;

import lakehead.grouptwo.residence_management_system.data.Building;
import lakehead.grouptwo.residence_management_system.data.Room;
import lakehead.grouptwo.residence_management_system.data.identifiers.ApplicationID;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.MessageID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;
//
public interface IResidenceDataGateway{
	//public Room getRoomFromID(RoomID roomID);
	//public Building getBuildingFromID(BuildingID buildingID);
	public Vector<RoomID> getAllAvailableRoomsInBuilding(BuildingID buildingID) throws Exception;
	public long getNumberOfAvailableRoomsInBuilding(BuildingID buildingID) throws Exception;
	public Vector<UserID> getOccupantsOfRoom(RoomID roomID) throws Exception;
	public Vector<MessageID> getUsersReceivedMessages(UserID userID) throws Exception;
	public Vector<MessageID> getUsersUnreadReceivedMessages(UserID userID) throws Exception;
	public String getContentsOfMessage(MessageID messageID) throws Exception;
	public boolean getReadStatusOfMessage(MessageID messageID) throws Exception;
	public void setReadStatusOfMessage(MessageID messageID, boolean setTo) throws Exception;
	public void sendMessage(UserID fromUser, UserID toUser, String contents) throws Exception;
	public RoomID getRoomOccupiedByUser(UserID userID) throws Exception;
	public BuildingID getBuildingThatContainsRoom(RoomID roomID) throws Exception;
	public UserID getManagerOfBuilding(BuildingID buildingID) throws Exception;
	public Vector<BuildingID> getBuildingsManagedByUser(UserID userID) throws Exception;
	public Vector<RoomID> getAllRoomsInBuilding(BuildingID buildingID) throws Exception;
	public long getNumberOfRoomsInBuilding(BuildingID buildingID) throws Exception;
	public void applyForResidence(UserID userID, int yearLevel, String requests) throws Exception;
	public Vector<ApplicationID> getResidenceApplications(int numToFetch) throws Exception;
	public UserID getApplicationUser(ApplicationID applicationID) throws Exception;
	public int getApplicationYearLevel(ApplicationID applicationID) throws Exception;
	public String getApplicationSpecialRequests(ApplicationID applicationID) throws Exception;
	public void setUserRoom(UserID userID, RoomID roomID) throws Exception;
	public void removeApplication(ApplicationID applicationID) throws Exception;
	public long getNumberOfApplications() throws Exception;
}