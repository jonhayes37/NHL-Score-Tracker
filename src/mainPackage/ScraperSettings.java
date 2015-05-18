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
	private Theme theme;
	
	public ScraperSettings(){
		this.alwaysOnTop = false;
		this.flash = true;
		this.isBorderless = true;
		this.refreshFrequency = 60;
		this.minGamesShown = 3;
		this.theme = new Theme();
	}
	
	public ScraperSettings(boolean onTop, boolean canFlash, boolean deco, int frequency, int minShown, String th){
		this.alwaysOnTop = onTop;
		this.flash = canFlash;
		this.isBorderless = deco;
		this.refreshFrequency = frequency;
		this.minGamesShown = minShown;
		this.theme = new Theme(th);
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
	         this.theme = newSettings.theme;
	      }catch(IOException | ClassNotFoundException i){ i.printStackTrace(); }
		newSettings = null;
	}
	
	// Returns true if both settings are equal
	public boolean isEqual(ScraperSettings s2) {
		return this.alwaysOnTop == s2.alwaysOnTop && this.flash == s2.flash && this.isBorderless == s2.isBorderless && this.refreshFrequency == s2.refreshFrequency && this.minGamesShown == s2.minGamesShown && this.theme.getName().equals(s2.theme.getName());
	}
	
	// Getters
	public boolean getOnTop(){ return alwaysOnTop; }
	public boolean getFlash(){ return flash; }
	public boolean getIsBorderless(){ return isBorderless; }
	public int getRefreshFrequency(){ return refreshFrequency; }
	public int getMinGamesShown(){ return minGamesShown; }
	public Theme getTheme(){ return this.theme; }
}
