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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
	private JPanel[] pnlCmb = new JPanel[2];
	private JCheckBox chkOnTop;
	private JCheckBox chkFlash;
	private JCheckBox chkIsBorderless;
	private JLabel lblCancel;
	private JLabel lblSave;
	private JLabel lblRefresh;
	private JLabel lblMinShown;
	private JComboBox<String> cmbTimes;
	private JComboBox<String> cmbMinShown;
	
	// Miscellaneous Data
	private static final String[] gameNames = { "1 game", "2 games", "3 games", "4 games", "5 games", "6 games", "7 games" };
	private static final String[] timeNames = {"5 seconds", "15 seconds", "30 seconds", "1 minute", "5 minutes", "15 minutes", "30 minutes", "1 hour"};
	private static final int[] times = {5, 15, 30, 60, 300, 900, 1800, 3600};
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 12);
	public String result;
	
	public SettingsWindow(Point location){
		
		ScraperSettings settings = new ScraperSettings();
		settings.Load();
		
		// --- UI Creation --- //
		pnlMain = new JPanel();
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		pnlMain.setBackground(Color.WHITE);
		
		// Checkbox Panel //
		pnlChk = new JPanel();
		pnlChk.setLayout(new GridLayout(3, 1, 0, 0));
		pnlChk.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		pnlChk.setBackground(Color.WHITE);
		chkOnTop = new JCheckBox("Tracker window is always on top");
		chkOnTop.setSelected(settings.getOnTop());
		chkOnTop.setBackground(Color.WHITE);
		chkOnTop.setFont(DEFAULT_FONT);
		chkFlash = new JCheckBox("Animate game when a goal is scored");
		chkFlash.setSelected(settings.getFlash());
		chkFlash.setBackground(Color.WHITE);
		chkFlash.setFont(DEFAULT_FONT);
		chkIsBorderless = new JCheckBox("Borderless window");
		chkIsBorderless.setSelected(settings.getIsBorderless());
		chkIsBorderless.setBackground(Color.WHITE);
		chkIsBorderless.setFont(DEFAULT_FONT);
		pnlChk.add(chkOnTop);
		pnlChk.add(chkFlash);
		pnlChk.add(chkIsBorderless);
		
		// Central Panels //
		pnlCentre = new JPanel();
		pnlCentre.setLayout(new BorderLayout());
		pnlCentre.setBackground(Color.WHITE);
		pnlCentre.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		pnlRefresh = new JPanel();
		pnlRefresh.setLayout(new GridLayout(2,1,0,10));
		pnlRefresh.setBackground(Color.WHITE);
		pnlRefresh.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
		
		// Combobox Panels //
		for (int i = 0; i < 2; i++) {
			pnlCmb[i] = new JPanel();
			pnlCmb[i].setBackground(Color.WHITE);
			pnlCmb[i].setLayout(new BorderLayout());
		}
		
		lblMinShown = new JLabel("Minimum # of games shown:");
		lblMinShown.setBackground(Color.WHITE);
		lblMinShown.setFont(DEFAULT_FONT);
		lblMinShown.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
		cmbMinShown = new JComboBox<String>(gameNames);
		cmbMinShown.setSelectedIndex(settings.getMinGamesShown() - 1);
		cmbMinShown.setBackground(Color.WHITE);		
		cmbMinShown.setFont(DEFAULT_FONT);
		cmbMinShown.setPreferredSize(new Dimension(90, 22));
		pnlCmb[0].add(lblMinShown);
		pnlCmb[0].add(cmbMinShown, BorderLayout.EAST);
		
		lblRefresh = new JLabel("Refresh every:");
		lblRefresh.setBackground(Color.WHITE);
		lblRefresh.setFont(DEFAULT_FONT);
		cmbTimes = new JComboBox<String>(timeNames);
		cmbTimes.setSelectedIndex(FindRefreshIndex(settings.getRefreshFrequency()));
		cmbTimes.setBackground(Color.WHITE);
		cmbTimes.setFont(DEFAULT_FONT);
		cmbTimes.setPreferredSize(new Dimension(90, 22));
		pnlCmb[1].add(lblRefresh);
		pnlCmb[1].add(cmbTimes, BorderLayout.EAST);
		pnlRefresh.add(pnlCmb[0]);
		pnlRefresh.add(pnlCmb[1]);
		
		// Button Panel //
		pnlButton = new JPanel();
		pnlButton.setLayout(new GridLayout(1,2,5,0));
		pnlButton.setBackground(Color.WHITE);
		pnlButton.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		lblCancel = new JLabel("Cancel", JLabel.CENTER);
		lblCancel.setOpaque(true);
		lblCancel.setBackground(Color.LIGHT_GRAY);
		lblCancel.setForeground(Color.WHITE);
		lblCancel.setFont(new Font("Arial", Font.BOLD, 16));
		lblCancel.addMouseListener(this);
		lblCancel.setPreferredSize(new Dimension(100,25));
		lblSave = new JLabel("Save", JLabel.CENTER);
		lblSave.setOpaque(true);
		lblSave.setBackground(new Color(62,179,113));
		lblSave.setForeground(Color.WHITE);
		lblSave.setFont(new Font("Arial", Font.BOLD, 16));
		lblSave.addMouseListener(this);
		lblSave.setPreferredSize(new Dimension(100,25));
		pnlButton.add(lblCancel);
		pnlButton.add(lblSave);
		
		pnlCentre.add(pnlRefresh, BorderLayout.NORTH);
		pnlCentre.add(pnlButton, BorderLayout.SOUTH);
		pnlMain.add(pnlChk, BorderLayout.NORTH);
		pnlMain.add(pnlCentre);
		
		this.add(pnlMain);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(200, 150);
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
			ScraperSettings newSettings = new ScraperSettings(chkOnTop.isSelected(), chkFlash.isSelected(), chkIsBorderless.isSelected(), times[cmbTimes.getSelectedIndex()], cmbMinShown.getSelectedIndex() + 1);
			newSettings.Save();
			result = "Save";
			this.dispose();
		}
	}
	
	// Finds the index of accepted refresh times in the times array
	private int FindRefreshIndex(int seconds) {
		for (int i = 0; i < times.length; i++) {
			if (times[i] == seconds) {
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
