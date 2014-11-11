package lakehead.grouptwo.residence_management_system.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
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

public class ManagerGUI extends JFrame{
	private static final long serialVersionUID = -5363908732688095195L;
	//
	private JPanel contentPane;
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 406, 226);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		//
		int messageCount;
		try{
			Vector<MessageID> messages = new User(accountData.getThisUserID(), residenceDataGateway, userDataGateway).getUnreadReceivedMessages();
			messageCount = messages.size();
		}catch(Exception e1){
			messageCount = -1;
		}
		//
		final JButton readMessageButton = new JButton("Read Messages: " + messageCount + " Outstanding");
		readMessageButton.setBounds(10, 21, 370, 43);
		readMessageButton.setBackground(Color.red);
		UIManager.put("Button.disabledText", Color.BLACK);
		if(messageCount <= 0){
			readMessageButton.setBackground(Color.green);
			readMessageButton.setEnabled(false);
		}
		
		readMessageButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				/*
				messageCount = messageCount - 1;
				readMessageButton.setText("Read Messages: " + messageCount + " Outstanding");
				if(messageCount <= 0){
					readMessageButton.setBackground(Color.green);
				}
				*/
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
				ManagerGUI MAN = new ManagerGUI(residenceDataGateway, userDataGateway, accountData);
				MAN.setVisible(true);
			}
		});
		contentPane.add(readMessageButton);
		
		JButton writeMessageButton = new JButton("Send Housing Wide Message");
		writeMessageButton.setBounds(10, 75, 370, 43);
		writeMessageButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				ManagerSendMessageGUI MANMES = new ManagerSendMessageGUI(residenceDataGateway, userDataGateway, accountData);
				MANMES.setVisible(true);
				setVisible(false);
			}
		});
		contentPane.add(writeMessageButton);
		
		String numOfApplications;
		
		try{
			numOfApplications = ""+residenceDataGateway.getNumberOfApplications();
		}catch(Exception e1){
			numOfApplications = "Error";
		}
		JButton assignHousingButton = new JButton("Assign Housing: " + numOfApplications + " Unassigned");
		assignHousingButton.setBounds(10, 129, 370, 43);
		assignHousingButton.setBackground(Color.red);
		try{
			if(residenceDataGateway.getNumberOfApplications() <= 0){
				assignHousingButton.setBackground(Color.green);
			}
		}catch(Exception e1){
			
		}
		
		assignHousingButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				ManagerAssignRoomGUI MANASS = new ManagerAssignRoomGUI(residenceDataGateway, userDataGateway, accountData);
				MANASS.setVisible(true);
				setVisible(false);
				
				/*
				assignCount = assignCount - 1;
				assignHousingButton.setText("Assign Housing: " + assignCount + " Unassigned");
				if(assignCount <= 0){
					assignHousingButton.setBackground(Color.green);
				}
				*/
			}
		});
		contentPane.add(assignHousingButton);
	}
}
