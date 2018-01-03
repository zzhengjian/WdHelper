package gd.driver.base;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Resources;
import com.google.gson.JsonObject;



public class Driver {

	
	private static WebDriver oWebDriver;	
	private static String root = "//body";
	
	public static WebDriver getWebDriver()
	{
		if(oWebDriver == null || oWebDriver.toString().contains("null")){
			oWebDriver = StartWebDriver(Browser.getCurrentBrowser());
		}
		
		return oWebDriver;
	}	
	
	public String getRootNode() {
		return root;
	}

	public static void setRootNode(String rootNode) {
		Driver.root = rootNode;
	}	
	
	public static String getCurrentUrl() {
		return getWebDriver().getCurrentUrl();
	}
	
	/**
	 * Instantiate browser specific WebDriver based on browser type.
	 * 
	 * @param sBrowserType
	 * (String)
	 * 
	 * @return
	 * (WebDriver)
	 * 
	 * @throws Exception
	 */
	private static WebDriver StartWebDriver(String sBrowserType)
	{		
		switch (sBrowserType.toUpperCase())
		{
			case "CHROME":	
				oWebDriver = startChrome();					
				break;
					
			case "IE":
				oWebDriver = startIE();				
				break;
				
			case "FIREFOX":
				oWebDriver =  startFirefox();					
				break;
				
			case "MARIONETTE":
				oWebDriver = startMarionette();
				
			default:
				oWebDriver =  startFirefox();			
				break;				
				
		}			
		return oWebDriver;
		
	}
	
		
	private static WebDriver startFirefox(){
		FirefoxProfile profile = new FirefoxProfile(new File(WebDriverSettings.PATH + "/conf/fxprofile"));		
		profile.setPreference("network.automatic-ntlm-auth.trusted-uris", "gdc1wcg01.nextestate.com");
		profile.setPreference("xpinstall.signatures.required", false);
		return  new FirefoxDriver(profile);	
	}
	
	private static WebDriver startMarionette(){
		return  new FirefoxDriver();	
	}
	
	private static WebDriver startChrome(){
		DesiredCapabilities caps = new DesiredCapabilities();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-popup-blocking");
		caps.setCapability(ChromeOptions.CAPABILITY, options);
		return new ChromeDriver(caps);
	}
	
	private static WebDriver startIE(){		
		DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
		dc.setCapability("nativeEvents", true);
		return new InternetExplorerDriver(dc);
	}
	
	public static void quitDriver(){
		if(oWebDriver != null)
			oWebDriver.quit();
	}
	
	public static boolean isConnected(){
		if(oWebDriver != null && !oWebDriver.toString().contains("null")){
			return true;
		}
		return false;
	}
	
