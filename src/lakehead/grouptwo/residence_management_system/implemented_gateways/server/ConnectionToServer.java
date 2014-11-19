package lakehead.grouptwo.residence_management_system.implemented_gateways.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import lakehead.grouptwo.residence_management_server.messages.ServerMessage;

public class ConnectionToServer{
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private Socket socket;
	//
	public ConnectionToServer(String host, int port) throws UnknownHostException, IOException{
		socket = new Socket(host, port);
		outStream = new ObjectOutputStream(socket.getOutputStream());
		inStream = new ObjectInputStream(socket.getInputStream());
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
		try{
			socket.close();
		}catch(IOException e){
			// what else can you do?
		}
	}
}
