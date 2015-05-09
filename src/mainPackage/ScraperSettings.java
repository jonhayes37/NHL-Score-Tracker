package mainPackage;
/*
 * The ScraperSettings class encapsulates the program's settings, 
 * and handles serialization to the file settings.stg
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ScraperSettings implements Serializable{

	private static final long serialVersionUID = -6448475325537972944L;
	private boolean alwaysOnTop;
	private boolean flash;
	private int refreshFrequency;
	private int minGamesShown;
	
	public ScraperSettings(){
		alwaysOnTop = false;
		refreshFrequency = 60;
		minGamesShown = 3;
		flash = true;
	}
	
	public ScraperSettings(boolean onTop, boolean canFlash, int frequency, int minShown){
		alwaysOnTop = onTop;
		flash = canFlash;
		refreshFrequency = frequency;
		minGamesShown = minShown;
	}
	
	// Saves current settings to file
	public void Save(){
		try{
	         FileOutputStream fileOut = new FileOutputStream("Resources/settings.stg");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(this);
	         out.close();
	         fileOut.close();
	      }catch(IOException i){ i.printStackTrace(); }
	}
	
	// Loads file settings into the class 
	public void Load(){
		ScraperSettings newSettings;
		try{
	         FileInputStream fileIn = new FileInputStream("Resources/settings.stg");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         newSettings = (ScraperSettings) in.readObject();
	         in.close();
	         fileIn.close();
	         this.alwaysOnTop = newSettings.alwaysOnTop;
	         this.flash = newSettings.flash;
	         this.refreshFrequency = newSettings.refreshFrequency;
	         this.minGamesShown = newSettings.minGamesShown;
	      }catch(IOException | ClassNotFoundException i){ i.printStackTrace(); }
		newSettings = null;
	}
	
	// Getters
	public boolean getOnTop(){ return alwaysOnTop; }
	public int getRefreshFrequency(){ return refreshFrequency; }
	public int getMinGamesShown(){ return minGamesShown; }
	public boolean getFlash(){ return flash; }
}
