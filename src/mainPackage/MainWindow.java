package mainPackage;

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
	private JLabel lblRefresh;
	private JLabel lblClose;

	// Data from scraping
	private short numGames;
	private String[][] teamNames;
	private int[][] teamGoals;
	private String[] gameTime;
	
	// Miscellaneous data
	private Calendar date = Calendar.getInstance();  // Current date in local timezone
	private final static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private static final String website = "http://www.sportsnet.ca/hockey/nhl/scores/";
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");

	public MainWindow(){
		
		// --- UI Creation --- //
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1){e1.printStackTrace();}
		pnlMain = new JPanel();
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlMain.setBackground(Color.WHITE);
		
		// Top Panel //
		pnlTop = new JPanel();
		pnlTop.setLayout(new BorderLayout());
		pnlTop.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlTop.setBackground(Color.WHITE);
		lblRefresh = new JLabel();
		lblRefresh.setIcon(new ImageIcon("Resources/refresh.png"));
		lblRefresh.setBorder(BorderFactory.createEmptyBorder(3,3,3,0));
		lblRefresh.addMouseListener(this);
		lblDate = new JLabel(FormatDate(date), JLabel.CENTER);
		lblDate.setFont(new Font("Arial", Font.BOLD, 14));
		lblClose = new JLabel();
		lblClose.setIcon(new ImageIcon("Resources/close.png"));
		lblClose.setBorder(BorderFactory.createEmptyBorder(3,0,3,3));
		lblClose.addMouseListener(this);
		pnlTop.add(lblRefresh, BorderLayout.WEST);
		pnlTop.add(lblDate);
		pnlTop.add(lblClose, BorderLayout.EAST);
		pnlListGames = new JPanel();
		
		// Initial Scraping / Updating
		Scrape();
		UpdateUI(true);
		
		pnlMain.add(pnlTop, BorderLayout.NORTH);
		pnlMain.add(pnlScroll);
		
		// Starting up the Window
		this.add(pnlMain);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300, 350);
		this.setIconImage(winIcon.getImage());
		this.setUndecorated(true);
		this.pack();
		this.setLocation(screenSize.width - this.getWidth() - 5, screenSize.height - this.getHeight() - 5);
		this.setVisible(true);
	}
	
	// Listener responses for refresh and close
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == lblRefresh){
			Scrape();
			UpdateUI(false);
		}else if (e.getSource() == lblClose){
			this.dispose();
			System.exit(0);
		}
	}
	
	// Scrapes www.nhl.com for the day's games and their status
	private void Scrape(){
		try {
			Document doc = Jsoup.connect(website).get();			
			// Navigates to proper container
			Elements teamCities = doc.getElementsByClass("scores-team-city");
			Elements teamName = doc.getElementsByClass("scores-team-name");
			Elements teamGoals = doc.getElementsByClass("team-score-container");
			Elements gameTimes = doc.select("td");	
				
			// Add info to arrays
			this.numGames = (short)(teamCities.size() / 2);
			this.teamNames = new String[numGames][2];
			this.teamGoals = new int[numGames][2];
			this.gameTime = new String[numGames];
			
			for (int i = 0; i < numGames; i++){
							
				// Team Names and Goals
				this.teamNames[i][0] = teamCities.get(2 * i).text() + " " + teamName.get(2 * i).text();
				this.teamNames[i][1] = teamCities.get(2 * i + 1).text() + " " + teamName.get(2 * i + 1).text();
				this.teamGoals[i][0] = Integer.parseInt(teamGoals.get(2 * i).text());
				this.teamGoals[i][1] = Integer.parseInt(teamGoals.get(2 * i + 1).text());
				
				// Game Time
				this.gameTime[i] = gameTimes.get(i).text();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error: Unable to retrieve data from www.sportsnet.ca. Please ensure you have the latest version of NHL Scraper.");
		}
	}
	
	// Updates UI elements with the latest scraped data
	private void UpdateUI(boolean startup){
		pnlGames = new GamePanel[numGames];
		pnlListGames.removeAll();
		pnlListGames.setLayout(new GridLayout(numGames, 1, 0, 0));
		for (int i = 0; i < numGames; i++){
			pnlGames[i] = new GamePanel();
			pnlGames[i].UpdatePanel(teamNames[i], teamGoals[i], gameTime[i]);
			pnlListGames.add(pnlGames[i]);
		}
		pnlScroll = new JScrollPane(pnlListGames, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pnlScroll.setPreferredSize(new Dimension(275,325));
		if (!startup) {
			pnlMain.remove(1);
			pnlMain.add(pnlScroll);
			this.validate();
			this.repaint();
		}
	}
	
	// Formats the retrieved Calendar instance into a string for the application
	private String FormatDate(Calendar date){
		String strDate = months[date.get(Calendar.MONTH)] + " " + date.get(Calendar.DAY_OF_MONTH) + ", " + date.get(Calendar.YEAR);
		return strDate;
	}
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	
	public static void main(String[] args){
		new MainWindow();
	}
	
}
