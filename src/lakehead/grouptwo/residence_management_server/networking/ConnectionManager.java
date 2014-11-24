package lakehead.grouptwo.residence_management_server.networking;
//
import java.util.Vector;
//
public class ConnectionManager{
	Vector<ConnectionToClient> connectionsToClients = new Vector<ConnectionToClient>();
	//
	public ConnectionManager(){
	}
	//
	public void addConnection(ConnectionToClient newConnection){
		connectionsToClients.add(newConnection);
	}
	//
	public void closeAllConnections(){
		for(int i=0; i<connectionsToClients.size(); i++){
			connectionsToClients.get(0).close();
			connectionsToClients.remove(0);
		}
	}
}
