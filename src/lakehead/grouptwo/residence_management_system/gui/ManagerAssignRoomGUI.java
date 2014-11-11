package lakehead.grouptwo.residence_management_system.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.User;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.ApplicationID;
import lakehead.grouptwo.residence_management_system.data.identifiers.UserID;

public class ManagerAssignRoomGUI extends JFrame{
	private static final long serialVersionUID = -648356484642126718L;
	//
	private JPanel contentPane;
	JTextArea txtrStuddentInfoGoes = new JTextArea();
	//
	UserID currUser = null;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	private IAccountData accountData;
	//
	public ManagerAssignRoomGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, IAccountData _accountData){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		accountData = _accountData;
		//
		setTitle("Assign Rooms");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 395, 355);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JButton btnNewButton = new JButton("House 1");
		btnNewButton.setBounds(10, 11, 103, 23);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				
				
			}
		});
		
		JButton btnNewButton_1 = new JButton("House 2");
		btnNewButton_1.setBounds(134, 11, 103, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("House 3");
		btnNewButton_2.setBounds(266, 11, 103, 23);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("House 4");
		btnNewButton_3.setBounds(10, 45, 103, 23);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("House 5");
		btnNewButton_4.setBounds(134, 45, 103, 23);
		contentPane.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("House 6");
		btnNewButton_5.setBounds(266, 45, 103, 23);
		contentPane.add(btnNewButton_5);
		
		txtrStuddentInfoGoes.setLineWrap(true);
		txtrStuddentInfoGoes.setText("Student info goes here.");
		txtrStuddentInfoGoes.setBounds(10, 79, 359, 192);
		contentPane.add(txtrStuddentInfoGoes);
		
		JButton btnAssignNextStudent = new JButton("Assign Next Student");
		btnAssignNextStudent.setBounds(10, 282, 174, 23);
		contentPane.add(btnAssignNextStudent);
		
		JButton exitButton = new JButton("Exit");
		exitButton.setBounds(194, 282, 174, 23);
		
		exitButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				ManagerGUI MANST = new ManagerGUI(residenceDataGateway, userDataGateway, accountData);
				MANST.setVisible(true);
				setVisible(false);
			}
		});
		
		contentPane.add(exitButton);
		
		updateUserInfo();
		
	}
	private void updateUserInfo(){
		try{
			ApplicationID application = residenceDataGateway.getResidenceApplications(1).get(0);
			String userInfo = "";
			User appUser = new User(residenceDataGateway.getApplicationUser(application), residenceDataGateway, userDataGateway);
			userInfo += "Name: " + appUser.getFirstName() + " " + appUser.getLastName() + "\n";
			userInfo += "Year Level: " + residenceDataGateway.getApplicationYearLevel(application) + "\n";
			userInfo += "Special Requests: " + residenceDataGateway.getApplicationSpecialRequests(application);
			txtrStuddentInfoGoes.setText(userInfo);
		}catch(Exception e){
			txtrStuddentInfoGoes.setText("Error getting application information.");
			e.printStackTrace();
		}
	}
}