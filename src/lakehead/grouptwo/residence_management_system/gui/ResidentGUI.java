package lakehead.grouptwo.residence_management_system.gui;

import java.awt.Color;
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

class ResidentGUI extends JFrame{
	private static final long serialVersionUID = -8414854513911850688L;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	private IAccountData accountData;
	//
	public ResidentGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, IAccountData _accountData){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		accountData = _accountData;
		//
		setTitle("Resident Infromation ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(522, 257);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);
		
		JLabel userLabel = new JLabel("Personal Information");
		userLabel.setBounds(185, 5, 180, 25);
		panel.add(userLabel);
		
		User currentUser = new User(accountData.getThisUserID(), residenceDataGateway, userDataGateway);
		JLabel userLabelFirstName;
		try{
			userLabelFirstName = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName());
		}catch(Exception e1){
			userLabelFirstName = new JLabel("Error: Couldn't get first name.");
		}
		userLabelFirstName.setBounds(10, 30, 180, 25);
		panel.add(userLabelFirstName);
		
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
		JLabel userLabelLastName = new JLabel("Currently in Room #: "+roomMessage);
		userLabelLastName.setBounds(10, 50, 580, 25);
		panel.add(userLabelLastName);
		
		/*
		JLabel userLabelLastName = new JLabel("Student Street Address");
		userLabelLastName.setBounds(10, 50, 180, 25);
		panel.add(userLabelLastName);
		
		JLabel userLabelCity = new JLabel("Student City");
		userLabelCity.setBounds(10, 70, 180, 25);
		panel.add(userLabelCity);
		
		JLabel userLabelProv = new JLabel("Student Province");
		userLabelProv.setBounds(10, 90, 180, 25);
		panel.add(userLabelProv);
		
		JLabel userLabelPostalCode = new JLabel("Student Postal Code");
		userLabelPostalCode.setBounds(10, 110, 180, 25);
		panel.add(userLabelPostalCode);
		
		JLabel userLabelContry = new JLabel("Student Country");
		userLabelContry.setBounds(10, 130, 180, 25);
		panel.add(userLabelContry);
		*/
		//
		int messageCount;
		try{
			Vector<MessageID> messages = new User(accountData.getThisUserID(), residenceDataGateway, userDataGateway).getUnreadReceivedMessages();
			messageCount = messages.size();
		}catch(Exception e1){
			messageCount = -1;
		}
		//
		final JButton messageButton = new JButton("Read Messages: " + messageCount + " Outstanding");
		messageButton.setBounds(10, 170, 230, 35);
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
					messageContents = message.getContents();
					message.setReadStatus(true);
				}catch(Exception e1){
					messageContents = "Error: Couldn't get message.";
				}
				setVisible(false);
				JOptionPane.showMessageDialog(null, messageContents);
				ResidentGUI Res = new ResidentGUI(residenceDataGateway, userDataGateway, accountData);
				Res.setVisible(true);
			}
		});
		panel.add(messageButton);
		
		JButton submitButton = new JButton("Submit Message to Housing");
		submitButton.setBounds(255, 170, 240, 35);
		submitButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				StudentSendMessageGUI STDMES = new StudentSendMessageGUI(residenceDataGateway, userDataGateway, accountData);
				STDMES.setVisible(true);
				setVisible(false);
			}
		});
		
		panel.add(submitButton);
	}
}