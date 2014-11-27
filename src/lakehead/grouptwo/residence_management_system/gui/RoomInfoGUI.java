package lakehead.grouptwo.residence_management_system.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import lakehead.grouptwo.residence_management_system.data.Building;
import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.Room;
import lakehead.grouptwo.residence_management_system.data.User;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;
import lakehead.grouptwo.residence_management_system.data.identifiers.BuildingID;
import lakehead.grouptwo.residence_management_system.data.identifiers.RoomID;

public class RoomInfoGUI extends VerticallyStackedMenu{
	//
	private static final long serialVersionUID = -2232052972220255713L;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	private IAccountData accountData;
	//
	private JComboBox<String> buildingComboBox = new JComboBox<String>();
	private JComboBox<String> roomComboBox = new JComboBox<String>();
	private JTextArea roomInfoTextArea = new JTextArea();
	private int lastSelectedBuilding = 0;
	//
	private Vector<String> buildingNames = new Vector<String>();
	private Vector<BuildingID> buildingIDs = null;
	//
	public RoomInfoGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, IAccountData _accountData){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		accountData = _accountData;
		//
		setTitle("Room Information");
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
		roomInfoTextArea.setMaximumSize(new Dimension(350, 250));
		roomInfoTextArea.setPreferredSize(new Dimension(350, 250));
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
		contentPane.add(buildingLabelAndComboBoxPanel);
		contentPane.add(roomLabelAndComboBoxPanel);
		contentPane.add(roomInfoTextArea);
		//
		pack();
		//
		updateAllItems();
	}
	//
	private void updateAllItems(){
		String showInfo = "";
		//
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
			showInfo = "";
		}else{
			roomComboBox.setEnabled(true);
			//
			Building buildingSelected = getSelectedBuilding();
			//
			Vector<Room> rooms = null;
			try{
				rooms = buildingSelected.getAllRooms();
			}catch(Exception e1){
				e1.printStackTrace();
			}
			//
			Vector<String> roomNumbers = new Vector<String>();
			//
			for(int i=0; i<rooms.size(); i++){
				try{
					roomNumbers.add(""+rooms.get(i).id.id);
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
			Room roomSelected = rooms.get(roomComboBox.getSelectedIndex());
			//
			Vector<User> residentsInRoom = null;
			try{
				residentsInRoom = roomSelected.getOccupyingUsers();
			}catch(Exception e){
				e.printStackTrace();
			}
			//
			showInfo += "Residents:\n";
			//
			if(residentsInRoom.size() != 0){
				try{
					for(int i=0; i<residentsInRoom.size(); i++){
						showInfo += "  "+(i+1)+") "+residentsInRoom.get(i).getFirstName()+" "+residentsInRoom.get(i).getLastName()+"\n";
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				showInfo += "  "+"none\n";
			}
			//
			try{
				showInfo += "Total Room Capacity: "+roomSelected.getCapacity()+"\n";
			}catch(Exception e){
				e.printStackTrace();
			}
			//
			String devices = null;
			try{
				devices = roomSelected.getDevices();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(devices != null){
				showInfo += "Devices in Room: "+devices+"\n";
			}else{
				showInfo += "Devices in Room: none\n";
			}
		}
		//
		roomInfoTextArea.setText(showInfo);
	}
	//
	private Building getSelectedBuilding(){
		return new Building(buildingIDs.get(buildingComboBox.getSelectedIndex()-1), residenceDataGateway, userDataGateway);
	}
}