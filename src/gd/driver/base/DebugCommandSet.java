package gd.driver.base;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

public class DebugCommandSet {
		
	
	public static void goTo(String url){
		Driver.getWebDriver().get(url);
	}
	
	public static void back(){
		Driver.getWebDriver().navigate().back();
	}
	
	public static void forward(){
		Driver.getWebDriver().navigate().forward();
	}
	
	public static String getTitle(){
		return Driver.getWebDriver().getTitle();
	}
		
	public static String getCurrentUrl(){
		return Driver.getWebDriver().getCurrentUrl();
	}
	
	public static void switchToDefaultContent(){
		Driver.getWebDriver().switchTo().defaultContent();
	}
	
	public static void switchToCurrentWindow(){
		String handle = Driver.getWebDriver().getWindowHandle();
		Driver.getWebDriver().switchTo().window(handle);
	}
	
	public static String executeJs(String jsScript){
		return (String)((RemoteWebDriver)Driver.getWebDriver()).executeScript(jsScript);
	}
	
	public static String getPageSource(){
		return Driver.getWebDriver().getPageSource();
	}
	
	public static String getCurrentWindowHandle(){
		return Driver.getWebDriver().getWindowHandle();
	}
	
	public static String getWindowHandles(){
		return Driver.getWebDriver().getWindowHandles().toString();
	}
	
	public static void switchToWindow(String windowHandle){
		Driver.getWebDriver().switchTo().window(windowHandle);
	}
	
	public static String getAlertText(){
		return Driver.getWebDriver().switchTo().alert().getText();
	}
	
	public static void acceptAlert(){
		Driver.getWebDriver().switchTo().alert().accept();
	}
	
	public static void dismissAlert(){
		Driver.getWebDriver().switchTo().alert().dismiss();
	}
	
	public static void sendKeyToAlert(String value){
		Driver.getWebDriver().switchTo().alert().sendKeys(value);
	}
	
	public static void switchToFrame(String selector){
		WebElement element = Driver.getWebDriver().findElement(Driver.getBy(selector));
		Driver.getWebDriver().switchTo().frame(element);
	}
	
	public static void switchToParentFrame(){
		Driver.getWebDriver().switchTo().parentFrame();
	}
	
	
	public static void click(String selector){
		Driver.getWebDriver().findElement(Driver.getBy(selector)).click();
	}	

	
	public static void clear(String selector){
		Driver.getWebDriver().findElement(Driver.getBy(selector)).clear();
	}
	
	public static void mouseover(String selector, Boolean usingJs){
		WebElement element = Driver.getWebDriver().findElement(Driver.getBy(selector));
		if(usingJs){
			JavascriptExecutor driver = (JavascriptExecutor)Driver.getWebDriver();
			String mouseOverJs = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			driver.executeScript(mouseOverJs, element);
		}
		else{
			Actions action = new Actions(Driver.getWebDriver());
			action.moveToElement(element).perform();
		}				
	}
	
	public static String getText(String selector){
		return Driver.getWebDriver().findElement(Driver.getBy(selector)).getText();
	}
	
	public static String isDisplayed(String selector){
		boolean toReturn = Driver.getWebDriver().findElement(Driver.getBy(selector)).isDisplayed();
		return String.valueOf(toReturn);
	}
	
	public static String isEnabled(String selector){
		boolean toReturn = Driver.getWebDriver().findElement(Driver.getBy(selector)).isEnabled();
		return String.valueOf(toReturn);
	}
	
	public static String isSelected(String selector){
		boolean toReturn = Driver.getWebDriver().findElement(Driver.getBy(selector)).isSelected();
		return String.valueOf(toReturn);
	}
	
	public static String getAttribute(String selector, String attribute){
		return Driver.getWebDriver().findElement(Driver.getBy(selector)).getAttribute(attribute);
	}
	
	public static String getCssValue(String selector, String cssValue){
		return Driver.getWebDriver().findElement(Driver.getBy(selector)).getCssValue(cssValue);
	}
	
	public static String getTag(String selector){
		return Driver.getWebDriver().findElement(Driver.getBy(selector)).getTagName();
	}
	
	public static String executeJsOnElement(String selector, String jsScript){
		WebElement oWebElement = Driver.getWebDriver().findElement(Driver.getBy(selector));
		return (String)((RemoteWebDriver)Driver.getWebDriver()).executeScript(jsScript, oWebElement);
	}
	
	public static String getElementCount(String selector){
		int size = Driver.getWebDriver().findElements(Driver.getBy(selector)).size();
		return String.valueOf(size);
	}
	
	public static String getElementScreenShot(String selector){
		WebElement oWebElement = Driver.getWebDriver().findElement(Driver.getBy(selector));
		//Get entire page screenshot
		File screenshot = ((TakesScreenshot)Driver.getWebDriver()).getScreenshotAs(OutputType.FILE);
		String result = null;		
		BufferedImage fullImg = null;
		try {
			fullImg = ImageIO.read(screenshot);
			//Get the location of element on the page
			Point point = oWebElement.getLocation();
			//Get width and height of the element
			int eleWidth = oWebElement.getSize().getWidth();
			int eleHeight = oWebElement.getSize().getHeight();
			//Crop the entire page screenshot to get only element screenshot
			BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
			ImageIO.write(eleScreenshot, "png", screenshot);
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			OutputStream b64 = new Base64OutputStream(os);
			ImageIO.write(eleScreenshot, "png", b64);
			result = os.toString("UTF-8");

		} catch (IOException e2) {
			throw new WebDriverException("IO Error in getElementScreenShot");
		}
		return result;
	}
	
	public static void highLightElement(String selector){
		WebElement oWebElement = Driver.getWebDriver().findElement(Driver.getBy(selector));
		ElementCommands.highLightElement(oWebElement);
	}
	
	public static void showElement(String selector){
		
		WebElement oWebElement = Driver.getWebDriver().findElement(Driver.getBy(selector));
		((JavascriptExecutor)Driver.getWebDriver()).executeScript("return $(arguments[0]).show();", oWebElement);	
	}
	
	public static void hideElement(String selector){
		
		WebElement oWebElement = Driver.getWebDriver().findElement(Driver.getBy(selector));
		((JavascriptExecutor)Driver.getWebDriver()).executeScript("return $(arguments[0]).hide();", oWebElement);	
	}
	
	public static String getElementInfo(String selector){
		
		WebElement oWebElement = Driver.getWebDriver().findElement(Driver.getBy(selector));
		return ElementCommands.getElementInfo(oWebElement);
	}
	


}
