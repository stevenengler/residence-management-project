package lakehead.grouptwo.residence_management_server.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import lakehead.grouptwo.residence_management_server.ResidenceManagementServerProtocol;
import lakehead.grouptwo.residence_management_server.messages.ServerMessage;

//
public class ConnectionToClient implements Runnable{
	Socket thisSocket;
	ResidenceManagementServerProtocol serverProtocol;
	// the protocol to handle the messages
	//
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
		// run this object as a thread
	}
	//
	@Override
	public void run(){
		try{
			outStream = new ObjectOutputStream(thisSocket.getOutputStream());
			inStream = new ObjectInputStream(thisSocket.getInputStream());
		}catch(SocketException e){
			// nothing can be done here, the connection's probably closed
		}catch(EOFException e){
			// nothing can be done here, the connection's probably closed
		}catch(IOException e){
			// should show this error to the server admin since it's an unknown error
			e.printStackTrace();
		}
		//
		try{
			while(running){
				// this should loop every x seconds assuming that the timeout for the socket was set
				try{
					ServerMessage receivedMessage = readMessage();
					serverProtocol.processFromClient(receivedMessage, this);
				}catch(SocketTimeoutException e){
					// do nothing and restart loop
				}catch(SocketException e){
					// close connection
					running = false;
				}
			}
		}catch(IOException e){
			// if it's not a ServerMessage object, then we don't know what it is
			try{
				e.printStackTrace();
				sendMessage(new ServerMessage(ServerMessage.MessageID.UNEXPECTED_MESSAGE_DATA_TYPE, null, null, null));
			}catch(IOException e1){
				// nothing can be done here, the connection's probably closed
			}
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
	public void sendMessage(ServerMessage message) throws SocketException, IOException{
		outStream.writeObject(message);
		outStream.flush();
	}
	public ServerMessage readMessage() throws IOException{
		try{
			Object sentFromClient = inStream.readObject();
			if(sentFromClient instanceof ServerMessage){
				return (ServerMessage)sentFromClient;
			}else{
				throw new IOException("Received object that wasn't the correct type (ServerMessage).");
			}
		}catch(ClassNotFoundException e){
			// problem with the serializable UID
			throw new IOException("Not using same message version.", e);
		}catch(SocketException e){
			// connection was likely closed, nothing we can do here
			throw e;
			
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
