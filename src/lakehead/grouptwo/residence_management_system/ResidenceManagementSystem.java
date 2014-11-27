package lakehead.grouptwo.residence_management_system;
//
import java.io.IOException;
//
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.gui.ResidenceManagementSystemGUI;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ConnectionToServer;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ServerResidenceData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ServerUserData;
//
public class ResidenceManagementSystem{
	public static void main(String[] args){
		if(args.length != 2){
			System.err.println(
					"This program requires 2 arguments:\n" +
						"\t1) Server IP address\n" +
						"\t2) Server port number\n" +
					"Examples:\n" +
						"\t1) java -jar ResidenceManagementSystem.jar localhost 1453\n" +
						"\t2) java -jar ResidenceManagementSystem.jar 192.168.1.56 1453" +
					"");
		}
		//
		int portNum = 0;
		boolean inputIsGood = true;
		//
		try{
			portNum = Integer.parseInt(args[1]);
			if(portNum < 0){
				throw new IllegalArgumentException();
			}
		}catch(NumberFormatException e){
			inputIsGood = false;
			System.err.println("The port number must be a positive integer");
		}
		//
		if(inputIsGood){
			new ResidenceManagementSystem(args[0], portNum);
		}
		//
		//new ResidenceManagementSystem("192.168.105.238", 1253);
		//new ResidenceManagementSystem("localhost", 1253);
	}
	//
	public ResidenceManagementSystem(String serverIP, int serverPort){
		//
		System.out.println("Connecting to "+serverIP+" ...");
		//
		ConnectionToServer connectionToServer = null;
		//
		try{
			connectionToServer = new ConnectionToServer(serverIP, serverPort);
		}catch(IOException e){
			System.err.println("Couldn't connect to the server. Check the IP address and port number arguments, " +
					"and also make sure that the port is not being blocked by a firewall.");
			return;
		}
		//
		System.out.println("Successfully connected.");
		//
		IResidenceDataGateway residenceDataGateway = new ServerResidenceData(connectionToServer);
		IUserDataGateway userDataGateway = new ServerUserData(connectionToServer);
		// the gateways classes provide the program with access to all of the data
		//
		new ResidenceManagementSystemGUI(residenceDataGateway, userDataGateway, connectionToServer);
	}
}