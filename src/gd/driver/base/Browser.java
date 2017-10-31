package gd.driver.base;

public class Browser {

	public final static String Firefox = "firefox";
	public final static String Chrome = "chrome";
	public final static String IE = "ie";
	
	private static String CurrentBrowser= Chrome;
	
	public static String getCurrentBrowser(){
		 return CurrentBrowser;
	}
	
	public static void setCurrentBrowser(String browser){
		CurrentBrowser = browser;
	}
}
