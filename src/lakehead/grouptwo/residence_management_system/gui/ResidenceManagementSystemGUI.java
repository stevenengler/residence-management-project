package lakehead.grouptwo.residence_management_system.gui;
//
import java.sql.Connection;

import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
//
public class ResidenceManagementSystemGUI{
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	//private IAccountData accountData;
	private Connection dbConnection;
	//
	public ResidenceManagementSystemGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, Connection _dbConnection){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		//accountData = _accountData;
		dbConnection = _dbConnection;
		//
		startGUI();
	}
	//
	public void startGUI(){
		//GUI will go here
		//new StartupGUI(residenceDataGateway, userDataGateway, dbConnection).setVisible(true);
	}
}