package gd.driver.debug.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.google.gson.JsonObject;

import gd.driver.base.DebugCommand;
import gd.driver.base.Driver;

public class CommandTest {
	
	

	public void testNewElement(){
		
		WebDriver driver = Driver.getWebDriver();		
		
		driver.get("https://secure.walmartmoneycard.com/login");
		
		JsonObject element = Driver.generateElement("#TxtAccountNoOrUserId");
		
		Driver.quitDriver();
		System.out.println(element.toString());
	}
	
	@Test
	public void testNewElementList(){
		
		Driver.getWebDriver();
		
		
		Driver.getWebDriver().get("https://www.walmartmoneycard.com/login");
		ArrayList<JsonObject> elements = Driver.generateElementList("#body");


		Driver.quitDriver();
		System.out.println(elements.toString());
	}
	
	

	public void showDebugCommands(){
				
		HashMap<String, DebugCommand> commands = DebugCommand.getDebugCommands();
		
		List<String> methods = new ArrayList<String>();
		for(Entry<String, DebugCommand> entry : commands.entrySet()){			
			methods.add(entry.getKey());
			System.out.println(entry.getKey());
		}
		
	}
	
	
	public void debuginfotest(){
		Driver.getWebDriver();		
		Driver.getWebDriver().get("https://www.walmartmoneycard.com/login");		
		
		HashMap<String, DebugCommand> commands = DebugCommand.getDebugCommands();
		
		String result = commands.get("getElementInfo").run(".//*[@id='div-form-errors']");
		Driver.quitDriver();
		System.out.println(result);
		
	}

}
