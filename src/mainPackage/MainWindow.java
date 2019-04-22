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
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
	ArrayList<Game> games;
	
	// Miscellaneous data
	private static final String osName = System.getProperty("os.name");
	private Calendar date = Calendar.getInstance();  // Current date in local timezone
	private ScraperSettings settings = new ScraperSettings();
	private ScheduledExecutorService refresh;
	ScheduledFuture<?> scheduledFuture = null;
	private final String PROJECT_NAME = "NHL Score Tracker";
	private final String VERSION_NUMBER = "1.4.5";
	private static final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private static final String WEBSITE_URL = "https://www.msn.com/en-ca/sports/nhl/scores";
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private List<Image> icons = new ArrayList<Image>();
	private int uiOffset = (osName.contains("Mac")) ? 15 : 0;
	private int consecFailScrapes = 0;

	public MainWindow(){
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1){e1.printStackTrace();}
		LoadingWindow ldw = new LoadingWindow(this);
		settings.Load();
		icons.add(new ImageIcon("Resources/icon-16x16.png").getImage()); 
		icons.add(new ImageIcon("Resources/icon.png").getImage());
		
		// --- UI Creation --- //
		pnlMain = new JPanel();
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlMain.setBackground(settings.getTheme().getPrimaryColor());
		
		// Top Panel //
		pnlTop = new JPanel();
		pnlTop.setLayout(new BorderLayout());
		pnlTop.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlTop.setBackground(settings.getTheme().getPrimaryColor());
		lblSettings = new JLabel();
		lblSettings.setIcon(new ImageIcon("Resources/settings.png"));
		lblSettings.setBorder(BorderFactory.createEmptyBorder(3,3,3,0));
		lblSettings.addMouseListener(this);
		lblDate = new JLabel("", JLabel.CENTER);
		lblDate.setFont(new Font("Arial", Font.BOLD, 16));
		lblDate.setOpaque(true);
		lblDate.setForeground(settings.getTheme().getTertiaryFontColor());
		lblDate.setBackground(settings.getTheme().getPrimaryColor());
		lblClose = new JLabel();
		lblClose.setIcon(new ImageIcon("Resources/close.png"));
		lblClose.setBorder(BorderFactory.createEmptyBorder(3,0,3,3));
		lblClose.addMouseListener(this);
		pnlTop.add(lblSettings, BorderLayout.WEST);
		pnlTop.add(lblDate);
		pnlTop.add(lblClose, BorderLayout.EAST);
		pnlListGames = new JPanel();
		
		// Initial Scraping / Updating main panel / Updating
		this.games = new ArrayList<Game>();
		boolean[] changed = Scrape(true);
		UpdateUI(true, changed);
		ScheduleRefresh();
		
		ldw.EndLoop();
		
		// Starting up the Window
		pnlMain.add(pnlTop, BorderLayout.NORTH);
		pnlMain.add(pnlScroll);
		this.add(pnlMain);
		this.setTitle("NHL Score Tracker " + VERSION_NUMBER);
		this.setAlwaysOnTop(settings.getOnTop());  // Sets the window to always be on top is checked
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(350, 350);
		this.setIconImages(icons);
		this.dispose();
		this.setUndecorated(settings.getIsBorderless());
		this.pack();
		this.setLocation(screenSize.width - this.getWidth() - 2, 2 + uiOffset);
		this.setVisible(true);
		
