package mainPackage;
/*
 * A GamePanel is a complex UI element which compactly holds
 * the necessary information for an NHL game, including the
 * two team's icons, names, goals scored, and the status of 
 * the game. It can be updated with new scraped information
 * by calling UpdatePanel();.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
	
	// UI Elements
	protected static final long serialVersionUID = -24209034273971017L;
	protected JPanel pnlGameInfo;
	protected JPanel[] pnlTeams = new JPanel[2];
	protected JPanel[] pnlOneTeam = new JPanel[2];
	protected JPanel[] pnlNames = new JPanel[2];
	protected JLabel lblPeriod;
	protected JLabel[] lblTeamIcons = new JLabel[2];
	protected JLabel[] lblTeamNames = new JLabel[2];
	protected JLabel[] lblTeamGoals = new JLabel[2];
	
	// Miscellaneous Data
	private static final Font DEFAULT_GAME_INFO_FONT = new Font("Arial", Font.PLAIN, 12);
	
	public GamePanel(){
		
		// --- UI Creation --- //
		pnlGameInfo = new JPanel();
		pnlGameInfo.setLayout(new GridLayout(2,1,0,0));
		pnlGameInfo.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		pnlGameInfo.setBackground(Color.WHITE);
		lblPeriod = new JLabel();
		lblPeriod.setBorder(BorderFactory.createEmptyBorder(0,15,0,10));
		lblPeriod.setFont(DEFAULT_GAME_INFO_FONT);
		lblPeriod.setPreferredSize(new Dimension(90,30));
		
		// Individual Team Panels //
		for (int i = 0; i < 2; i++){
			pnlTeams[i] = new JPanel();
			pnlTeams[i].setLayout(new BorderLayout());
			pnlTeams[i].setBackground(Color.WHITE);
			lblTeamIcons[i] = new JLabel();
			lblTeamIcons[i].setBorder(BorderFactory.createEmptyBorder(5,5,5,15));
			
			pnlNames[i] = new JPanel();
			pnlNames[i].setLayout(new BorderLayout());
			pnlNames[i].setBackground(Color.WHITE);
			lblTeamNames[i] = new JLabel();
			lblTeamNames[i].setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
			lblTeamNames[i].setFont(DEFAULT_GAME_INFO_FONT);
			lblTeamGoals[i] = new JLabel();
			lblTeamGoals[i].setFont(DEFAULT_GAME_INFO_FONT);
			pnlNames[i].add(lblTeamNames[i], BorderLayout.WEST);
			pnlNames[i].add(lblTeamGoals[i], BorderLayout.EAST);
			
			pnlTeams[i].add(lblTeamIcons[i], BorderLayout.WEST);
			pnlTeams[i].add(pnlNames[i]);                              
			pnlGameInfo.add(pnlTeams[i]);
		}
		
		// Window startup
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.add(pnlGameInfo);
		this.add(lblPeriod, BorderLayout.EAST);
	}
	
	// Updates a panel with scraped information
	protected void UpdatePanel(String[] names, int[] goals, String time){
		
		if (time.equals("End 3RD") && goals[0] != goals[1]) {
			time = "FINAL";
		}
		
		for (int i = 0; i < 2; i++){
			this.lblTeamIcons[i].setIcon(new ImageIcon("Resources/" + names[i] + ".png"));
			this.lblTeamNames[i].setText(names[i]);
			this.lblTeamGoals[i].setText(goals[i] + "");
			this.lblPeriod.setText(time);	
			
			// Bolds the winning team and their score, and if the game is over
			if (((goals[0] > goals[1] && i == 0) || (goals[1] > goals[0] && i == 1)) && time.equals("FINAL")){
				this.lblTeamNames[i].setFont(new Font(this.lblTeamNames[i].getFont().getFontName(), Font.BOLD, this.lblTeamNames[i].getFont().getSize()));
				this.lblTeamGoals[i].setFont(new Font(this.lblTeamGoals[i].getFont().getFontName(), Font.BOLD, this.lblTeamGoals[i].getFont().getSize()));
			}
		}

		if (time.equals("FINAL")){    // If the game is over, grays and bolds 'FINAL'
			lblPeriod.setForeground(Color.GRAY);
			this.lblPeriod.setFont(new Font(this.lblPeriod.getFont().getFontName(), Font.BOLD, this.lblPeriod.getFont().getSize()));
		}else if (time.contains("3RD") && time.length() < 9 && time.charAt(0) < '5'){    // If the game is in the last 5 minutes of play, bolds and colors the game time red
			lblPeriod.setForeground(new Color(249,13,25));
			this.lblPeriod.setFont(new Font(this.lblPeriod.getFont().getFontName(), Font.BOLD, this.lblPeriod.getFont().getSize()));
		}else if (time.contains("PM")) {   // Game has not started yet 
			lblTeamGoals[0].setText("");
			lblTeamGoals[1].setText("");
		}
	}
}
