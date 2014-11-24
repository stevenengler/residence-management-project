package lakehead.grouptwo.residence_management_server.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import lakehead.grouptwo.residence_management_server.ResidenceManagementServerProtocol;

public class ConnectionListener implements Runnable{
	ServerSocket serverSocket;
	ConnectionManager connectionManager;
	Thread serverThread;
	ResidenceManagementServerProtocol serverProtocol;
	//
	boolean running = true;
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
			Socket clientSocket;
			while(running){
				clientSocket = null;
				try{
					clientSocket = serverSocket.accept();
				}catch(SocketTimeoutException e){
					// do nothing since we want the while loop to start over (and check if still running)
				}
				if(clientSocket != null){
					clientSocket.setSoTimeout(10*1000);
					// 10 seconds
					connectionManager.addConnection(new ConnectionToClient(clientSocket, serverProtocol));
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		running = false;
	}
	//
	public void close(){
		running = false;
	}
	public boolean isClosed(){
		return !running;
	}
}