//		if (settings.getAutoUpdate()){
//			Updater update = new Updater(this.PROJECT_NAME, this.VERSION_NUMBER);
//			update.CheckVersion();
//		}
	}
	
	// Listener responses for settings and close
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == lblSettings){   // Settings button
			SettingsWindow newSetWindow = new SettingsWindow(this.getLocation());
			String buttonPushed = newSetWindow.result;
			ScraperSettings newSet = new ScraperSettings();      // Loads in new settings for comparison
			newSet.Load();
			if (buttonPushed.equals("Save") && !newSet.isEqual(settings)) {  // If a change has been made in settings, restarts the tracker 
				settings.Load();
				scheduledFuture.cancel(false);
				this.dispose();
				new MainWindow();  // Restarts with new settings
			}
		}else if (e.getSource() == lblClose){   // Close button
			scheduledFuture.cancel(false);
			this.dispose();
			System.exit(0);
		}
	}
	
	// Scrapes for the day's games and their status
	private boolean[] Scrape(boolean startup){
		boolean[] scoreChanged = null;
		try {
			Document doc = Jsoup.connect(WEBSITE_URL).get();			
			
			// Navigates to proper container for each piece of data
			Element scoresTable = doc.selectFirst(".icehockeyscorestable");
			Elements scorecards = scoresTable.select("tbody.rowlink");
			
			// Add info to local arrays
			scoreChanged = new boolean[scorecards.size()];

			for (Element card: scorecards){
				String winningTeamName = card.selectFirst("tr").selectFirst("td.teamname").text();
				String losingTeamName = card.select("tr").get(1).selectFirst("td.teamname").text();
				short winningGoals = Short.parseShort(card.selectFirst("td.winningteam").text());
				Elements awayTeamGoals = card.select("tr").get(1).select("td.teamscore");
				short losingGoals = Short.parseShort(awayTeamGoals.get(awayTeamGoals.size() - 1).text());
				String gameTime = this.FormatGameTime(card.selectFirst("td.matchstatus").text());
				
				// Adding formatted team names and goals and the game time to internal data arrays
				Game curGame = new Game(winningTeamName, losingTeamName, winningGoals, losingGoals, gameTime);
				if (!startup) {
					for (short i = 0; i < this.games.size(); i++) {
						Game g = this.games.get(i);
						if (g.equals(curGame)) {
							if (g.scoreChanged(curGame)) {
								scoreChanged[i] = true;
							}
							this.games.set(i, curGame);
						}
					}
				}else {
					this.games.add(curGame);
				}
			}
			
		} catch (IOException e) {   // Gives an error dialog if unable to connect to the website, then closes
			e.printStackTrace();
			if (consecFailScrapes * settings.getRefreshFrequency() > 180){
				JOptionPane errorPane = new JOptionPane("<html><div style=\"text-align: center;\">We're having issues retrieving score data. Please ensure you have a stable internet<br>connection and the latest version of NHL Score Tracker (this is version " + VERSION_NUMBER + ").</html>");
			    JDialog errorDialog = errorPane.createDialog((JFrame)null, "Retrieval Error");
			    errorDialog.setLocationRelativeTo(this);
			    errorDialog.setVisible(true);
				scheduledFuture.cancel(false);
			}
		}
		return scoreChanged;
	}
	
	// Formats a game time string to '20:00 1ST' if the game has actually started but the website hasn't updated yet
	private String FormatGameTime(String time) {
		int tempHour, tempMins;
		if (time.contains("PM")) {
			tempHour = Integer.parseInt(time.split(" ")[0].split(":")[0]);
			tempMins = Integer.parseInt(time.split(" ")[0].split(":")[1]);
			Calendar tempDate = Calendar.getInstance();
			tempDate.set(Calendar.HOUR_OF_DAY, (tempHour == 12) ? tempHour : (tempHour + 12));
			tempDate.set(Calendar.MINUTE, tempMins);
			if (this.date.compareTo(tempDate) > 0) {  // Time is currently after the game time text
				return "20:00 1st";
			}
		}else if (time.contains("ET")) {
			return time.replaceAll("ET", "OT");
		}
		return time;
	}
	
	// Updates UI elements with the latest scraped data
	private void UpdateUI(boolean startup, boolean[] goalScored){
		// Creates the new game panels from the given data
		pnlGames = new GamePanel[this.games.size()];
		pnlListGames.removeAll();
		if (this.games.size() > 0) {
			pnlListGames.setLayout(new GridLayout(this.games.size(), 1, 0, 0));
			for (int i = 0; i < this.games.size(); i++){
				pnlGames[i] = new GamePanel(settings.getTheme());
				pnlGames[i].UpdatePanel(this.games.get(i));
			}
			
			// Sorting games, and adding them to the UI
			pnlGames = (pnlGames.length > 1) ? SortGames(pnlGames) : pnlGames;
			for (int i = 0; i < pnlGames.length; i++) {
				pnlListGames.add(pnlGames[i]);
			}
			pnlScroll = new JScrollPane(pnlListGames, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			pnlScroll.getVerticalScrollBar().setUnitIncrement(98);
			
			// Resizes the window if there are less games than the minimum required to show
			if (this.games.size() < settings.getMinGamesShown()){
				pnlScroll.setPreferredSize(new Dimension(325, this.games.size() * 98));
			}else{
				pnlScroll.setPreferredSize(new Dimension(325, settings.getMinGamesShown() * 98));
			}
		}else{   // No games on today
			/*
			 * Title Panel -> primaryColor
			 * Game Panel UI and SettingsWindow UI -> secondaryColor
			 * Cancel Button -> tertiaryColor
			 * Save Button -> quarternaryColor
			 * Game Panel Border -> quintiaryColor
			 * Settings text and Team Names -> primaryFontColor
			 * Game Time -> secondaryFontColor
			 * Date -> tertiaryFontColor
			 * ComboBox text -> quarternaryFontColor
			 */
			
			JPanel pnlGame = new JPanel();
			pnlGame.setLayout(new GridBagLayout());
			pnlGame.setBackground(settings.getTheme().getSecondaryColor());
			pnlGame.setPreferredSize(new Dimension(300, 98));
			JLabel lblNoGames = (this.date.get(Calendar.HOUR_OF_DAY) < 12) ? new JLabel("No games yesterday", JLabel.CENTER) : new JLabel("No games today", JLabel.CENTER);
			lblNoGames.setForeground(settings.getTheme().getSecondaryFontColor());
			lblNoGames.setOpaque(true);
			lblNoGames.setBackground(settings.getTheme().getSecondaryColor());
			lblNoGames.setFont(new Font("Arial", Font.BOLD, 16));
			pnlGame.add(lblNoGames);
			pnlScroll = new JScrollPane(pnlGame, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		
		// If application is not scraping for the first time, resets the UI panel
		if (!startup) {
			pnlMain.remove(1);
			pnlMain.add(pnlScroll);
			this.pack();
			this.validate();
			this.repaint();
			
			// Flashes games if a goal was scored
			for (int i = 0; i < this.games.size(); i++) {
				if (goalScored[i]) {
					this.toFront();
					this.repaint();
					if (settings.getFlash()) {
						FlashPanel(pnlGames[i]);
					}
				}
			}
		}
		
		// Updates date if necessary
		this.date = Calendar.getInstance();
		lblDate.setText(FormatDate()); 
	}
	
	// Scheduling auto-refresh
	private void ScheduleRefresh() {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(false);
		}
		refresh = Executors.newSingleThreadScheduledExecutor();
		scheduledFuture = refresh.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run(){
				boolean[] changed = Scrape(false);
				UpdateUI(false, changed);
			}
		}, 0, settings.getRefreshFrequency(), TimeUnit.SECONDS);
	}
	
	// Formats the retrieved Calendar instance into a string for the application
	private String FormatDate(){
		if (this.date.get(Calendar.HOUR_OF_DAY) < 12 && this.date.get(Calendar.DAY_OF_MONTH) > 1) {  // Need to go back a day (it's before noon)
			this.date.set(Calendar.DAY_OF_MONTH, this.date.get(Calendar.DAY_OF_MONTH) - 1); 
		}else if (this.date.get(Calendar.HOUR_OF_DAY) < 12) {   // Need to change month when going back a day (e.g. May 1 -> April 30)
			this.date.set(Calendar.MONTH, this.date.get(Calendar.MONTH) - 1);
			this.date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		String strDate = months[this.date.get(Calendar.MONTH)] + " " + this.date.get(Calendar.DAY_OF_MONTH) + ", " + this.date.get(Calendar.YEAR);
		return strDate;
	}
	
	// Sorts games by priority: 2 Fav Teams > 1 Fav Team > OT > 3RD > 2ND > 1ST > Not started > FINAL,
	// then each priority is sorted by time remaining in the period (except for teh final two
	private GamePanel[] SortGames(GamePanel[] games) {
		GamePanel[] sortedGames = new GamePanel[games.length];
		int[] priorities = new int[games.length];
		
		for (int i = 0; i < games.length; i++) {  // Giving each game a priority
			String[] tempTime = games[i].lblPeriod.getText().split(" ");
			if (tempTime.length > 1 && !tempTime[0].contains("FINAL") && !tempTime[1].contains("FINAL")) {
				if (tempTime[1].equals("1ST")) {    // Game is in the 1st period
					priorities[i] = 3;
				}else if (tempTime[1].equals("2ND")) {    // Game is in the 2nd period
					priorities[i] = 4;
				}else if (tempTime[1].equals("3RD")) {    // Game is in the 3rd period
					priorities[i] = 5;
				}else if (tempTime[1].equals("PM")) {    // Game hasn't started yet 
					priorities[i] = 2;
				}else {    // It's in OT (not 'FINAL (OT)')
					priorities[i] = 6;
				}
				String team1 = games[i].lblTeamNames[0].getText();
				String team2 = games[i].lblTeamNames[1].getText();
				if (!tempTime[1].equals("PM") && this.settings.getFavTeams().contains(team1) && this.settings.getFavTeams().contains(team2)){	// If an active game contains two favourite teams
					priorities[i] += 14;
				}else if (!tempTime[1].equals("PM") && (this.settings.getFavTeams().contains(team1) || this.settings.getFavTeams().contains(team2))){	// If an active game contains one favourite team
					priorities[i] += 7;
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
					double time1 = (time1Nums[0].contains("END")) ? 0 : (Integer.parseInt(time1Nums[0]) + Integer.parseInt(time1Nums[1]) / 60.0);  // If time is "End 1ST", treat as if time left is 0:00
					double time2 = (time2Nums[0].contains("END")) ? 0 : (Integer.parseInt(time2Nums[0]) + Integer.parseInt(time2Nums[1]) / 60.0);
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
			
			// Increments RGB color values at intervals so that they reach Color.WHITE (255,255,255) simultaneously
			if (i % 21 == 0) { 
				tempColor = new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue() + 1);
			}
			if (i % 75 == 0) {
				tempColor = new Color(tempColor.getRed(), tempColor.getGreen() + 1, tempColor.getBlue());
			}
			if (i % 175 == 0) { 
				tempColor = new Color(tempColor.getRed() + 1, tempColor.getGreen(), tempColor.getBlue());
			}
			
			// Sets all game card components to the new colour
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
				if (i == 1) {
					Thread.sleep(750);
				}else{
					Thread.sleep(1, 250000);
				}
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
