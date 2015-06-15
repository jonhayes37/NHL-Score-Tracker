package mainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class ThemeMakerWindow extends JDialog implements ActionListener{

	private static final long serialVersionUID = 419023322926816033L;
	private JPanel pnlMain;
	private JPanel pnlTheme;
	private JPanel pnlTopMain;
	private JPanel pnlColorMain;
	private JPanel pnlColorFormat;
	private JPanel pnlColors;
	private JPanel pnlButtons;
	private JPanel[] pnlIndColors = new JPanel[9];
	private ButtonGroup btnColorFormat;
	private JRadioButton[] btnRadios = new JRadioButton[2];
	private JLabel lblName;
	private JLabel[] lblColors = new JLabel[] {new JLabel("Primary Colour:", JLabel.CENTER), new JLabel("Secondary Colour:", JLabel.CENTER), new JLabel("Tertiary Colour:", JLabel.CENTER), new JLabel("Quarternary Colour:", JLabel.CENTER), new JLabel("Quintiary Colour:", JLabel.CENTER), new JLabel("Primary Font Colour:", JLabel.CENTER), new JLabel("Secondary Font Colour:", JLabel.CENTER), new JLabel("Tertiary Font Colour:", JLabel.CENTER), new JLabel("Quarternary Font Colour:", JLabel.CENTER)};
	private JTextArea[] txtColors = new JTextArea[9];
	private JTextArea txtName;
	private JButton btnHelp;
	private JButton btnSave;
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon-16x16.png");
	
	public ThemeMakerWindow() {

		pnlMain = new JPanel();
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		pnlTopMain = new JPanel();
		pnlTopMain.setLayout(new BorderLayout());
		pnlTheme = new JPanel();
		pnlTheme.setLayout(new BorderLayout());
		pnlTheme.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		lblName = new JLabel("Theme Name");
		txtName = new JTextArea();
		txtName.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		txtName.setPreferredSize(new Dimension(120,20));
		pnlTheme.add(lblName);
		pnlTheme.add(txtName, BorderLayout.EAST);
		
		pnlColorMain = new JPanel();
		pnlColorMain.setLayout(new BorderLayout());
		pnlColorMain.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
		
		TitledBorder title = new TitledBorder("Color Scheme");
		title.setTitleJustification(TitledBorder.LEFT);
		pnlColorFormat = new JPanel();
		pnlColorFormat.setLayout(new GridLayout(1,2,5,0));
		pnlColorFormat.setBorder(title);
		btnRadios[0] = new JRadioButton("RGB (104,35,79)");
		btnRadios[1] = new JRadioButton("Hex (548FCD)");
		btnColorFormat = new ButtonGroup();
		btnColorFormat.add(btnRadios[0]);
		btnColorFormat.add(btnRadios[1]);
		pnlColorFormat.add(btnRadios[0]);
		pnlColorFormat.add(btnRadios[1]);
		
		pnlColors = new JPanel();
		pnlColors.setLayout(new GridLayout(9,1,5,5));
		pnlColors.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		for (int i = 0; i < 9; i++) {
			pnlIndColors[i] = new JPanel();
			pnlIndColors[i].setLayout(new BorderLayout());
			pnlIndColors[i].add(lblColors[i]);
			txtColors[i] = new JTextArea(1,10);
			txtColors[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
			pnlIndColors[i].add(txtColors[i], BorderLayout.EAST);
			pnlColors.add(pnlIndColors[i]);
		}
		pnlColorMain.add(pnlColorFormat, BorderLayout.NORTH);
		pnlColorMain.add(pnlColors);
		pnlTopMain.add(pnlTheme, BorderLayout.NORTH);
		pnlTopMain.add(pnlColorMain);
		
		pnlButtons = new JPanel();
		pnlButtons.setLayout(new GridLayout(1,2,5,0));
		btnHelp = new JButton("Help...");
		btnHelp.addActionListener(this);
		btnSave = new JButton("Save Theme");
		btnSave.addActionListener(this);
		pnlButtons.add(btnHelp);
		pnlButtons.add(btnSave);
		
		pnlMain.add(pnlTopMain);
		pnlMain.add(pnlButtons, BorderLayout.SOUTH);
		
		this.add(pnlMain);
		this.setTitle("Custom Theme Maker");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(250, 500);
		this.setIconImage(winIcon.getImage());
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSave) {
			if (btnRadios[0].isSelected()) { // RGB is selected
				// Processing text into integers
				int[][] rgbNums = new int[9][3];
				for (int i = 0; i < 9; i++){
					String[] tempNums = txtColors[i].getText().split(",");
					for (int j = 0; j < 3; j++) {
						rgbNums[i][j] = Integer.parseInt(tempNums[j]);
					}
				}
				// Making the colors
				Color[] themeColors = new Color[9];
				for (int i = 0; i < 9; i++) {
					themeColors[i] = new Color(rgbNums[i][0], rgbNums[i][1], rgbNums[i][2]);
				}
				Theme newTheme = new Theme(txtName.getText(), themeColors[0], themeColors[1], themeColors[2], themeColors[3], themeColors[4], themeColors[5], themeColors[6], themeColors[7], themeColors[8]);
				newTheme.Save();
			}else{ // Hex is selected
				String[] hexColors = new String[9];
				Color[] themeColors = new Color[9];
				for (int i = 0; i < 9; i++) {
					hexColors[i] = txtColors[i].getText();
					themeColors[i] = Color.decode(hexColors[i]);
				}
				Theme newTheme = new Theme(txtName.getText(), themeColors[0], themeColors[1], themeColors[2], themeColors[3], themeColors[4], themeColors[5], themeColors[6], themeColors[7], themeColors[8]);
				newTheme.Save();
			}
			this.dispose();
		}else if (e.getSource() == btnHelp) {
			JOptionPane.showMessageDialog(this, "<html><div style=\"text-align: center\">The five panel colours and 4 font colours available to change<br>can be combined"
					+ " to make any theme you can imagine!<br><strong>NOTE: A restart is required before the custom theme can be applied!</strong></div><br><br>"
					+ "The UI background and font colour is changed by each colour as follows:<br>"
					+ "<strong>Primary Colour:</strong> Title Panel in main UI<br><strong>Secondary Colour:</strong> Individual Game Panels and Settings Window<br><strong>Tertiary Colour:</strong> Cancel Button<br><strong>Quarternary Colour:</strong> Save Button<br>"
					+ "<strong>Quintiary Colour:</strong> Game Panel Border<br><br><strong>Primary Font Colour:</strong> Settings Text and Team Names<br><strong>Secondary Font Colour:</strong> Game Time<br><strong>Tertiary Font Colour:</strong> Date in Title Panel<br>"
					+ "<strong>Quarternary Font Colour:</strong> Dropdown Menu Text in Settings</html>", "Custom Theme Help", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
}
