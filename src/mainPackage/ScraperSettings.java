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
	private boolean isBorderless;
	private int refreshFrequency;
	private int minGamesShown;
	
	public ScraperSettings(){
		alwaysOnTop = false;
		flash = true;
		isBorderless = true;
		refreshFrequency = 60;
		minGamesShown = 3;
	}
	
	public ScraperSettings(boolean onTop, boolean canFlash, boolean deco, int frequency, int minShown){
		alwaysOnTop = onTop;
		flash = canFlash;
		isBorderless = deco;
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
	         this.isBorderless = newSettings.isBorderless;
	         this.refreshFrequency = newSettings.refreshFrequency;
	         this.minGamesShown = newSettings.minGamesShown;
	      }catch(IOException | ClassNotFoundException i){ i.printStackTrace(); }
		newSettings = null;
	}
	
	// Returns true if both settings are equal
	public boolean isEqual(ScraperSettings s2) {
		return this.alwaysOnTop == s2.alwaysOnTop && this.flash == s2.flash && this.isBorderless == s2.isBorderless && this.refreshFrequency == s2.refreshFrequency && this.minGamesShown == s2.minGamesShown;
	}
	
	// Getters
	public boolean getOnTop(){ return alwaysOnTop; }
	public boolean getFlash(){ return flash; }
	public boolean getIsBorderless(){ return isBorderless; }
	public int getRefreshFrequency(){ return refreshFrequency; }
	public int getMinGamesShown(){ return minGamesShown; }
}
