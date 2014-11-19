package lakehead.grouptwo.residence_management_server.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//
import java.net.Socket;
import java.net.SocketException;

import lakehead.grouptwo.residence_management_server.ResidenceManagementServerProtocol;
import lakehead.grouptwo.residence_management_server.messages.ServerMessage;

//
public class ConnectionToClient implements Runnable{
	Socket thisSocket;
	ResidenceManagementServerProtocol serverProtocol;
	ObjectInputStream inStream;
	ObjectOutputStream outStream;
	//
	boolean running = true;
	
	//
	public ConnectionToClient(Socket connectionSocket, ResidenceManagementServerProtocol _serverProtocol){
		thisSocket = connectionSocket;
		serverProtocol = _serverProtocol;
		//
		Thread thisThread = new Thread(this);
		thisThread.start();
	}
	
	//
	@Override
	public void run(){
		try{
			// String fromServer;
			// BufferedReader in = new BufferedReader(new
			// InputStreamReader(connection.getInputStream()));
			//
			outStream = new ObjectOutputStream(thisSocket.getOutputStream());
			inStream = new ObjectInputStream(thisSocket.getInputStream());
			//
			while(running){
				serverProtocol.processFromClient((ServerMessage)inStream.readObject(), this);
			}
		}catch(SocketException e){
			// nothing can be done here, the connection's closed
		}catch(EOFException e){
			// nothing can be done here, the connection's closed
		}catch(Exception e){
			System.out.println("Some sort of error here...");
			e.printStackTrace();
		}
		//
		try{
			thisSocket.close();
		}catch(IOException e){
			// nothing alarming if this fails
		}
		running = false;
	}
	//
	public void sendMessage(ServerMessage message) throws IOException{
		outStream.writeObject(message);
	}
	public ServerMessage readMessage() throws IOException{
		try{
			return (ServerMessage)inStream.readObject();
		}catch(ClassNotFoundException e){
			System.out.println("Not using same message version.");
			throw new IOException();
		}
	}
	//
	public void close(){
		running = false;
	}
	public boolean isClosed(){
		return !running;
	}
}
