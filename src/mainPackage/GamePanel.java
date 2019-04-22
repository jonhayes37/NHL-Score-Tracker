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
	private Theme usedTheme = new Theme();
	
	// Miscellaneous Data
	private static final Font DEFAULT_GAME_INFO_FONT = new Font("Arial", Font.PLAIN, 13);
	
	public GamePanel(Theme theme){
		
		this.usedTheme = theme;
		
		// --- UI Creation --- //
		pnlGameInfo = new JPanel();
		pnlGameInfo.setLayout(new GridLayout(2,1,0,0));
		pnlGameInfo.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		pnlGameInfo.setBackground(usedTheme.getSecondaryColor());
		lblPeriod = new JLabel("", JLabel.CENTER);
		lblPeriod.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		lblPeriod.setFont(DEFAULT_GAME_INFO_FONT);
		lblPeriod.setOpaque(true);
		lblPeriod.setForeground(usedTheme.getSecondaryFontColor());
		lblPeriod.setBackground(usedTheme.getSecondaryColor());
		lblPeriod.setPreferredSize(new Dimension(90,30));
		
		// Individual Team Panels //
		for (int i = 0; i < 2; i++){
			pnlTeams[i] = new JPanel();
			pnlTeams[i].setLayout(new BorderLayout());
			pnlTeams[i].setBackground(usedTheme.getSecondaryColor());
			lblTeamIcons[i] = new JLabel();
			lblTeamIcons[i].setBorder(BorderFactory.createEmptyBorder(5,5,5,15));
			lblTeamIcons[i].setBackground(usedTheme.getSecondaryColor());
			
			pnlNames[i] = new JPanel();
			pnlNames[i].setLayout(new BorderLayout());
			pnlNames[i].setBackground(usedTheme.getSecondaryColor());
			lblTeamNames[i] = new JLabel();
			lblTeamNames[i].setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
			lblTeamNames[i].setFont(DEFAULT_GAME_INFO_FONT);
			lblTeamNames[i].setOpaque(true);
			lblTeamNames[i].setForeground(usedTheme.getPrimaryFontColor());
			lblTeamNames[i].setBackground(usedTheme.getSecondaryColor());
			lblTeamGoals[i] = new JLabel("", JLabel.CENTER);
			lblTeamGoals[i].setFont(DEFAULT_GAME_INFO_FONT);
			lblTeamGoals[i].setOpaque(true);
			lblTeamGoals[i].setForeground(usedTheme.getPrimaryFontColor());
			lblTeamGoals[i].setBackground(usedTheme.getSecondaryColor());
			pnlNames[i].add(lblTeamNames[i], BorderLayout.WEST);
			pnlNames[i].add(lblTeamGoals[i], BorderLayout.EAST);
			
			pnlTeams[i].add(lblTeamIcons[i], BorderLayout.WEST);
			pnlTeams[i].add(pnlNames[i]);                              
			pnlGameInfo.add(pnlTeams[i]);
		}
		
		// Window startup
		this.setBorder(BorderFactory.createLineBorder(usedTheme.getQuintiaryColor()));
		this.setLayout(new BorderLayout());
		this.setBackground(usedTheme.getSecondaryColor());
		this.add(pnlGameInfo);
		this.add(lblPeriod, BorderLayout.EAST);
	}
	
	// Updates a panel with scraped information
	protected void UpdatePanel(Game g){
		if (g.getGameTime().equals("END 3RD") && !g.isTied()) {  // Sets the game to final if a team won but the site says 'END 3RD'
			g.setGameTime("FINAL");
		}
		
		// Updating the two team panels
		this.lblTeamIcons[0].setIcon(new ImageIcon("Resources/" + g.getHomeTeam() + ".png"));
		this.lblTeamIcons[1].setIcon(new ImageIcon("Resources/" + g.getAwayTeam() + ".png"));
		this.lblTeamNames[0].setText(g.getHomeTeam());
		this.lblTeamNames[1].setText(g.getAwayTeam());
		this.lblTeamGoals[0].setText(String.valueOf(g.getHomeGoals()));
		this.lblTeamGoals[1].setText(String.valueOf(g.getAwayGoals()));
		this.lblPeriod.setText(g.getGameTime());	
		
		// Bolds the winning team and their score, and if the game is over
		if (g.getHomeGoals() > g.getAwayGoals() && g.getGameTime().contains("FINAL")){
			this.lblTeamNames[0].setFont(new Font(this.lblTeamNames[0].getFont().getFontName(), Font.BOLD, this.lblTeamNames[0].getFont().getSize()));
			this.lblTeamGoals[0].setFont(new Font(this.lblTeamGoals[0].getFont().getFontName(), Font.BOLD, this.lblTeamGoals[0].getFont().getSize()));
		}else if (g.getAwayGoals() > g.getHomeGoals() && g.getGameTime().contains("FINAL")) {
			this.lblTeamNames[1].setFont(new Font(this.lblTeamNames[1].getFont().getFontName(), Font.BOLD, this.lblTeamNames[1].getFont().getSize()));
			this.lblTeamGoals[1].setFont(new Font(this.lblTeamGoals[1].getFont().getFontName(), Font.BOLD, this.lblTeamGoals[1].getFont().getSize()));
		}

		if (g.getGameTime().contains("FINAL")){    // If the game is over, grays and bolds 'FINAL'
			lblPeriod.setForeground(usedTheme.getSecondaryFontColor());
			this.lblPeriod.setFont(new Font(this.lblPeriod.getFont().getFontName(), Font.BOLD, this.lblPeriod.getFont().getSize()));
		}else if (g.getGameTime().contains("3RD") && g.getGameTime().length() < 9 && g.getGameTime().charAt(0) < '5'){    // If the game is in the last 5 minutes of play, bolds and colors the game time red
			lblPeriod.setForeground(new Color(249,13,25));
			this.lblPeriod.setFont(new Font(this.lblPeriod.getFont().getFontName(), Font.BOLD, this.lblPeriod.getFont().getSize()));
		}else if (g.getGameTime().contains("PM")) {   // Hides the goals if the game has not started yet
			lblTeamGoals[0].setText("");
			lblTeamGoals[1].setText("");
		}
	}
}
