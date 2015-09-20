package mainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ManageTeamsWindow extends JDialog implements MouseListener {

	private static final long serialVersionUID = 9045593882432159130L;
	private JPanel pnlMain;
	private JPanel pnlCmbs;
	private JPanel pnlButton;
	private JLabel lblInfo;
	private JComboBox<String> cmbAllTeams;
	private JComboBox<String> cmbFavTeams;
	private JLabel lblAdd;
	private JLabel lblRemove;
	private JLabel lblCancel;
	private JLabel lblSave;
	private JLabel lblAllTeams;
	private JLabel lblYourTeams;
	private ArrayList<String> oldTeams;
	public ArrayList<String> newTeams;
	private Theme usedTheme;
	public boolean success = false;
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 12);
	private final String[] teamNames = new String[]{"Anaheim Ducks", "Arizona Coyotes", "Boston Bruins", "Buffalo Sabres", "Calgary Flames", "Carolina Hurricanes", "Chicago Blackhawks",
			"Colorado Avalanche", "Columbus Blue Jackets", "Dallas Stars", "Detroit Red Wings", "Edmonton Oilers", "Florida Panthers", "Los Angeles Kings", "Minnesota Wild", "Montréal Canadiens",
			"Nashville Predators", "New Jersey Devils", "New York Islanders", "New York Rangers", "Ottawa Senators", "Philadelphia Flyers", "Pittsbrugh Penguins", "San Jose Sharks", "St. Louis Blues",
			"Tampa Bay Lightning", "Toronto Maple Leafs", "Vancouver Canucks", "Washington Capitals", "Winnipeg Jets"};
	
	public ManageTeamsWindow(ArrayList<String> old, Theme thm, Point location){
		this.oldTeams = old;
		this.newTeams = this.oldTeams;
		this.usedTheme = thm;
		
		lblInfo = new JLabel("<html>Adding teams to your Favourite Teams makes sure<br>that whenever your team is playing,"
				+ " their game<br>panel will always be at the top of the game list.</html>");
		lblInfo.setFont(DEFAULT_FONT);
		lblInfo.setBackground(usedTheme.getSecondaryColor());
		lblInfo.setOpaque(true);
		lblInfo.setForeground(usedTheme.getPrimaryFontColor());
		lblInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		lblAllTeams = new JLabel("Available Teams:");
		lblAllTeams.setFont(DEFAULT_FONT);
		lblAllTeams.setBackground(usedTheme.getSecondaryColor());
		lblAllTeams.setOpaque(true);
		lblAllTeams.setForeground(usedTheme.getPrimaryFontColor());
		lblYourTeams = new JLabel("Your Favourite Teams:");
		lblYourTeams.setFont(DEFAULT_FONT);
		lblYourTeams.setBackground(usedTheme.getSecondaryColor());
		lblYourTeams.setOpaque(true);
		lblYourTeams.setForeground(usedTheme.getPrimaryFontColor());
		lblAdd = new JLabel("Add", JLabel.CENTER);
		lblAdd.setOpaque(true);
		lblAdd.setBackground(usedTheme.getQuarternaryColor());
		lblAdd.setForeground(Color.WHITE);
		lblAdd.setFont(new Font("Arial", Font.BOLD, 16));
		lblAdd.addMouseListener(this);
		lblRemove = new JLabel("Remove", JLabel.CENTER);
		lblRemove.setOpaque(true);
		lblRemove.setBackground(usedTheme.getTertiaryColor());
		lblRemove.setForeground(Color.WHITE);
		lblRemove.setFont(new Font("Arial", Font.BOLD, 16));
		lblRemove.addMouseListener(this);
		cmbAllTeams = new JComboBox<String>();
		cmbAllTeams.setBackground(usedTheme.getSecondaryColor());
		cmbAllTeams.setFont(DEFAULT_FONT);
		cmbAllTeams.setOpaque(true);
		cmbAllTeams.setForeground(usedTheme.getQuarternaryFontColor());
		cmbFavTeams = new JComboBox<String>();
		cmbFavTeams.setBackground(usedTheme.getSecondaryColor());
		cmbFavTeams.setFont(DEFAULT_FONT);
		cmbFavTeams.setOpaque(true);
		cmbFavTeams.setForeground(usedTheme.getQuarternaryFontColor());
		UpdateCmbs(AvailableTeams(), newTeams);
		
		pnlCmbs = new JPanel();
		pnlCmbs.setLayout(new GridLayout(4,2,5,5));
		pnlCmbs.setBackground(usedTheme.getSecondaryColor());
		pnlCmbs.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pnlCmbs.add(lblAllTeams);
		pnlCmbs.add(new JLabel());
		pnlCmbs.add(cmbAllTeams);
		pnlCmbs.add(lblAdd, BorderLayout.EAST);
		pnlCmbs.add(lblYourTeams);
		pnlCmbs.add(new JLabel());
		pnlCmbs.add(cmbFavTeams);
		pnlCmbs.add(lblRemove, BorderLayout.EAST);
		
		// Button Panel //
		pnlButton = new JPanel();
		pnlButton.setLayout(new GridLayout(1,2,5,0));
		pnlButton.setBackground(usedTheme.getSecondaryColor());
		pnlButton.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));
		lblCancel = new JLabel("Cancel", JLabel.CENTER);
		lblCancel.setOpaque(true);
		lblCancel.setBackground(usedTheme.getTertiaryColor());
		lblCancel.setForeground(Color.WHITE);
		lblCancel.setFont(new Font("Arial", Font.BOLD, 16));
		lblCancel.addMouseListener(this);
		lblCancel.setPreferredSize(new Dimension(100,25));
		lblSave = new JLabel("Save", JLabel.CENTER);
		lblSave.setOpaque(true);
		lblSave.setBackground(usedTheme.getQuarternaryColor());
		lblSave.setForeground(Color.WHITE);
		lblSave.setFont(new Font("Arial", Font.BOLD, 16));
		lblSave.addMouseListener(this);
		lblSave.setPreferredSize(new Dimension(110,25));
		pnlButton.add(lblCancel);
		pnlButton.add(lblSave);
		
		pnlMain = new JPanel();
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setBackground(usedTheme.getSecondaryColor());
		pnlMain.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		pnlMain.add(lblInfo, BorderLayout.NORTH);
		pnlMain.add(pnlCmbs);
		pnlMain.add(pnlButton, BorderLayout.SOUTH);
		
		this.add(pnlMain);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(winIcon.getImage());
		this.setUndecorated(true);
		this.pack();
		this.setLocation(location.x - this.getWidth() - 3, location.y);
		this.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == lblAdd){
			this.newTeams.add((String)cmbAllTeams.getSelectedItem());
			UpdateCmbs(AvailableTeams(), newTeams);
		}else if (e.getSource() == lblRemove){
			if (this.newTeams.size() > 0){
				this.newTeams.remove(cmbFavTeams.getSelectedIndex());
				UpdateCmbs(AvailableTeams(), newTeams);	
			}
		}else if (e.getSource() == lblCancel){
			this.dispose();
		}else if (e.getSource() == lblSave){
			this.success = true;
			this.dispose();
		}
	}

	// Gets the available teams to allocate as favourites
	private ArrayList<String> AvailableTeams(){
		ArrayList<String> teams = new ArrayList<String>();
		for (String team : teamNames){
			if (!this.newTeams.contains(team)){	// If the team is not already in the user's favourite teams
				teams.add(team);
			}
		}
		return teams;
	}
	
	// Update the ComboBoxes with available values
	private void UpdateCmbs(ArrayList<String> availTeams, ArrayList<String> favTeams){
		this.cmbAllTeams.setModel(new DefaultComboBoxModel<String>(ListToArray(availTeams)));
		this.cmbFavTeams.setModel(new DefaultComboBoxModel<String>(ListToArray(favTeams)));
	}
	
	private String[] ListToArray(ArrayList<String> list){
		Collections.sort(list);
		String[] arr = new String[list.size()];
		for (int i = 0; i < list.size(); i++){
			arr[i] = list.get(i);
		}
		if (arr.length == 0){
			arr = new String[]{"No teams"};
		}
		
		return arr;
	}
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	

}
