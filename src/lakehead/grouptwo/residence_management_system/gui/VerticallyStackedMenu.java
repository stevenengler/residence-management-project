package lakehead.grouptwo.residence_management_system.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class VerticallyStackedMenu extends JFrame{
	private static final long serialVersionUID = 1627445308597466578L;
	//
	protected JPanel contentPane = new JPanel();
	//
	public VerticallyStackedMenu(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		setLocationRelativeTo(null);
		setContentPane(contentPane);
	}
	//
	protected void setDefaultMenuButtonProperties(JComponent component){
		component.setMaximumSize(new Dimension(400, 50));
		component.setPreferredSize(new Dimension(400, 50));
		component.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	//
	protected JPanel createLeftAlignedJPanel(){
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		newPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		return newPanel;
	}
	//
	protected Component createVerticalPaddingComponent(){
		return Box.createRigidArea(new Dimension(0,5));
	}
}
