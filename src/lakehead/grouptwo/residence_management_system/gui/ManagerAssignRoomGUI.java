package lakehead.grouptwo.residence_management_system.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import lakehead.grouptwo.residence_management_system.data.Building;
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.Room;
import lakehead.grouptwo.residence_management_system.data.User;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.ApplicationID;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;

public class ManagerAssignRoomGUI extends VerticallyStackedMenu{
	private static final long serialVersionUID = -648356484642126718L;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	private IAccountData accountData;
	//
	private JComboBox<String> buildingComboBox = new JComboBox<String>();
	private JComboBox<String> roomComboBox = new JComboBox<String>();
	private JTextArea studentInfoTextArea = new JTextArea();
	private JButton assignStudentBtn = new JButton("Assign Student To This Room");
	private int lastSelectedBuilding = 0;
	//
	private Vector<String> buildingNames = new Vector<String>();
	private Vector<BuildingID> buildingIDs = null;
	//
	//private User currentlyAssigningUser;
	private ApplicationID currentApplication;
	private Room roomSelected;
	//
	public ManagerAssignRoomGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, IAccountData _accountData){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		accountData = _accountData;
		//
		setTitle("Assign Rooms");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//
		try{
			buildingIDs = residenceDataGateway.getAllBuildings();
		}catch(Exception e1){
			e1.printStackTrace();
		}
		//
		buildingNames.add("(Building)");
		//
		for(int i=0; i<buildingIDs.size(); i++){
			try{
				buildingNames.add(new Building(buildingIDs.get(i), residenceDataGateway, userDataGateway).getName());
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		//
		buildingComboBox.addItemListener(new ItemListener(){
			public void itemStateChanged (ItemEvent event){
				if(event.getStateChange() == ItemEvent.SELECTED){
					updateAllItems();
				}
			}
		});
		//
		roomComboBox.addItemListener(new ItemListener(){
			public void itemStateChanged (ItemEvent event){
				if(event.getStateChange() == ItemEvent.SELECTED){
					updateAllItems();
				}
			}
		});
		//
		studentInfoTextArea.setMaximumSize(new Dimension(350, 250));
		studentInfoTextArea.setPreferredSize(new Dimension(350, 250));
		//
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(buildingNames);
		buildingComboBox.setModel(model);
		//
		JPanel buildingLabelAndComboBoxPanel = createLeftAlignedJPanel();
		JLabel buildingLabel = new JLabel("Building");
		//
		buildingLabelAndComboBoxPanel.add(buildingLabel);
		buildingLabelAndComboBoxPanel.add(buildingComboBox);
		//
		JPanel roomLabelAndComboBoxPanel = createLeftAlignedJPanel();
		JLabel roomLabel = new JLabel("Room");
		//
		roomLabelAndComboBoxPanel.add(roomLabel);
		roomLabelAndComboBoxPanel.add(roomComboBox);
		//
		assignStudentBtn.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				try{
					assignUserToRoom(new User(residenceDataGateway.getApplicationUser(currentApplication), residenceDataGateway, userDataGateway), roomSelected);
				}catch(Exception e1){
					e1.printStackTrace();
				}
				updateUserInfo();
				updateAllItems();
			}
			
		});
		
		//
		contentPane.add(buildingLabelAndComboBoxPanel);
		contentPane.add(roomLabelAndComboBoxPanel);
		contentPane.add(studentInfoTextArea);
		contentPane.add(assignStudentBtn);
		//
		pack();
		//
		updateUserInfo();
		updateAllItems();
	}
	//
	private void assignUserToRoom(User user, Room room){
		boolean canAssignToRoom = true;
		if(room == null){
			JOptionPane.showMessageDialog(null, "Please select a room first.");
			return;
		}
		if(canAssignToRoom){
			try{
				if(room.getOccupyingUsers().size() >= room.getCapacity()){
					canAssignToRoom = false;
				}
			}catch(Exception e){
				e.printStackTrace();
				canAssignToRoom = false;
				JOptionPane.showMessageDialog(null, "There was an error while trying to assign the user to the room.");
			}
		}
		if(canAssignToRoom){
			try{
				residenceDataGateway.setUserRoom(user.id, room.id);
				residenceDataGateway.removeApplication(currentApplication);
			}catch(Exception e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was an error while trying to assign the user to the room.");
			}
		}else{
			JOptionPane.showMessageDialog(null, "Sorry, that room seems to have filled up.");
		}
	}
	//
	private void updateUserInfo(){
		try{
			if(residenceDataGateway.getNumberOfApplications() == 0){
				studentInfoTextArea.setText("All users have been assigned rooms.");
				assignStudentBtn.setEnabled(false);
				assignStudentBtn.setBackground(Color.GRAY);
			}else{
				currentApplication = residenceDataGateway.getResidenceApplications(1).get(0);
				String userInfo = "";
				//
				User currentlyAssigningUser = new User(residenceDataGateway.getApplicationUser(currentApplication), residenceDataGateway, userDataGateway);
				//
				currentlyAssigningUser = new User(residenceDataGateway.getApplicationUser(currentApplication), residenceDataGateway, userDataGateway);
				userInfo += "Name: " + currentlyAssigningUser.getFirstName() + " " + currentlyAssigningUser.getLastName() + "\n";
				userInfo += "Year Level: " + residenceDataGateway.getApplicationYearLevel(currentApplication) + "\n";
				userInfo += "Special Requests: " + residenceDataGateway.getApplicationSpecialRequests(currentApplication);
				studentInfoTextArea.setText(userInfo);
				assignStudentBtn.setEnabled(true);
				assignStudentBtn.setBackground(Color.BLACK);
			}
		}catch(Exception e){
			studentInfoTextArea.setText("Error getting application information.");
			e.printStackTrace();
		}
	}
	//
	private void updateAllItems(){
		if(buildingComboBox.getSelectedIndex() == 0){
			roomComboBox.setEnabled(false);
			//
			Vector<String> defaultOption = new Vector<String>();
			defaultOption.add("(Room)");
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(defaultOption);
			if(lastSelectedBuilding != buildingComboBox.getSelectedIndex()){
				roomComboBox.setModel(model);
				lastSelectedBuilding = buildingComboBox.getSelectedIndex();
			}
			//
			roomSelected = null;
		}else{
			roomComboBox.setEnabled(true);
			//
			Building buildingSelected = getSelectedBuilding();
			//
			Vector<Room> rooms = null;
			try{
				rooms = buildingSelected.getAllAvailableRooms();
			}catch(Exception e1){
				e1.printStackTrace();
			}
			//
			Vector<String> roomNumbers = new Vector<String>();
			//
			for(int i=0; i<rooms.size(); i++){
				try{
					roomNumbers.add(""+rooms.get(i).id.id+" ("+rooms.get(i).getOccupyingUsers().size()+"/"+rooms.get(i).getCapacity()+")");
				}catch(Exception e1){
					e1.printStackTrace();
				}
			}
			//
			if(lastSelectedBuilding != buildingComboBox.getSelectedIndex()){
				DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(roomNumbers);
				roomComboBox.setModel(model);
				lastSelectedBuilding = buildingComboBox.getSelectedIndex();
			}
			//
			roomSelected = rooms.get(roomComboBox.getSelectedIndex());
		}
	}
	//
	private Building getSelectedBuilding(){
		return new Building(buildingIDs.get(buildingComboBox.getSelectedIndex()-1), residenceDataGateway, userDataGateway);
	}
}