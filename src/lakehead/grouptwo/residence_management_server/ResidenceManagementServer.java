package lakehead.grouptwo.residence_management_server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
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
			// the timeout for the server is needed so that the server can quit if dbConnection.close() is called
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
				// the JDBC driver needed for the PostgreSQL database
			}catch(ClassNotFoundException e){
				initializationError = true;
				System.err.println("Could not load the PostgreSQL Driver.");
				e.printStackTrace();
			}
		}
		//
		Connection dbConnection = null;
		//
		if(!initializationError){
			try{
				dbConnection = DriverManager.getConnection("jdbc:postgresql:ResidenceManagementSystem", "residence_management_server", "password_a$g3e4rg45y57u");
			}catch(SQLException e){
				initializationError = true;
				System.err.println("Could not connect to the database.");
				e.printStackTrace();
			}
		}
		//
		if(!initializationError){
			ResidenceManagementServerProtocol serverProtocol = new ResidenceManagementServerProtocol(dbConnection);
			ConnectionManager manager = new ConnectionManager();
			ConnectionListener connectionListener = new ConnectionListener(serverSocket, manager, serverProtocol);
			//
			Enumeration<NetworkInterface> networkInterfaces = null;
			try{
				networkInterfaces = NetworkInterface.getNetworkInterfaces();
			}catch(SocketException e){
				e.printStackTrace();
			}
			//
			System.out.println("The following are the host addresses from the network interfaces:");
			//
			while(networkInterfaces != null && networkInterfaces.hasMoreElements())
			{
			    NetworkInterface n = (NetworkInterface) networkInterfaces.nextElement();
			    Enumeration<InetAddress> ee = n.getInetAddresses();
			    while (ee.hasMoreElements())
			    {
			        InetAddress i = (InetAddress)ee.nextElement();
			        System.out.println("\tHost address: "+i.getHostAddress());
			    }
			}
			//
			System.out.println();
			System.out.println("The port number is: "+portNumber);
			//
			System.out.println();
			System.out.println("Enter the line \"stop\" to close open-connections and stop the server.");
			//
			Scanner keyboard = new Scanner(System.in);
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
			//
			keyboard.close();
			//
			System.out.println("Stopping (please wait at least 10 seconds)...");
		}
	}
}
