package lakehead.grouptwo.residence_management_system.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;

class StartupGUI extends JFrame{
	private static final long serialVersionUID = -8448733317069922553L;
	//
	private JPanel contentPane;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	//private IAccountData accountData;
	private Connection dbConnection;
	//
	public StartupGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, Connection _dbConnection){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		//accountData = _accountData;
		dbConnection = _dbConnection;
		//
		setTitle("LU Housing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 209, 285);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JLabel welcomeLabel = new JLabel("Welcome to LU Housing");
		welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		welcomeLabel.setBounds(23, 11, 160, 25);
		contentPane.add(welcomeLabel);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(23, 58, 147, 47);
		contentPane.add(loginButton);
		
		loginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				LoginGUI LOG = new LoginGUI(residenceDataGateway, userDataGateway, dbConnection);
				LOG.setVisible(true);
				setVisible(false);
			}
		});
		
		JButton applyButton = new JButton("Apply for Housing");
		applyButton.setBounds(23, 174, 147, 47);
		contentPane.add(applyButton);
		
		applyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ApplyForResidenceGUI APP = new ApplyForResidenceGUI(residenceDataGateway, userDataGateway, dbConnection);
				APP.setVisible(true);
				setVisible(false);
			}
		});
		
		JButton housingInfoButton = new JButton("Housing Info.");
		housingInfoButton.setBounds(23, 116, 147, 47);
		contentPane.add(housingInfoButton);
		
		housingInfoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				HousingInformationGUI HINFO = new HousingInformationGUI(residenceDataGateway, userDataGateway);
				HINFO.setVisible(true);
				setVisible(false);
			}
		});
		
	}
}