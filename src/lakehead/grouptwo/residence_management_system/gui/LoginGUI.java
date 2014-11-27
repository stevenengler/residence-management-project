package lakehead.grouptwo.residence_management_system.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.User;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ConnectionToServer;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ServerAccountData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ServerResidenceData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ServerUserData;

class LoginGUI extends JFrame{
	private static final long serialVersionUID = 1L;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	//private IAccountData accountData;
	private ConnectionToServer connectionToServer;
	//
	public LoginGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, ConnectionToServer _connectionToServer){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		//accountData = _accountData;
		connectionToServer = _connectionToServer;
		
		setTitle("Login ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 160);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);
		
		JLabel userLabel = new JLabel("User:");
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);
		
		final JTextField userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		panel.add(userText);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(10, 40, 80, 25);
		panel.add(passwordLabel);
		
		final JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 160, 25);
		panel.add(passwordText);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(100, 80, 100, 25);
		panel.add(loginButton);
		
		loginButton.addActionListener(new ActionListener(){
			
			String name;
			String password;
			
			public void actionPerformed(ActionEvent e){
				//name = userText.getText();
				//password = new String(passwordText.getPassword());
				//System.out.println(name + ' ' + password); // working check
				//
				ServerAccountData accountData = null;
				try{
					accountData = new ServerAccountData(connectionToServer, userText.getText(), passwordText.getPassword());
				}catch(AuthenticationException ae){
					JOptionPane.showMessageDialog(null, ae.getMessage());
				}
				//
				if(accountData != null){
					((ServerResidenceData)residenceDataGateway).setAccountData(accountData);
					((ServerUserData)userDataGateway).setAccountData(accountData);
					try{
						if(new User(accountData.getThisUserID(), residenceDataGateway, userDataGateway).getPermissions() == 1){
							ResidentGUI Res = new ResidentGUI(residenceDataGateway, userDataGateway, accountData);
							Res.setVisible(true);
							setVisible(false);
						}else{
							ManagerGUI Man = new ManagerGUI(residenceDataGateway, userDataGateway, accountData);
							Man.setVisible(true);
							setVisible(false);
						}
					}catch(Exception e1){
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Logged in, but there was an error getting account data.");
					}
				}
				//
				/*
				if(name.equals("manager")){
					
					ManagerGUI Man = new ManagerGUI(residenceDataGateway, userDataGateway, accountData);
					Man.setVisible(true);
					setVisible(false);
					System.out.println("Manager"); // working check
				}
				
				if(name.equals("student")){
					
					ResidentGUI Res = new ResidentGUI(residenceDataGateway, userDataGateway, accountData);
					Res.setVisible(true);
					setVisible(false);
					System.out.println("Student"); // working check
				}
				*/
			}
		});
	}
	
}