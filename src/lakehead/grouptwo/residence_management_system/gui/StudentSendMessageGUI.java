package lakehead.grouptwo.residence_management_system.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import lakehead.grouptwo.residence_management_system.data.IAccountData;
import lakehead.grouptwo.residence_management_system.data.User;
import lakehead.grouptwo.residence_management_system.data.gateways.IResidenceDataGateway;
import lakehead.grouptwo.residence_management_system.data.gateways.IUserDataGateway;

public class StudentSendMessageGUI extends JFrame{
	private static final long serialVersionUID = 3746053895472051221L;
	//
	private JPanel contentPane;
	private JTextArea textField;
	//
	private IResidenceDataGateway residenceDataGateway;
	private IUserDataGateway userDataGateway;
	private IAccountData accountData;
	//
	public StudentSendMessageGUI(IResidenceDataGateway _residenceDataGateway, IUserDataGateway _userDataGateway, IAccountData _accountData){
		residenceDataGateway = _residenceDataGateway;
		userDataGateway = _userDataGateway;
		accountData = _accountData;
		//
		setTitle("Send Message to Housing Management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 457, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JLabel messageLabel = new JLabel("Write message below:");
		messageLabel.setBounds(10, 11, 159, 20);
		contentPane.add(messageLabel);
		
		textField = new JTextArea();
		textField.setBounds(10, 42, 414, 209);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton submitMesButton = new JButton("Submit Message");
		submitMesButton.setBounds(157, 262, 129, 33);
		submitMesButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				String Mes = textField.getText();
				
				try{
					residenceDataGateway.sendMessage(accountData.getThisUserID(), new User(accountData.getThisUserID(), residenceDataGateway, userDataGateway).getResidenceRoom().getContainingBuilding().getManager().id, Mes);
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null, "Could not send the message!");
					e1.printStackTrace();
					return;
				}
				
				//System.out.println(Mes); // working check
				
				setVisible(false);
				ResidentGUI Res = new ResidentGUI(residenceDataGateway, userDataGateway, accountData);
				Res.setVisible(true);
				
			}
		});
		contentPane.add(submitMesButton);
		
	}
}
