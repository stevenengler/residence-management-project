package lakehead.grouptwo.residence_management_system.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.Message;
import lakehead.grouptwo.residence_management_system.data.User;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.MessageID;

class ResidentGUI extends VerticallyStackedMenu{
	private static final long serialVersionUID = -8414854513911850688L;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	private IAccountData accountData;
	private User currentUser;
	//
	public ResidentGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, IAccountData _accountData){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		accountData = _accountData;
		//
		currentUser = new User(accountData.getThisUserID(), residenceDataGateway, userDataGateway);
		//
		setTitle("Resident Home");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//
		JPanel labelsPanel = createLeftAlignedJPanel();
		JLabel userLabel = new JLabel("Personal Information");
		//
		labelsPanel.add(userLabel);
		//
		contentPane.add(labelsPanel);
		contentPane.add(createVerticalPaddingComponent());
		contentPane.add(createUserNameLabel());
		contentPane.add(createRoomNumberLabel());
		//
		contentPane.add(createVerticalPaddingComponent());
		contentPane.add(createReadMessagesButton());
		contentPane.add(createVerticalPaddingComponent());
		contentPane.add(sendMessageToHousingButton());
		//
		pack();
	}
	//
	private JPanel createUserNameLabel(){
		JPanel labelsPanel = createLeftAlignedJPanel();
		//
		JLabel userNameLabel;
		//
		try{
			userNameLabel = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName());
		}catch(Exception e1){
			userNameLabel = new JLabel("Error: Couldn't get first name.");
		}
		//
		labelsPanel.add(userNameLabel);
		//
		return labelsPanel;
	}
	//
	private JPanel createRoomNumberLabel(){
		JPanel labelsPanel = createLeftAlignedJPanel();
		//
		String roomMessage;
		try{
			if(currentUser.getResidenceRoom() == null){
				roomMessage = "(have not yet been assigned a room)";
			}else{
				roomMessage = currentUser.getResidenceRoom().id.id + " (building " + currentUser.getResidenceRoom().getContainingBuilding().id.id + ")";
			}
		}catch(Exception e){
			roomMessage = "Error";
		}
		//
		JLabel userRoomNumberLabel = new JLabel("Currently in Room #: "+roomMessage);
		//
		labelsPanel.add(userRoomNumberLabel);
		//
		return labelsPanel;
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
		JButton messageButton = new JButton("Read Messages: " + messageCount + " Outstanding");
		//
		setDefaultMenuButtonProperties(messageButton);
		//
		messageButton.setBackground(Color.red);
		UIManager.put("Button.disabledText", Color.BLACK);
		if(messageCount <= 0){
			messageButton.setBackground(Color.green);
			messageButton.setEnabled(false);
		}
		
		messageButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				String messageContents = "";
				try{
					Vector<MessageID> messages = new User(accountData.getThisUserID(), residenceDataGateway, userDataGateway).getUnreadReceivedMessages();
					Message message = new Message(messages.get(0), residenceDataGateway, userDataGateway);
					messageContents zzz= message.getContents();
					message.setReadStatus(true);
				}catch(Exception e1){
					messageContents = "Error: Couldn't get message.";
				}
				setVisible(false);
				JOptionPane.showMessageDialog(null, messageContents);
				ResidentGUI residentGUI = new ResidentGUI(residenceDataGateway, userDataGateway, accountData);
				residentGUI.setVisible(true);
			}
			
		});
		//
		return messageButton;
	}
	//
	private JButton sendMessageToHousingButton(){
		JButton submitButton = new JButton("Submit Message to Housing");
		//
		setDefaultMenuButtonProperties(submitButton);
		//
		submitButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				StudentSendMessageGUI studentSendMessageGUI = new StudentSendMessageGUI(residenceDataGateway, userDataGateway, accountData);
				studentSendMessageGUI.setVisible(true);
				setVisible(false);
			}
			
		});
		//
		return submitButton;
	}
}