package lakehead.grouptwo.residence_management_system;
//
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.gui.ResidenceManagementSystemGUI;
import lakehead.grouptwo.residence_management_system.implemented_gateways.client_sql.ClientSQLAccountData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.client_sql.ClientSQLResidenceData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.client_sql.ClientSQLUserData;
//
public class ResidenceManagementSystem{
	public static void main(String[] args){
		new ResidenceManagementSystem();
	}
	//
	public ResidenceManagementSystem(){
		Connection dbConnection = null;
		try{
			Class.forName("org.postgresql.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		//
		try{
			dbConnection = DriverManager.getConnection("jdbc:postgresql:ResidenceManagementSystem", "testLogin", "abc");
		}catch(SQLException e){
			e.printStackTrace();
		}
		//
		IResidenceDataGateway residenceDataGateway = new ClientSQLResidenceData(dbConnection);
		IUserDataGateway userDataGateway = new ClientSQLUserData(dbConnection);
		/*
		char[] password = "my_pass".toCharArray();
		ClientSQLAccountData accountData = null;
		try{
			accountData = new ClientSQLAccountData(dbConnection, "sengler", password);
		}catch(AuthenticationException ae){
			System.out.println(ae.getMessage());
		}
		*/
		//
		ResidenceManagementSystemGUI gui = new ResidenceManagementSystemGUI(residenceDataGateway, userDataGateway, dbConnection);
		//accountData.logOut();
	}
}