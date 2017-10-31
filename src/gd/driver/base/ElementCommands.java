package gd.driver.base;

import java.util.ArrayList;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

public class ElementCommands {

	
	public static void highLightElement(WebElement oWebElement){
		
		if(!oWebElement.isDisplayed()){
			throw new ElementNotVisibleException("Element not visible");
		}
		
		((RemoteWebElement)oWebElement).getCoordinates().inViewPort();		
		JavascriptExecutor driver = (JavascriptExecutor)Driver.getWebDriver();
		
		ArrayList<String> oldflasher = new ArrayList<String>();
		oldflasher.add("");
		oldflasher.add("");
		
		
		String js = "var flasher = [];" 
					+ " flasher[0] = arguments[0].style.outline;"
					+ " flasher[1] = arguments[0].style.background;"
					+ " arguments[0].style.outline='3px solid red';"
					+ " arguments[0].style.background = 'yellow';"
					+ " return flasher;";
		
		String revertjs = "var flasher = [];" 
				+ " flasher[0] = arguments[0].style.outline;"
				+ " flasher[1] = arguments[0].style.background;"
				+ " arguments[0].style.outline='" + oldflasher.get(0) + "';"
				+ " arguments[0].style.background = '" + oldflasher.get(1) + "';"
				+ " return flasher;";		

		for(int i=0; i<3; i++){
			oldflasher = (ArrayList<String>)driver.executeScript(js, oWebElement);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}
			oldflasher = (ArrayList<String>)driver.executeScript(revertjs, oWebElement);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}
		}
	}
	
	
public static String getElementInfo(WebElement oWebElement){
		
		StringBuilder elementinfo = new StringBuilder();

		String js = "var attrs = '';"
	           + " var el = arguments[0];"
	           + " for (var i = 0, atts = el.attributes, n = atts.length, arr = []; i < n; i++){"
	           + "     if(i === 0){"
	           + "       attrs = atts[i].nodeName + ': ' + atts[i].nodeValue"
	           + "     }"
	           + "     else{"
	           + "       attrs += '|' + atts[i].nodeName + ': ' + atts[i].nodeValue"
	           + "     }"
	           + " }"
	           + " return attrs;";     
		String obj = (String)((JavascriptExecutor)Driver.getWebDriver()).executeScript(js, oWebElement);
		ArrayList<String> attrs = new ArrayList<String>();
		attrs.add("isdisplayed: " + String.valueOf(oWebElement.isDisplayed()));
		attrs.add("isenabled: " + String.valueOf(oWebElement.isEnabled()));
		attrs.add("isselected: " + String.valueOf(oWebElement.isSelected()));
		attrs.add("text: " + oWebElement.getText());
		String value = oWebElement.getAttribute("value") == null ? "" : oWebElement.getAttribute("value");
		attrs.add("value: " + value);
		attrs.add(obj.replace("|", "\n"));
		for(String css : WebDriverSettings.CSSs){
			attrs.add(css + ": " + oWebElement.getCssValue(css));
		}
		
		for(String line : attrs){
			elementinfo.append(line).append("\n");
		}
		
		return elementinfo.toString();
	}
}
