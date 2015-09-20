package mainPackage;
/*
 * The SettingsWindow class is a dialog window in which
 * the user can edit settings for the scraper.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SettingsWindow extends JDialog implements MouseListener{
	
	// UI Elements
	private static final long serialVersionUID = -145581092843805827L;
	private JPanel pnlMain;
	private JPanel pnlChk;
	private JPanel pnlCentre;
	private JPanel pnlRefresh;
	private JPanel pnlButton;
	private JPanel[] pnlCmb = new JPanel[3];
	private JCheckBox chkOnTop;
	private JCheckBox chkFlash;
	private JCheckBox chkIsBorderless;
	private JCheckBox chkAutoUpdate;
	private JLabel lblCancel;
	private JLabel lblSave;
	private JLabel lblRefresh;
	private JLabel lblMinShown;
	private JLabel lblTheme;
	private JLabel lblManageFavs;
	private JComboBox<String> cmbTimes;
	private JComboBox<String> cmbMinShown;
	private JComboBox<String> cmbThemes;
	private Theme usedTheme = new Theme();
	
	// Miscellaneous Data
	private static final String[] gameNames = { "1 game", "2 games", "3 games", "4 games", "5 games", "6 games", "7 games" };
	private static final String[] timeNames = {"5 seconds", "15 seconds", "30 seconds", "1 minute", "5 minutes", "15 minutes", "30 minutes", "1 hour"};
	private String[] themeNames;
	private static final int[] times = {5, 15, 30, 60, 300, 900, 1800, 3600};
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 12);
	private ArrayList<String> favTeams = new ArrayList<String>();
	public String result;
	final String DIRECTORY = System.getProperty("user.dir") + "\\Themes\\";
	final File fileInstance = new File(DIRECTORY);
	final String[] themes = fileInstance.list();
	
	
	public SettingsWindow(Point location){
		
		// Loading in settings and the available themes
		ScraperSettings settings = new ScraperSettings();
		settings.Load();
		this.usedTheme = settings.getTheme();
		this.favTeams = settings.getFavTeams();
		themeNames = new String[themes.length + 1];
		for (int i = 0; i < themes.length; i++) {
			themeNames[i] = themes[i].substring(0, themes[i].length() - 4);
		}
		themeNames[themeNames.length - 1] = "Add custom theme...";
		
		// --- UI Creation --- //
		pnlMain = new JPanel();
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		pnlMain.setBackground(usedTheme.getSecondaryColor());
		
		// Checkbox Panel //
		pnlChk = new JPanel();
		pnlChk.setLayout(new GridLayout(4, 1, 0, 0));
		pnlChk.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		pnlChk.setBackground(usedTheme.getSecondaryColor());
		chkOnTop = new JCheckBox("Tracker window is always on top");
		chkOnTop.setSelected(settings.getOnTop());
		chkOnTop.setBackground(usedTheme.getSecondaryColor());
		chkOnTop.setFont(DEFAULT_FONT);
		chkOnTop.setOpaque(true);
		chkOnTop.setForeground(usedTheme.getPrimaryFontColor());
		chkFlash = new JCheckBox("Animate game when a goal is scored");
		chkFlash.setSelected(settings.getFlash());
		chkFlash.setBackground(usedTheme.getSecondaryColor());
		chkFlash.setFont(DEFAULT_FONT);
		chkFlash.setOpaque(true);
		chkFlash.setForeground(usedTheme.getPrimaryFontColor());
		chkIsBorderless = new JCheckBox("Borderless window");
		chkIsBorderless.setSelected(settings.getIsBorderless());
		chkIsBorderless.setBackground(usedTheme.getSecondaryColor());
		chkIsBorderless.setFont(DEFAULT_FONT);
		chkIsBorderless.setOpaque(true);
		chkIsBorderless.setForeground(usedTheme.getPrimaryFontColor());
		chkAutoUpdate = new JCheckBox("Automatically check for updates");
		chkAutoUpdate.setSelected(settings.getAutoUpdate());
		chkAutoUpdate.setBackground(usedTheme.getSecondaryColor());
		chkAutoUpdate.setFont(DEFAULT_FONT);
		chkAutoUpdate.setOpaque(true);
		chkAutoUpdate.setForeground(usedTheme.getPrimaryFontColor());
		pnlChk.add(chkOnTop);
		pnlChk.add(chkFlash);
		pnlChk.add(chkIsBorderless);
		pnlChk.add(chkAutoUpdate);
		
		// Central Panels //
		pnlCentre = new JPanel();
		pnlCentre.setLayout(new BorderLayout());
		pnlCentre.setBackground(usedTheme.getSecondaryColor());
		pnlCentre.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		pnlRefresh = new JPanel();
		pnlRefresh.setLayout(new GridLayout(4,1,0,10));
		pnlRefresh.setBackground(usedTheme.getSecondaryColor());
		pnlRefresh.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
		
		// Combobox Panels //
		for (int i = 0; i < 3; i++) {
			pnlCmb[i] = new JPanel();
			pnlCmb[i].setBackground(Color.WHITE);
			pnlCmb[i].setLayout(new BorderLayout());
		}
		
		lblMinShown = new JLabel("Minimum # of games shown:");
		lblMinShown.setBackground(usedTheme.getSecondaryColor());
		lblMinShown.setFont(DEFAULT_FONT);
		lblMinShown.setOpaque(true);
		lblMinShown.setForeground(usedTheme.getPrimaryFontColor());
		lblMinShown.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
		cmbMinShown = new JComboBox<String>(gameNames);
		cmbMinShown.setSelectedIndex(settings.getMinGamesShown() - 1);
		cmbMinShown.setBackground(usedTheme.getSecondaryColor());		
		cmbMinShown.setFont(DEFAULT_FONT);
		cmbMinShown.setPreferredSize(new Dimension(90, 22));
		cmbMinShown.setOpaque(true);
		cmbMinShown.setForeground(usedTheme.getQuarternaryFontColor());
		pnlCmb[0].add(lblMinShown);
		pnlCmb[0].add(cmbMinShown, BorderLayout.EAST);
		
		lblRefresh = new JLabel("Refresh every:");
		lblRefresh.setBackground(usedTheme.getSecondaryColor());
		lblRefresh.setFont(DEFAULT_FONT);
		lblRefresh.setOpaque(true);
		lblRefresh.setForeground(usedTheme.getPrimaryFontColor());
		cmbTimes = new JComboBox<String>(timeNames);
		cmbTimes.setSelectedIndex(FindRefreshIndex(settings.getRefreshFrequency()));
		cmbTimes.setBackground(usedTheme.getSecondaryColor());
		cmbTimes.setFont(DEFAULT_FONT);
		cmbTimes.setPreferredSize(new Dimension(90, 22));
		cmbTimes.setOpaque(true);
		cmbTimes.setForeground(usedTheme.getQuarternaryFontColor());
		pnlCmb[1].add(lblRefresh);
		pnlCmb[1].add(cmbTimes, BorderLayout.EAST);
		
		lblTheme = new JLabel("Current Theme:");
		lblTheme.setBackground(usedTheme.getSecondaryColor());
		lblTheme.setFont(DEFAULT_FONT);
		lblTheme.setOpaque(true);
		lblTheme.setForeground(usedTheme.getPrimaryFontColor());
		cmbThemes = new JComboBox<String>(themeNames);
		cmbThemes.setSelectedIndex(FindThemeIndex(this.usedTheme.getName()));
		cmbThemes.setBackground(usedTheme.getSecondaryColor());
		cmbThemes.setFont(DEFAULT_FONT);
		cmbThemes.setOpaque(true);
		cmbThemes.setForeground(usedTheme.getQuarternaryFontColor());
		cmbThemes.setPreferredSize(new Dimension(130, 22));
		cmbThemes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        if (cmbThemes.getSelectedIndex() == (cmbThemes.getItemCount() - 1)) {
		        	new ThemeMakerWindow();
		        	
		        }
		    }
		});
		pnlCmb[2].add(lblTheme);
		pnlCmb[2].add(cmbThemes, BorderLayout.EAST);
		lblManageFavs = new JLabel("Manage Favourite Teams", JLabel.CENTER);
		lblManageFavs.setOpaque(true);
		lblManageFavs.setBackground(usedTheme.getQuarternaryColor());
		lblManageFavs.setForeground(Color.WHITE);
		lblManageFavs.setFont(new Font("Arial", Font.BOLD, 16));
		lblManageFavs.addMouseListener(this);
		lblManageFavs.setPreferredSize(new Dimension(110,25));
		pnlRefresh.add(pnlCmb[0]);
		pnlRefresh.add(pnlCmb[1]);
		pnlRefresh.add(pnlCmb[2]);
		pnlRefresh.add(lblManageFavs);
		
		// Button Panel //
		pnlButton = new JPanel();
		pnlButton.setLayout(new GridLayout(1,2,5,0));
		pnlButton.setBackground(usedTheme.getSecondaryColor());
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
		
		pnlCentre.add(pnlRefresh, BorderLayout.NORTH);
		pnlCentre.add(pnlButton, BorderLayout.SOUTH);
		pnlMain.add(pnlChk, BorderLayout.NORTH);
		pnlMain.add(pnlCentre);
		
		this.add(pnlMain);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(winIcon.getImage());
		this.setUndecorated(true);
		this.pack();
		this.setLocation(location.x - this.getWidth() - 3, location.y);
		this.setVisible(true);
	}

	// Listeners for the buttons
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == lblCancel) {   // Cancel
			result = "Cancel";
			this.dispose();
		}else if (e.getSource() == lblSave) {   // Save
			ScraperSettings newSettings = new ScraperSettings(chkOnTop.isSelected(), chkFlash.isSelected(), chkIsBorderless.isSelected(), chkAutoUpdate.isSelected(), times[cmbTimes.getSelectedIndex()], cmbMinShown.getSelectedIndex() + 1, this.favTeams, themeNames[cmbThemes.getSelectedIndex()]);
			newSettings.Save();
			result = "Save";
			this.dispose();
		}else if (e.getSource() == lblManageFavs){	// Manage Favourite Teams
			ManageTeamsWindow mtw = new ManageTeamsWindow(this.favTeams, this.usedTheme, this.getLocation());
			if (mtw.success){
				this.favTeams = mtw.newTeams;
			}
		}
	}
	
	// Finds the index of the refresh time in the times array
	private int FindRefreshIndex(int seconds) {
		for (int i = 0; i < times.length; i++) {
			if (times[i] == seconds) {
				return i;
			}
		}
		return 0;
	}
	
	// Finds the index of the theme in the themes array
	private int FindThemeIndex(String name) {
		for (int i = 0; i < themeNames.length; i++) {
			if (themeNames[i].equals(name)) {
				return i;
			}
		}
		return 0;
	}
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	
}
