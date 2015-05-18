package mainPackage;

/*
 * The theme class represents the changes made 
 * to the program upon implementation of a theme.
 * Themes do not change the tracker's functionality.
 * The theme alters the program's look as follows:
 * 
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

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Theme implements Serializable{

	private static final long serialVersionUID = 194762704985197609L;
	private String name;
	private Color primaryColor;
	private Color secondaryColor;
	private Color tertiaryColor;
	private Color quarternaryColor;
	private Color quintiaryColor;
	private Color primaryFontColor;
	private Color secondaryFontColor;
	private Color tertiaryFontColor;
	private Color quarternaryFontColor;
	
	// Default theme
	public Theme() {
		this.name = "Material";
		this.primaryColor = Color.WHITE;
		this.secondaryColor = Color.WHITE;
		this.tertiaryColor = Color.LIGHT_GRAY;
		this.quarternaryColor = new Color(62,179,113);
		this.quintiaryColor = Color.LIGHT_GRAY;
		this.primaryFontColor = Color.BLACK;
		this.secondaryFontColor = Color.GRAY;
		this.tertiaryFontColor = Color.BLACK;
		this.quarternaryFontColor = Color.BLACK;
	}
	
	// Load theme by name
	public Theme(String themeName) {
		Theme newTheme = new Theme();
		newTheme.Load(themeName);
		this.name = newTheme.name;
        this.primaryColor = newTheme.primaryColor;
        this.secondaryColor = newTheme.secondaryColor;
        this.tertiaryColor = newTheme.tertiaryColor;
        this.quarternaryColor = newTheme.quarternaryColor;
        this.quintiaryColor = newTheme.quintiaryColor;
        this.primaryFontColor = newTheme.primaryFontColor;
        this.secondaryFontColor = newTheme.secondaryFontColor;
        this.tertiaryFontColor = newTheme.tertiaryFontColor;
        this.quarternaryFontColor = newTheme.quarternaryFontColor;
	}
	
	// Custom theme
	public Theme(String n, Color pC, Color sC, Color tC, Color qC, Color qqC, Color pT, Color sT, Color tT, Color qT) {
		this.name = n;
		this.primaryColor = pC;
		this.secondaryColor = sC;
		this.tertiaryColor = tC;
		this.quarternaryColor = qC;
		this.quintiaryColor = qqC;
		this.primaryFontColor = pT;
		this.secondaryFontColor = sT;
		this.tertiaryFontColor = tT;
		this.quarternaryFontColor = qT;
	}
	
	// Saves the current theme to file
	public void Save(){
		try{
	         FileOutputStream fileOut = new FileOutputStream("Themes/" + this.name + ".thm");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(this);
	         out.close();
	         fileOut.close();
	      }catch(IOException i){ i.printStackTrace(); }
	}
	
	// Loads the requested theme into the class 
	public void Load(String fileName){
		Theme newTheme;
		try{
	         FileInputStream fileIn = new FileInputStream("Themes/" + fileName + ".thm");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         newTheme = (Theme) in.readObject();
	         in.close();
	         fileIn.close();
	         this.name = newTheme.name;
	         this.primaryColor = newTheme.primaryColor;
	         this.secondaryColor = newTheme.secondaryColor;
	         this.tertiaryColor = newTheme.tertiaryColor;
	         this.quarternaryColor = newTheme.quarternaryColor;
	         this.quintiaryColor = newTheme.quintiaryColor;
	         this.primaryFontColor = newTheme.primaryFontColor;
	         this.secondaryFontColor = newTheme.secondaryFontColor;
	         this.tertiaryFontColor = newTheme.tertiaryFontColor;
	         this.quarternaryFontColor = newTheme.quarternaryFontColor;
	      }catch(IOException | ClassNotFoundException i){ i.printStackTrace(); }
		newTheme = null;
	}
	
	// Getters
	public String getName() { return this.name; }
	public Color getPrimaryColor() { return this.primaryColor; }
	public Color getSecondaryColor() { return this.secondaryColor; }
	public Color getTertiaryColor() { return this.tertiaryColor; }
	public Color getQuarternaryColor() { return this.quarternaryColor; }
	public Color getQuintiaryColor(){ return this.quintiaryColor; }
	public Color getPrimaryFontColor() { return this.primaryFontColor; }
	public Color getSecondaryFontColor() { return this.secondaryFontColor; }
	public Color getTertiaryFontColor() { return this.tertiaryFontColor; }
	public Color getQuarternaryFontColor() { return this.quarternaryFontColor; }
}
