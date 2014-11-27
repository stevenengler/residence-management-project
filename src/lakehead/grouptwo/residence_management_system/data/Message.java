package lakehead.grouptwo.residence_management_system.data;
//
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.MessageID;
//
public class Message{
	public final MessageID id;
	private IResidenceDataGateway residenceData;
	private IUserDataGateway userData;
	//
	public Message(MessageID _id, IResidenceDataGateway _residenceData, IUserDataGateway _userData){
		id = _id;
		residenceData = _residenceData;
		userData = _userData;
	}
	//
	public String getContents() throws Exception{
		return residenceData.getContentsOfMessage(id);
	}
	public boolean getReadStatus() throws Exception{
		return residenceData.getReadStatusOfMessage(id);
	}
	public void setReadStatus(boolean setTo) throws Exception{
		residenceData.setReadStatusOfMessage(id, setTo);
	}
}