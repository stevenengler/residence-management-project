package lakehead.grouptwo.residence_management_server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import lakehead.grouptwo.residence_management_server.networking.ConnectionListener;
import lakehead.grouptwo.residence_management_server.networking.ConnectionManager;

public class ResidenceManagementServer{
	public static void main(String[] args){
		int port = Integer.parseInt(args[0]);
		new ResidenceManagementServer(port);
	}
	//
	ResidenceManagementServer(int portNumber){
		boolean initializationError = false;
		//
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(portNumber);
			serverSocket.setSoTimeout(10*1000);
			// 10 seconds
		}catch(BindException e){
			initializationError = true;
			System.err.println("It looks like there already is a server running on port "+portNumber+".");
			System.err.println("Please try a different port number.");
		}catch(IOException e){
			initializationError = true;
			System.err.println("Could not create server.");
			e.printStackTrace();
		}
		//
		if(!initializationError){
			try{
				Class.forName("org.postgresql.Driver");
			}catch(ClassNotFoundException e){
				initializationError = true;
				System.err.println("Could not load Postgre Driver.");
				e.printStackTrace();
			}
		}
		//
		Connection dbConnection = null;
		//
		if(!initializationError){
			try{
				dbConnection = DriverManager.getConnection("jdbc:postgresql:ResidenceManagementSystem", "testLogin", "abc");
			}catch(SQLException e){
				initializationError = true;
				System.err.println("Could not connect to database.");
				e.printStackTrace();
			}
		}
		//
		if(!initializationError){
			ResidenceManagementServerProtocol serverProtocol = new ResidenceManagementServerProtocol(dbConnection);
			ConnectionManager manager = new ConnectionManager();
			ConnectionListener connectionListener = new ConnectionListener(serverSocket, manager, serverProtocol);
			//
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Enter the line \"stop\" to close open-connections and stop the server.");
			//
			boolean checkForInput = true;
			//
			while(checkForInput && keyboard.hasNext() == true){
				String inputString = keyboard.next();
				if(inputString.equalsIgnoreCase("stop")){
					connectionListener.close();
					manager.closeAllConnections();
					try{
						dbConnection.close();
					}catch(SQLException e){
						// nothing to do here since we're ending the program anyways
					}
					checkForInput = false;
				}else if(!inputString.equals("")){
					System.out.println("Command is not recognized.");
				}
			}
			System.out.println("Stopping (please wait at least 10 seconds)...");
		}
	}
}
