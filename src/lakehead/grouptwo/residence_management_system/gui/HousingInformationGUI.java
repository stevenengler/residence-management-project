package lakehead.grouptwo.residence_management_system.gui;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import lakehead.grouptwo.residence_management_system.data.Building;
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;

public class HousingInformationGUI extends JFrame{
	private static final long serialVersionUID = 281025075560627287L;
	//
	private JPanel contentPane;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	//private IAccountData accountData;
	
	//
	public HousingInformationGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		//accountData = _accountData;
		//
setTitle("Housing Information");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 349);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JLabel firstYearLabel = new JLabel("1st year housing rooms avalible:");
		firstYearLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		firstYearLabel.setBounds(10, 28, 232, 25);
		contentPane.add(firstYearLabel);
		try{
			Building building1 = new Building(new BuildingID(1), residenceDataGateway, userDataGateway);
			JLabel house1Label = new JLabel(building1.getName() + ": " + building1.getNumberOfAvailableRooms()+"/"+building1.getNumberOfRooms()+" rooms available");
			house1Label.setBounds(10, 64, 383, 19);
			contentPane.add(house1Label);
	
			Building building2 = new Building(new BuildingID(2), residenceDataGateway, userDataGateway);
			JLabel house2Label = new JLabel(building2.getName() + ": " + building2.getNumberOfAvailableRooms()+"/"+building2.getNumberOfRooms()+" rooms available");
			house2Label.setBounds(10, 94, 383, 19);
			contentPane.add(house2Label);
			
			JLabel restYearLabel = new JLabel("2nd, 3rd and 4th year housing rooms avalible:");
			restYearLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
			restYearLabel.setBounds(10, 137, 379, 25);
			contentPane.add(restYearLabel);
	
			Building building3 = new Building(new BuildingID(3), residenceDataGateway, userDataGateway);
			JLabel house3Label = new JLabel(building3.getName() + ": " + building3.getNumberOfAvailableRooms()+"/"+building3.getNumberOfRooms()+" rooms available");
			house3Label.setBounds(10, 173, 383, 19);
			contentPane.add(house3Label);
	
			Building building4 = new Building(new BuildingID(4), residenceDataGateway, userDataGateway);
			JLabel house4Lable = new JLabel(building4.getName() + ": " + building4.getNumberOfAvailableRooms()+"/"+building4.getNumberOfRooms()+" rooms available");
			house4Lable.setBounds(10, 203, 383, 19);
			contentPane.add(house4Lable);
	
			Building building5 = new Building(new BuildingID(5), residenceDataGateway, userDataGateway);
			JLabel house5Lable = new JLabel(building5.getName() + ": " + building5.getNumberOfAvailableRooms()+"/"+building5.getNumberOfRooms()+" rooms available");
			house5Lable.setBounds(10, 233, 383, 19);
			contentPane.add(house5Lable);
	
			Building building6 = new Building(new BuildingID(6), residenceDataGateway, userDataGateway);
			JLabel house6Lable = new JLabel(building6.getName() + ": " + building6.getNumberOfAvailableRooms()+"/"+building6.getNumberOfRooms()+" rooms available");
			house6Lable.setBounds(10, 263, 383, 19);
			contentPane.add(house6Lable);
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: Couldn't get the available rooms.");
		}
	}
}
