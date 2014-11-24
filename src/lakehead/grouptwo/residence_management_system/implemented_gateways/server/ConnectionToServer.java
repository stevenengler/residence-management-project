package lakehead.grouptwo.residence_management_system.implemented_gateways.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import lakehead.grouptwo.residence_management_server.messages.MessageDataID;
import lakehead.grouptwo.residence_management_server.messages.ServerCommandMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerRequestMessage;
import lakehead.grouptwo.residence_management_server.messages.ServerSendMessage;
import lakehead.grouptwo.residence_management_system.data.AuthenticationException;

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
		outStream.flush();
	}
	public ServerMessage readMessage() throws IOException{
		try{
			return (ServerMessage) inStream.readObject();
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