	public static void setPageLoadTimeOut(int seconds){
		if(oWebDriver != null)
			oWebDriver.manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);		
	}
	
	public static void setImplicitylyWait(int seconds){
		if(oWebDriver != null)
			oWebDriver.manage().timeouts().implicitlyWait(seconds * 1000, TimeUnit.MILLISECONDS);		
	}
	
	public static void setDesktopMode(){
		oWebDriver.manage().window().maximize();
	}
	
	public static void setRWDMode(){		
		Dimension arg0 = new Dimension(640, 840);
		oWebDriver.manage().window().setSize(arg0);
	}
	
	/**
	 * Instantiate RemoteWebDriver based on browser type.  This is used for Grid.
	 * 
	 * @param sBrowserType
	 * (String)
	 * 
	 * @return
	 * (WebDriver)
	 * 
	 * @throws Exception
	 */
	public WebDriver StartRemoteWebDriver(String sBrowser, URL remoteAddress) throws Exception
	{

		DesiredCapabilities dc = null;
		RemoteWebDriver oRemoteWebDriver;
		String[] aBrowserInfo = sBrowser.split("\\|");   // Browser info consist of BrowserType|Version
		boolean bMaximizeBrowser = true;
		
		switch (aBrowserInfo[0].toUpperCase())
		{
			case "CHROME":
				dc = DesiredCapabilities.chrome();
				if (bMaximizeBrowser)
				{
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--start-maximized");
					dc.setCapability(ChromeOptions.CAPABILITY, options);
					//dc.setCapability("chrome.switches", java.util.Arrays.asList("--start-maximized"));
				}
				break;	
			case "IE":
				dc = DesiredCapabilities.internetExplorer();
				dc.setCapability("nativeEvents", true);
				dc.setCapability("platform", "ANY");
				//dc.setCapability("ensureCleanSession", false);
				break;
			case "FIREFOX":
				dc = DesiredCapabilities.firefox();
				break;
			default:
				throw new Exception("Failed to instantiate RemoteWebDriver!");
				
		}
		
		try
		{
			if (aBrowserInfo.length > 1)
				dc.setVersion("11");
				//dc.setCapability("version", aBrowserInfo[1]);
			
			
			oRemoteWebDriver =  new RemoteWebDriver(remoteAddress, dc);
			return oRemoteWebDriver;
		}
		catch (Exception e)
		{
			//logger.error("Failed to instantiate RemoteWebDriver:  {}", e);
			throw new Exception("Failed to instantiate RemoteWebDriver!", e.getCause());
		}
	}

	
	public static void waitForPageReady()
	{
		WebDriverWait wait = new WebDriverWait(oWebDriver, 5000);
		wait.until(pageLoaded(oWebDriver));
		wait.until(ExpectedConditions.presenceOfElementLocated(getBy(root)));
		
	}
	
	
	public static JsonObject generateElement(String selector){		
		WebElement element = getWebDriver().findElement(getBy(selector));	
		
		ElementHelper elementHelper = new ElementHelper(element);
		String elementName = elementHelper.evaluateElementName();
		
		JsonObject ele = new JsonObject();
		ele.addProperty("elementName", elementName);
		ele.addProperty("selector", selector);
		if(!"".equals(elementHelper.getText()) && !element.getTagName().toLowerCase().matches("select"))
		{
			ele.addProperty("text", elementHelper.getText());
		}
		return ele;		
	}
	
	public static JsonObject generateElement(WebElement element){	
		
		String selector = getSelectorFromElement(element);
		if("".equals(selector)) {
			return null;
		}
		
		ElementHelper elementHelper = new ElementHelper(element);
		String elementName = elementHelper.evaluateElementName();
		
		JsonObject ele = new JsonObject();
		ele.addProperty("elementName", elementName);
		ele.addProperty("selector", selector);
		if(!"".equals(elementHelper.getText()) && !element.getTagName().toLowerCase().matches("select"))
		{
			ele.addProperty("text", elementHelper.getText());
		}
		return ele;		
	}
	
	
	
	public static ArrayList<JsonObject> generateElementList(String parentLocator){
		
		waitForPageReady();
		ArrayList<JsonObject> elements = new ArrayList<JsonObject>();		
		List<WebElement> elementsToWalk = new ArrayList<WebElement>();
		
		WebElement parent = getWebDriver().findElement(getBy(parentLocator));
		for(String xpath : ElementHelper.xpaths())
		{			
			elementsToWalk.addAll(parent.findElements(By.xpath(xpath)));
		}
		
		JsonObject ele;		
		for(WebElement e : elementsToWalk)
		{			
			if(e.isDisplayed())
			{				
				ele = generateElement(e);
				if(ele != null)
					elements.add(ele);
			}				
		}		
		
		return elements;		
	}
	
	public static String inspectOnElement() {
		if(!(oWebDriver instanceof FirefoxDriver)){
			throw new WebDriverException("Inspect only works on firefox");			
		}
		
		//this line to trigger firepath inspect mode					
		oWebDriver.manage().ime().deactivate();
		Utils.sleepFor(1);
		//this line to get selector once element is inspected
		return oWebDriver.manage().ime().getActiveEngine();	
		
		
	}
	
	/**
	 * To generate an ElementList from give page and parent node
	 * [default parent would be "//body"]
	 * 
	 * */
	private static String getSelectorFromElement(WebElement e) {
		
		String selector = "";
		String getselectorjs = readScriptImpl("/gd/driver/resources/getSelector.js");
		try {
			selector = (String) ((RemoteWebDriver) oWebDriver).executeScript(getselectorjs + " return utils.getCssSelectorFromNode(arguments[0]);", e);
		} catch (Exception e1) {			
			System.err.println("generate selector for element fail");
			System.err.println("ignore exceptions");
		}
				
		return selector;
	}

	
	public static ExpectedCondition<Boolean> pageLoaded(final WebDriver driver) {
	    return new ExpectedCondition<Boolean>() {
	      @Override
	      public Boolean apply(WebDriver driver){ 

	        	return (Boolean)((JavascriptExecutor)driver).executeScript("return document.readyState == 'complete';");
	      }

	      @Override
	      public String toString() {
	        return String.format("Page (%s) to become loaded", driver);
	      }
	    };
	 }
	
	/**
	 * get By from String 
	 * this method only supports css and xpath
	 * 
	 * */
	public static By getBy(String selector)
	{
		
		if(Utils.isXpath(selector))
		{
			return By.xpath(selector);
		}
		else
		{
			return By.cssSelector(selector);
		}
	}
	
	private static  String readScriptImpl(String script) {
	    URL url = Driver.class.getResource(script);

	    if (url == null) {
	      throw new RuntimeException("Cannot locate " + script);
	    }

	    try {
	      return Resources.toString(url, Charset.forName("UTF-8"));
	    } catch (IOException e) {
	      throw new RuntimeException(e);
	    }
	  }	
}
