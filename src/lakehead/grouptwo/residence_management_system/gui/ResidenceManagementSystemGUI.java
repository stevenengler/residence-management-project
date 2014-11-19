package lakehead.grouptwo.residence_management_system.gui;
//
import java.sql.Connection;

import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ConnectionToServer;
//
public class ResidenceManagementSystemGUI{
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	//private IAccountData accountData;
	private ConnectionToServer connectionToServer;
	//
	public ResidenceManagementSystemGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, ConnectionToServer _connectionToServer){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		//accountData = _accountData;
		connectionToServer = _connectionToServer;
		//
		startGUI();
	}
	//
	public void startGUI(){
		new StartupGUI(residenceDataGateway, userDataGateway, connectionToServer).setVisible(true);
	}
}