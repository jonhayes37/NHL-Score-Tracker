package mainPackage;
/*
 * The SettingsWindow class is a dialog window in which
 * the user can edit settings for the scraper.
 */
import java.awt.BorderLayout;
import java.awt.Color;
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
	private JPanel pnlCentre;
	private JPanel pnlRefresh;
	private JPanel pnlButton;
	private JCheckBox chkOnTop;
	private JLabel lblCancel;
	private JLabel lblSave;
	private JLabel lblRefresh;
	private JComboBox<String> cmbTimes;
	
	// Miscellaneous Data
	private static final String[] timeNames = {"15 seconds", "30 seconds", "1 minute", "5 minutes", "15 minutes", "30 minutes", "1 hour", "2 hours"};
	private static final int[] times = {15, 30, 60, 300, 900, 1800, 3600, 7200};
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 12);
	
	public SettingsWindow(Point location){
		
		ScraperSettings settings = new ScraperSettings();
		settings.Load();
		
		// --- UI Creation --- //
		pnlMain = new JPanel();
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		pnlMain.setBackground(Color.WHITE);
		chkOnTop = new JCheckBox("Scraper is always on top");
		chkOnTop.setSelected(settings.getOnTop());
		chkOnTop.setBackground(Color.WHITE);
		chkOnTop.setFont(DEFAULT_FONT);
		chkOnTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		
		pnlCentre = new JPanel();
		pnlCentre.setLayout(new BorderLayout());
		pnlCentre.setBackground(Color.WHITE);
		pnlCentre.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		pnlRefresh = new JPanel();
		pnlRefresh.setLayout(new GridLayout(1,2,5,0));
		pnlRefresh.setBackground(Color.WHITE);
		pnlRefresh.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
		lblRefresh = new JLabel("Refresh every:");
		lblRefresh.setBackground(Color.WHITE);
		lblRefresh.setFont(DEFAULT_FONT);
		cmbTimes = new JComboBox<String>(timeNames);
		cmbTimes.setSelectedIndex(FindRefreshIndex(settings.getRefreshFrequency()));
		cmbTimes.setBackground(Color.WHITE);
		cmbTimes.setFont(DEFAULT_FONT);
		pnlRefresh.add(lblRefresh);
		pnlRefresh.add(cmbTimes);
		
		pnlButton = new JPanel();
		pnlButton.setLayout(new GridLayout(1,2,5,0));
		pnlButton.setBackground(Color.WHITE);
		pnlButton.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		lblCancel = new JLabel("Cancel", JLabel.CENTER);
		lblCancel.setOpaque(true);
		lblCancel.setBackground(Color.LIGHT_GRAY);
		lblCancel.setForeground(Color.WHITE);
		lblCancel.setFont(new Font("Arial", Font.BOLD, 14));
		lblCancel.addMouseListener(this);
		lblSave = new JLabel("Save", JLabel.CENTER);
		lblSave.setOpaque(true);
		lblSave.setBackground(new Color(62,179,113));
		lblSave.setForeground(Color.WHITE);
		lblSave.setFont(new Font("Arial", Font.BOLD, 14));
		lblSave.addMouseListener(this);
		pnlButton.add(lblCancel);
		pnlButton.add(lblSave);
		
		pnlCentre.add(pnlRefresh, BorderLayout.NORTH);
		pnlCentre.add(pnlButton, BorderLayout.SOUTH);
		pnlMain.add(chkOnTop, BorderLayout.NORTH);
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

	// Listeners for Cancel / Save
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == lblSave) {
			ScraperSettings newSettings = new ScraperSettings(chkOnTop.isSelected(), times[cmbTimes.getSelectedIndex()]);
			newSettings.Save();
			this.dispose();
		}else if (e.getSource() == lblCancel) {
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
		return -1;
	}
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}


	
}
