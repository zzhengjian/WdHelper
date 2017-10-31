package gd.driver.base;

import org.openqa.selenium.WebDriverException;


public class PageHelper {
	
	public static boolean isFlex = true;

	
	public static String getPagePrefix(Projects project)
	{
		String toReturn = null;
		switch(project)
		{
			case GD:
				toReturn =  isFlex ? "GDFlex_" : "GD_";
				break;
			case Nascar:
				toReturn =  isFlex ? "NascarFlex_" : "Nascar_";
				break;
			case Rush:
				toReturn =  isFlex ? "RushFlex_" : "Rush_";
				break;
			case Walmart:
				toReturn =  "";
				break;
			case GoBank:
				toReturn = "";
			case FSC:
				toReturn = "";
			default:
				toReturn = "";
				break;
				
		}
		
		return toReturn;
	}
	
	public static Projects getProjectFromUrl(String url)
	{
		if(url.contains("walmartmoneycard"))
			return Projects.Walmart;
		else if(url.contains("racing"))
			return Projects.Nascar;
		else if(url.contains("rush"))
			return Projects.Rush;
		else if(url.contains("greendot") && !url.contains("racing"))
			return Projects.GD;
		else if(url.contains("pos"))
			return Projects.FSC;
		else 
			throw new WebDriverException("Not implemented project");
		
	}

	public static String generatePageNameWithUrl(String url)
	{
		return  getPagePrefix(getProjectFromUrl(url)) + Utils.getPageNameFromUrl(url);
	}
	
	public static String generatePageNameWithProject(Projects project, String pageName)
	{
		return  getPagePrefix(project) + pageName;
	}
	

	
	public enum Projects {		
		GD,
		Walmart,
		GoBank,
		FSC,
		Nascar,
		Rush,
	}
	
	

	
	


}
