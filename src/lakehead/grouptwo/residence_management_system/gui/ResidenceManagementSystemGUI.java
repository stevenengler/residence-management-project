package lakehead.grouptwo.residence_management_system.gui;
//
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
//
public class ResidenceManagementSystemGUI{
	public IResidenceDataGateway residenceDataGateway;
	public IUserDataGateway userDataGateway;
	public IAccountData accountData;
	//
	public ResidenceManagementSystemGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, IAccountData _accountData){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		accountData = _accountData;
		//
		startGUI();
	}
	//
	public void startGUI(){
		
	}
}