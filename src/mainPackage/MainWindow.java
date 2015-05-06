package mainPackage;
/*
 * The NHL Score Tracker is a compact, borderless window which
 * sits at the bottom right of the screen. It regularly scrapes
 * data from Sportsnet, and updates the UI accordingly.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MainWindow extends JFrame implements MouseListener{

	// UI Elements
	private static final long serialVersionUID = -2398924529942399252L;
	private JPanel pnlMain;
	private JPanel pnlTop;
	private JPanel pnlListGames;
	private JScrollPane pnlScroll;
	private GamePanel[] pnlGames;
	private JLabel lblDate;
	private JLabel lblSettings;
	private JLabel lblClose;

	// Data from scraping
	private short numGames;
	private String[][] teamNames;
	private int[][] teamGoals;
	private String[] gameTime;
	
	// Miscellaneous data
	private Calendar date = Calendar.getInstance();  // Current date in local timezone
	private ScraperSettings settings = new ScraperSettings();
	private final static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private static final String website = "http://www.sportsnet.ca/hockey/nhl/scores/";
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");

	public MainWindow(){
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1){e1.printStackTrace();}
		settings.Load();
		
		// --- UI Creation --- //
		pnlMain = new JPanel();
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlMain.setBackground(Color.WHITE);
		
		// Top Panel //
		pnlTop = new JPanel();
		pnlTop.setLayout(new BorderLayout());
		pnlTop.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlTop.setBackground(Color.WHITE);
		lblSettings = new JLabel();
		lblSettings.setIcon(new ImageIcon("Resources/settings.png"));
		lblSettings.setBorder(BorderFactory.createEmptyBorder(3,3,3,0));
		lblSettings.addMouseListener(this);
		lblDate = new JLabel(FormatDate(), JLabel.CENTER);
		lblDate.setFont(new Font("Arial", Font.BOLD, 16));
		lblClose = new JLabel();
		lblClose.setIcon(new ImageIcon("Resources/close.png"));
		lblClose.setBorder(BorderFactory.createEmptyBorder(3,0,3,3));
		lblClose.addMouseListener(this);
		pnlTop.add(lblSettings, BorderLayout.WEST);
		pnlTop.add(lblDate);
		pnlTop.add(lblClose, BorderLayout.EAST);
		pnlListGames = new JPanel();
		
		// Initial Scraping / Updating main panel
		boolean[] changed = Scrape(true);
		UpdateUI(true, changed);
		ScheduleRefresh();
		
		// Starting up the Window
		pnlMain.add(pnlTop, BorderLayout.NORTH);
		pnlMain.add(pnlScroll);
		this.add(pnlMain);
		this.setAlwaysOnTop(settings.getOnTop());  // Sets the window to always be on top is checked
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(325, 350);
		this.setIconImage(winIcon.getImage());
		this.setUndecorated(true);
		this.pack();
		this.setLocation(screenSize.width - this.getWidth() - 2, 2);
		this.setVisible(true);
	}
	
	// Listener responses for refresh and close
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == lblSettings){  // Settings button
			new SettingsWindow(this.getLocation());
			settings.Load();      // Loads in and applies new settings
			ScheduleRefresh();
			this.setAlwaysOnTop(settings.getOnTop());  // Sets the window to always be on top is checked
		}else if (e.getSource() == lblClose){  // Close button
			this.dispose();
			System.exit(0);
		}
	}
	
	// Scrapes www.sportsnet.ca/hockey/nhl/scores/ for the day's games and their status
	private boolean[] Scrape(boolean startup){
		boolean[] scoreChanged = null;
		try {
			Document doc = Jsoup.connect(website).get();			
			// Navigates to proper container
			Elements teamCities = doc.getElementsByClass("scores-team-city");
			Elements teamName = doc.getElementsByClass("scores-team-name");
			Elements teamGoals = doc.getElementsByClass("team-score-container");
			Elements gameTimes = doc.select("td");	
				
			// Add info to local arrays
			int[][] oldGoals = this.teamGoals;
			this.numGames = (short)(teamCities.size() / 2);
			this.teamNames = new String[numGames][2];
			this.teamGoals = new int[numGames][2];
			this.gameTime = new String[numGames];
			scoreChanged = new boolean[numGames];
			
			for (int i = 0; i < numGames; i++){
							
				// Team Names and Goals
				this.teamNames[i][0] = teamCities.get(2 * i).text() + " " + teamName.get(2 * i).text();
				this.teamNames[i][1] = teamCities.get(2 * i + 1).text() + " " + teamName.get(2 * i + 1).text();
				this.teamGoals[i][0] = (teamGoals.get(2 * i).text().equals("")) ? 0 : Integer.parseInt(teamGoals.get(2 * i).text());
				this.teamGoals[i][1] = (teamGoals.get(2 * i + 1).text().equals("")) ? 0 : Integer.parseInt(teamGoals.get(2 * i + 1).text());

				// Game Time
				this.gameTime[i] = gameTimes.get(i).text();
				
				// Tracks if a goal was scored
				if (!startup && (this.teamGoals[i][0] != oldGoals[i][0] || this.teamGoals[i][1] != oldGoals[i][1])) {
					scoreChanged[i] = true;
				}else{
					scoreChanged[i] = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error: Unable to retrieve data from http://www.sportsnet.ca/hockey/nhl/scores/.\nPlease ensure you have the latest version of NHL Sccore Tracker.");
		}
		return scoreChanged;
	}
	
	// Updates UI elements with the latest scraped data
	private void UpdateUI(boolean startup, boolean[] goalScored){
		
		// Creates the new game panels from the given data
		pnlGames = new GamePanel[numGames];
		pnlListGames.removeAll();
		pnlListGames.setLayout(new GridLayout(numGames, 1, 0, 0));
		for (int i = 0; i < numGames; i++){
			pnlGames[i] = new GamePanel();
			pnlGames[i].UpdatePanel(teamNames[i], teamGoals[i], gameTime[i]);
		}
		
		// Sorting games, and adding them to the UI
		pnlGames = SortGames(pnlGames);
		for (int i = 0; i < pnlGames.length; i++) {
			pnlListGames.add(pnlGames[i]);
		}
		pnlScroll = new JScrollPane(pnlListGames, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Resizes the window if there are less than 3 games
		if (numGames > 2){
			pnlScroll.setPreferredSize(new Dimension(280,325));
		}else{
			pnlScroll.setPreferredSize(new Dimension(280, 10 + numGames * 105));
		}
		
		// If application is not scraping for the first time, resets the UI panel
		if (!startup) {
			pnlMain.remove(1);
			pnlMain.add(pnlScroll);
			this.validate();
			this.repaint();
			
			// Flashes games if a goal was scored
			for (int i = 0; i < numGames; i++) {
				if (goalScored[i]) {
					FlashPanel(pnlGames[i]);
				}
			}
		}
		
		// Updates date if necessary
		this.date = Calendar.getInstance();
		lblDate.setText(FormatDate()); 
	}
	
	// Scheduling auto-refresh
	private void ScheduleRefresh() {
		ScheduledExecutorService refresh = Executors.newSingleThreadScheduledExecutor();
		refresh.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run(){
				boolean[] changed = Scrape(false);
				UpdateUI(false, changed);
			}
		}, 0, settings.getRefreshFrequency(), TimeUnit.SECONDS);
	}
	
	// Formats the retrieved Calendar instance into a string for the application
	private String FormatDate(){
		if (this.date.get(Calendar.HOUR_OF_DAY) < 12 && this.date.get(Calendar.DAY_OF_MONTH) > 1) { 
			this.date.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH - 1); 
		}else if (this.date.get(Calendar.HOUR_OF_DAY) < 12) {
			this.date.set(Calendar.MONTH, Calendar.MONTH - 1);
			this.date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		String strDate = months[this.date.get(Calendar.MONTH)] + " " + this.date.get(Calendar.DAY_OF_MONTH) + ", " + this.date.get(Calendar.YEAR);
		return strDate;
	}
	
	// Sorts games by priority: OT > 3RD > 2ND > 1ST > Not started > FINAL,
	// then each priority is sorted by time remaining in the period (except for teh final two
	private GamePanel[] SortGames(GamePanel[] games) {
		GamePanel[] sortedGames = new GamePanel[games.length];
		int[] priorities = new int[games.length];
		
		for (int i = 0; i < games.length; i++) {  // Giving each game a priority
			String[] tempTime = games[i].lblPeriod.getText().split(" ");
			if (tempTime.length > 1) {
				if (tempTime[1].equals("1ST")) {    // Game is in the 1st period
					priorities[i] = 3;
				}else if (tempTime[1].equals("2ND")) {    // Game is in the 2nd period
					priorities[i] = 4;
				}else if (tempTime[1].equals("3RD")) {    // Game is in the 3rd period
					priorities[i] = 5;
				}else if (tempTime[i].equals("PM")) {    // Game hasn't started yet 
					priorities[i] = 2;
				}else{    // It's in OT
					priorities[i] = 6;
				}
			}else{   // Period is 'FINAL'
				priorities[i] = 1;
			}
		}
		
		// Sorts the games by priority, and adjusts priority indices as required
		int curIndex = 0;
		int[] newPriorities = new int[games.length];
		for (int i = 6; i > 0; i--) {
			for (int j = 0; j < games.length; j++) {
				if (priorities[j] == i) {
					sortedGames[curIndex] = games[j];
					newPriorities[curIndex] = priorities[j];
					curIndex++;
				}
			}
		}
		
		// Sorts the games by time remaining in the period
		boolean notSorted = true;
		while (notSorted) {  
			notSorted = false;
			for (int i = 0; i < sortedGames.length; i++) {
				if (i + 1 < sortedGames.length && newPriorities[i] == newPriorities[i + 1] && newPriorities[i + 1] > 2) {
					String[] time1Nums = sortedGames[i].lblPeriod.getText().split(" ")[0].split(":");
					String[] time2Nums = sortedGames[i + 1].lblPeriod.getText().split(" ")[0].split(":");
					double time1 = Integer.parseInt(time1Nums[0]) + Integer.parseInt(time1Nums[1]) / 60.0;
					double time2 = Integer.parseInt(time2Nums[0]) + Integer.parseInt(time2Nums[1]) / 60.0;
					if (time2 < time1) {  // Need to swap times
						notSorted = true;
						GamePanel tempGame = sortedGames[i];
						sortedGames[i] = sortedGames[i + 1];
						sortedGames[i + 1] = tempGame;
					}
				}
			}
		}
		return sortedGames;
	}

	// Flashes and fades a game card to show a change in score
	private void FlashPanel(GamePanel game) {
		Color tempColor = new Color(240,220,130);  // Initial flash colour
		for (int i = 1; i <= 2625; i++) {
			if (i % 21 == 0) { 
				tempColor = new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue() + 1);
			}
			if (i % 75 == 0) {
				tempColor = new Color(tempColor.getRed(), tempColor.getGreen() + 1, tempColor.getBlue());
			}
			if (i % 175 == 0) { 
				tempColor = new Color(tempColor.getRed() + 1, tempColor.getGreen(), tempColor.getBlue());
			}
			game.setBackground(tempColor);
			game.lblPeriod.setBackground(tempColor);
			game.pnlGameInfo.setBackground(tempColor);
			game.pnlNames[0].setBackground(tempColor);
			game.pnlNames[1].setBackground(tempColor);
			game.pnlTeams[0].setBackground(tempColor);
			game.pnlTeams[1].setBackground(tempColor);
			this.validate();
			this.repaint();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	
	// Program entry point
	public static void main(String[] args){
		new MainWindow();
	}
	
}
