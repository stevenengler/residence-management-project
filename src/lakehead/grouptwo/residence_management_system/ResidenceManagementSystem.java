package lakehead.grouptwo.residence_management_system;
//
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.gui.ResidenceManagementSystemGUI;
import lakehead.grouptwo.residence_management_system.implemented_gateways.dummy.DummyAccountData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.dummy.DummyResidenceData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.dummy.DummyUserData;
//
public class ResidenceManagementSystem{
	public static void main(String[] args){
		new ResidenceManagementSystem();
	}
	//
	public ResidenceManagementSystem(){
		IResidenceDataGateway residenceDataGateway = new DummyResidenceData();
		IUserDataGateway userDataGateway = new DummyUserData();
		IAccountData accountData = new DummyAccountData();
		//
		ResidenceManagementSystemGUI gui = new ResidenceManagementSystemGUI(residenceDataGateway, userDataGateway, accountData);
	}
}