package lakehead.grouptwo.residence_management_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lakehead.grouptwo.residence_management_server.networking.ConnectionListener;
import lakehead.grouptwo.residence_management_server.networking.ConnectionManager;

public class ResidenceManagementServer{
	public static void main(String[] args){
		new ResidenceManagementServer();
	}
	//
	ResidenceManagementServer(){
		int portNumber = 12023;
		//
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(portNumber);
		}catch(IOException e){
			System.out.println("Eat a Snickers (you're not you when you're hungry).");
			e.printStackTrace();
		}
		//
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
		ResidenceManagementServerProtocol serverProtocol = new ResidenceManagementServerProtocol(dbConnection);
		ConnectionManager manager = new ConnectionManager();
		ConnectionListener connectionListener = new ConnectionListener(serverSocket, manager, serverProtocol);
		//
		
	}
}
