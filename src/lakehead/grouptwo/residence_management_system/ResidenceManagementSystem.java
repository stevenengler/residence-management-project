package lakehead.grouptwo.residence_management_system;
//
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lakehead.grouptwo.residence_management_server.messages.ServerMessage;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.gui.ResidenceManagementSystemGUI;
import lakehead.grouptwo.residence_management_system.implemented_gateways.client_sql.ClientSQLAccountData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.client_sql.ClientSQLResidenceData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.client_sql.ClientSQLUserData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ConnectionToServer;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ServerResidenceData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ServerUserData;
//
public class ResidenceManagementSystem{
	public static void main(String[] args){
		new ResidenceManagementSystem();
	}
	//
	public ResidenceManagementSystem(){
		
		ConnectionToServer connectionToServer = null;
		
		try{
			connectionToServer = new ConnectionToServer("localhost", 12023);
		}catch(UnknownHostException e){
			e.printStackTrace();
			return;
		}catch(IOException e){
			e.printStackTrace();
			return;
		}
		
		/*
		if(connectionToServer != null){
			try{
				connectionToServer.sendMessage(new ServerMessage(ServerMessage.MessageID.REQUEST_DATA, new String("hi")));
				System.out.println("Message Sent!");
			}catch(IOException e){
				System.out.println("error here");
				e.printStackTrace();
			}
		}
		*/
		
		/*
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
		*/
		
		//
		//IResidenceDataGateway residenceDataGateway = new ClientSQLResidenceData(dbConnection);
		//IUserDataGateway userDataGateway = new ClientSQLUserData(dbConnection);
		IResidenceDataGateway residenceDataGateway = new ServerResidenceData(connectionToServer);
		IUserDataGateway userDataGateway = new ServerUserData(connectionToServer);
		//
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
		
		ResidenceManagementSystemGUI gui = new ResidenceManagementSystemGUI(residenceDataGateway, userDataGateway, connectionToServer);
		//accountData.logOut();
		//System.out.println("HI");
		//connectionToServer.close();
	}
}