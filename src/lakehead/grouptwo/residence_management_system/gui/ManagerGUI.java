package lakehead.grouptwo.residence_management_system.gui;
//
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
//
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
//
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.Message;
import lakehead.grouptwo.residence_management_system.data.User;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.MessageID;
//
public class ManagerGUI extends VerticallyStackedMenu{
	//
	private static final long serialVersionUID = -5363908732688095195L;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	private IAccountData accountData;
	//
	public ManagerGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, IAccountData _accountData){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		accountData = _accountData;
		//
		setTitle("Manager Homepage");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//
		contentPane.add(createReadMessagesButton());
		contentPane.add(createVerticalPaddingComponent());
		contentPane.add(createWriteMessageButton());
		contentPane.add(createVerticalPaddingComponent());
		contentPane.add(createAssignHousingButton());
		contentPane.add(createVerticalPaddingComponent());
		contentPane.add(createRoomInfoButton());
		//
		pack();
	}
	//
	private JButton createReadMessagesButton(){
		int messageCount;
		try{
			Vector<MessageID> messages = new User(accountData.getThisUserID(), residenceDataGateway, userDataGateway).getUnreadReceivedMessages();
			messageCount = messages.size();
		}catch(Exception e1){
			messageCount = -1;
		}
		//
		JButton readMessageButton = new JButton("Read Messages: " + messageCount + " Outstanding");
		setDefaultMenuButtonProperties(readMessageButton);
		//
		readMessageButton.setBackground(Color.red);
		UIManager.put("Button.disabledText", Color.BLACK);
		if(messageCount <= 0){
			readMessageButton.setBackground(Color.green);
			readMessageButton.setEnabled(false);
		}
		//
		readMessageButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				String messageContents = "";
				try{
					Vector<MessageID> messages = new User(accountData.getThisUserID(), residenceDataGateway, userDataGateway).getUnreadReceivedMessages();
					Message message = new Message(messages.get(0), residenceDataGateway, userDataGateway);
					messageContents = message.getContents();
					message.setReadStatus(true);
				}catch(Exception e1){
					e1.printStackTrace();
					messageContents = "Error: Couldn't get message.";
				}
				setVisible(false);
				JOptionPane.showMessageDialog(null, messageContents);
				ManagerGUI managerGUI = new ManagerGUI(residenceDataGateway, userDataGateway, accountData);
				managerGUI.setVisible(true);
			}
			
		});
		//
		return readMessageButton;
	}
	//
	private JButton createWriteMessageButton(){
		JButton writeMessageButton = new JButton("Send Housing Wide Message");
		setDefaultMenuButtonProperties(writeMessageButton);
		//
		writeMessageButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				ManagerSendMessageGUI sendMessageGUI = new ManagerSendMessageGUI(residenceDataGateway, userDataGateway, accountData);
				sendMessageGUI.setVisible(true);
				setVisible(false);
			}
			
		});
		//
		return writeMessageButton;
	}
	//
	private JButton createAssignHousingButton(){
		String numOfApplications;
		//
		try{
			numOfApplications = ""+residenceDataGateway.getNumberOfApplications();
		}catch(Exception e1){
			numOfApplications = "Error";
		}
		//
		JButton assignHousingButton = new JButton("Assign Housing: " + numOfApplications + " Unassigned");
		setDefaultMenuButtonProperties(assignHousingButton);
		//
		assignHousingButton.setBackground(Color.red);
		//
		try{
			if(residenceDataGateway.getNumberOfApplications() <= 0){
				assignHousingButton.setBackground(Color.green);
			}
		}catch(Exception e1){
			
		}
		//
		assignHousingButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				ManagerAssignRoomGUI assignRoomGUI = new ManagerAssignRoomGUI(residenceDataGateway, userDataGateway, accountData);
				assignRoomGUI.setVisible(true);
				setVisible(false);
			}
			
		});
		//
		return assignHousingButton;
	}
	private JButton createRoomInfoButton(){
		JButton roomInfoButton = new JButton("Veiw Room Information");
		setDefaultMenuButtonProperties(roomInfoButton);
		//
		roomInfoButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
	                	
				RoomInfoGUI roomInfoGUI = new RoomInfoGUI(residenceDataGateway, userDataGateway, accountData);
	            roomInfoGUI.setVisible(true);
	            setVisible(false);
			}
			
		});
		//
		return roomInfoButton;
	}
}
