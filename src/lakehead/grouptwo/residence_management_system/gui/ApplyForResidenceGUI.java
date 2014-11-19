package lakehead.grouptwo.residence_management_system.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import lakehead.grouptwo.residence_management_system.data.AuthenticationException;
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.implemented_gateways.client_sql.ClientSQLAccountData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ConnectionToServer;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ServerAccountData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ServerResidenceData;
import lakehead.grouptwo.residence_management_system.implemented_gateways.server.ServerUserData;

public class ApplyForResidenceGUI extends JFrame{
	private static final long serialVersionUID = 4553205297481048092L;
	//
	String sex = "";
	int year = 0;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	//private IAccountData accountData;
	private ConnectionToServer connectionToServer;
	//
	public ApplyForResidenceGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, ConnectionToServer _connectionToServer){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		//accountData = _accountData;
		connectionToServer = _connectionToServer;
		//
		setTitle("Application for Housing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(360, 450);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);
		
		JLabel usernameLabel = new JLabel("Lakehead Username:");
		usernameLabel.setBounds(10, 10, 160, 25);
		panel.add(usernameLabel);
		
		final JTextField usernameText = new JTextField(20);
		usernameText.setBounds(170, 10, 160, 25);
		panel.add(usernameText);

		JLabel passwordLabel = new JLabel("Lakehead Password:");
		passwordLabel.setBounds(10, 40, 160, 25);
		panel.add(passwordLabel);
		
		final JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(170, 40, 160, 25);
		panel.add(passwordText);
		/*
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setBounds(10, 10, 80, 25);
		panel.add(firstNameLabel);
		
		final JTextField firstNameText = new JTextField(20);
		firstNameText.setBounds(170, 10, 160, 25);
		panel.add(firstNameText);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setBounds(10, 40, 80, 25);
		panel.add(lastNameLabel);
		
		final JTextField lastNameText = new JTextField(20);
		lastNameText.setBounds(170, 40, 160, 25);
		panel.add(lastNameText);
		
		JLabel address1Label = new JLabel("Address Line 1:");
		address1Label.setBounds(10, 70, 100, 25);
		panel.add(address1Label);
		
		final JTextField address1Text = new JTextField(20);
		address1Text.setBounds(170, 70, 160, 25);
		panel.add(address1Text);
		
		JLabel address2Label = new JLabel("Address Line 2:");
		address2Label.setBounds(10, 100, 100, 25);
		panel.add(address2Label);
		
		final JTextField address2Text = new JPasswordField(20);
		address2Text.setBounds(170, 100, 160, 25);
		panel.add(address2Text);
		
		JLabel cityLabel = new JLabel("City:");
		cityLabel.setBounds(10, 130, 80, 25);
		panel.add(cityLabel);
		
		final JTextField cityText = new JTextField(20);
		cityText.setBounds(170, 130, 160, 25);
		panel.add(cityText);
		
		JLabel postalCodeLabel = new JLabel("Postal Code:");
		postalCodeLabel.setBounds(10, 160, 80, 25);
		panel.add(postalCodeLabel);
		
		final JTextField postalCodeText = new JTextField(20);
		postalCodeText.setBounds(170, 160, 160, 25);
		panel.add(postalCodeText);
		
		JLabel countryLabel = new JLabel("Country:");
		countryLabel.setBounds(10, 190, 80, 25);
		panel.add(countryLabel);
		
		final JTextField countryText = new JTextField(20);
		countryText.setBounds(170, 190, 160, 25);
		panel.add(countryText);
		*/
		
		JLabel yearLabel = new JLabel("Academic Year:");
		yearLabel.setBounds(10, 70, 100, 25);
		panel.add(yearLabel);
		
		JRadioButton yearButton1 = new JRadioButton("1");
		yearButton1.setBounds(170, 70, 40, 25);
		panel.add(yearButton1);
		
		yearButton1.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				year = 1;
				//System.out.println(year); // working check
			}
		});
		
		JRadioButton yearButton2 = new JRadioButton("2");
		yearButton2.setBounds(210, 70, 40, 25);
		panel.add(yearButton2);
		
		yearButton2.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				year = 2;
				//System.out.println(year); // working check
			}
		});
		
		JRadioButton yearButton3 = new JRadioButton("3");
		yearButton3.setBounds(250, 70, 40, 25);
		panel.add(yearButton3);
		
		yearButton3.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				year = 3;
				//System.out.println(year); // working check
			}
		});
		
		JRadioButton yearButton4 = new JRadioButton("4+");
		yearButton4.setBounds(290, 70, 40, 25);
		panel.add(yearButton4);
		
		yearButton4.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				year = 4;
				//System.out.println(year); // working check
			}
		});
		
		final ButtonGroup radioGroupYear = new ButtonGroup();
		radioGroupYear.add(yearButton1);
		radioGroupYear.add(yearButton2);
		radioGroupYear.add(yearButton3);
		radioGroupYear.add(yearButton4);
		
		/*
		JLabel sexLabel = new JLabel("Sex:");
		sexLabel.setBounds(10, 250, 100, 25);
		panel.add(sexLabel);
		
		JRadioButton sexBox = new JRadioButton("M");
		sexBox.setBounds(170, 250, 40, 25);
		panel.add(sexBox);
		
		sexBox.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				sex = "Male";
				System.out.println(sex); // working check
			}
		});
		
		JRadioButton sexBox2 = new JRadioButton("F");
		sexBox2.setBounds(210, 250, 40, 25);
		panel.add(sexBox2);
		
		sexBox2.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				sex = "Female";
				System.out.println(sex); // working check
			}
		});
		
		ButtonGroup radioGroupSex = new ButtonGroup();
		radioGroupSex.add(sexBox);
		radioGroupSex.add(sexBox2);
		
		*/
		
		JLabel requestLabel = new JLabel("Special Needs/Requests:");
		requestLabel.setBounds(10, 280, 150, 25);
		panel.add(requestLabel);
		
		final JTextArea requestArea = new JTextArea(20, 20);
		requestArea.setBounds(170, 280, 160, 75);
		panel.add(requestArea);
		
		JButton registerButton = new JButton("Submit Application");
		registerButton.setBounds(100, 370, 150, 25);
		panel.add(registerButton);
		
		registerButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent action){
				
				ServerAccountData accountData = null;
				try{
					accountData = new ServerAccountData(connectionToServer, usernameText.getText(), passwordText.getPassword());
				}catch(AuthenticationException ae){
					JOptionPane.showMessageDialog(null, ae.getMessage());
				}
				//
				if(accountData != null){
					
					int year = 0;
					
					for (Enumeration<AbstractButton> buttons = radioGroupYear.getElements(); buttons.hasMoreElements();) {
			            AbstractButton button = buttons.nextElement();

			            if (button.isSelected()) {
			            	year = Integer.parseInt(button.getText());
			            }
			        }
					
					if(year != 0){
					
						try{
							((ServerResidenceData)residenceDataGateway).setAccountData(accountData);
							((ServerUserData)userDataGateway).setAccountData(accountData);
							// need to add measures to prevent the user from registering twice or if they already have a room
							residenceDataGateway.applyForResidence(accountData.getThisUserID(), year, requestArea.getText());
							setVisible(false);
							new StartupGUI(residenceDataGateway, userDataGateway, connectionToServer).setVisible(true);
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							((ServerResidenceData)residenceDataGateway).setAccountData(null);
						}
					
					}else{

						JOptionPane.showMessageDialog(null, "Please select a year level.");
					}
					
				}
				/*
				String fname = firstNameText.getText();
				String lname = lastNameText.getText();
				String address1 = address1Text.getText();
				String address2 = address2Text.getText();
				String city = cityText.getText();
				String postalcode = postalCodeText.getText();
				String country = countryText.getText();
				
				System.out.println(fname + lname + address1 + address2 + city + postalcode + country + year + sex);
				*/
				//setVisible(false);
			}
		});
		
	}
	
}