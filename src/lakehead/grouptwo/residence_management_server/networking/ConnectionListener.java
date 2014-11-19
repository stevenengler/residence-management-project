package lakehead.grouptwo.residence_management_server.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lakehead.grouptwo.residence_management_server.ResidenceManagementServerProtocol;

public class ConnectionListener implements Runnable{
	ServerSocket serverSocket;
	ConnectionManager connectionManager;
	Thread serverThread;
	ResidenceManagementServerProtocol serverProtocol;
	//
	public ConnectionListener(ServerSocket _serverSocket, ConnectionManager _socketManager, ResidenceManagementServerProtocol _serverProtocol){
		serverSocket = _serverSocket;
		connectionManager = _socketManager;
		serverProtocol = _serverProtocol;
		//
        serverThread = new Thread(this);
        serverThread.start();
	}
	//
	@Override
	public void run(){
		try{
			while(true){
				Socket clientSocket = serverSocket.accept();
				connectionManager.addConnection(new ConnectionToClient(clientSocket, serverProtocol));
			}
		}catch(IOException e){
			System.out.println("Please insert a sandwich into the CD tray.");
		}
	}
}
