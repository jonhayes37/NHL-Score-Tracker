package mainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
	
	protected static final long serialVersionUID = -24209034273971017L;
	protected JPanel pnlGameInfo;
	protected JPanel[] pnlTeams = new JPanel[2];
	protected JPanel[] pnlOneTeam = new JPanel[2];
	protected JPanel[] pnlNames = new JPanel[2];
	protected JLabel lblPeriod;
	protected JLabel[] lblTeamIcons = new JLabel[2];
	protected JLabel[] lblTeamNames = new JLabel[2];
	protected JLabel[] lblTeamGoals = new JLabel[2];
	
	public GamePanel(){
		
		pnlGameInfo = new JPanel();
		pnlGameInfo.setLayout(new GridLayout(2,1,0,5));
		pnlGameInfo.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		pnlGameInfo.setBackground(Color.WHITE);
		lblPeriod = new JLabel();
		lblPeriod.setForeground(Color.GRAY);
		lblPeriod.setBorder(BorderFactory.createEmptyBorder(0,15,0,10));
		
		for (int i = 0; i < 2; i++){
			pnlTeams[i] = new JPanel();
			pnlTeams[i].setLayout(new BorderLayout());
			pnlTeams[i].setBackground(Color.WHITE);
			lblTeamIcons[i] = new JLabel();
			lblTeamIcons[i].setBorder(BorderFactory.createEmptyBorder(5,5,5,10));
			
			pnlNames[i] = new JPanel();
			pnlNames[i].setLayout(new BorderLayout());
			pnlNames[i].setBackground(Color.WHITE);
			lblTeamNames[i] = new JLabel();
			lblTeamNames[i].setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
			lblTeamGoals[i] = new JLabel();
			pnlNames[i].add(lblTeamNames[i], BorderLayout.WEST);
			pnlNames[i].add(lblTeamGoals[i], BorderLayout.EAST);
			
			pnlTeams[i].add(lblTeamIcons[i], BorderLayout.WEST);
			pnlTeams[i].add(pnlNames[i]);
			pnlGameInfo.add(pnlTeams[i]);
		}
		
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.add(pnlGameInfo);
		this.add(lblPeriod, BorderLayout.EAST);
	}
	
	// Updates a panel with scraped information
	protected void UpdatePanel(String[] names, int[] goals, String time){
		for (int i = 0; i < 2; i++){
			this.lblTeamIcons[i].setIcon(new ImageIcon("Resources/" + names[i] + ".png"));
			this.lblTeamNames[i].setText(names[i]);
			this.lblTeamGoals[i].setText(goals[i] + "");
			this.lblPeriod.setText(time);
			
			// Bolds the winning team and their score, and if the game is over
			if ((goals[0] > goals[1] && i == 0) || (goals[1] > goals[0] && i == 1)){
				this.lblTeamNames[i].setFont(new Font(this.lblTeamNames[i].getFont().getFontName(), Font.BOLD, this.lblTeamNames[i].getFont().getSize()));
				this.lblTeamGoals[i].setFont(new Font(this.lblTeamGoals[i].getFont().getFontName(), Font.BOLD, this.lblTeamGoals[i].getFont().getSize()));
			}
		}
		if (time.equals("FINAL")){
			this.lblPeriod.setFont(new Font(this.lblPeriod.getFont().getFontName(), Font.BOLD, this.lblPeriod.getFont().getSize()));
		}
	}
}
