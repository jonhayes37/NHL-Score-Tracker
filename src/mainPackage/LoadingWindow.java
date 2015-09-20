package mainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoadingWindow {
	
	private JDialog dlg;
	private JPanel pnlMain;
	private JLabel lblInfo;
	private boolean looping = true;
	
	public LoadingWindow(JFrame parent){
		dlg = new JDialog(parent);
		lblInfo = new JLabel();
		lblInfo.setPreferredSize(new Dimension(128,128));
		lblInfo.setOpaque(true);
		pnlMain = new JPanel();
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		pnlMain.setBackground(Color.WHITE);
		pnlMain.add(lblInfo);
		
		dlg.add(pnlMain);
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dlg.setModalityType(ModalityType.MODELESS);
		dlg.setUndecorated(true);
		dlg.pack();
		dlg.setLocationRelativeTo(null);
		dlg.setVisible(true);
		
		Loop();
	}
	
	private void Loop(){
		new Thread(new Runnable(){
			public void run(){
				int curAngle = 0;
				while (looping){
					// Getting new photo
					BufferedImage img = null;
					try{
						img = ImageIO.read(new File("Resources/" + curAngle + ".png"));
					}catch (IOException e){ e.printStackTrace(); }
					Image newImg = img.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
					lblInfo.setIcon(new ImageIcon(newImg));
					
					// Waiting and updating angle
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) { e.printStackTrace(); }
					if (curAngle == 345){
						curAngle = 0;
					}else{
						curAngle += 15;
					}
				}
			}
		}).start();
	}
	
	public void EndLoop(){
		this.looping = false;
		this.dlg.dispose();
	}
}
