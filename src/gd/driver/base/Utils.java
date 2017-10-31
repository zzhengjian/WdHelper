package gd.driver.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
	
	
	
	public static String capitalizeWord(String text)
	{	
		if(text==null)
		{
			throw new NullPointerException("error input");
		}
		
		if(isCamelCased(text))
		{
			return text;
		}
	
		if(text.length() > 1)
		{					
			return text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
		}
		return text.toUpperCase();
		
	}

	public static String getPageNameFromUrl(String url)
	{
		String[] _url = url.split("/");		
		String[] _pageName = _url[_url.length - 1].split("#")[0].split("\\?")[0].split("-");
		String pageName = "";
		for(int index=0;index<_pageName.length;index++)
		{
			pageName += capitalizeWord(_pageName[index]);
		}
		
		return pageName;
	}

	/**
	 * evaluate text to generate a Element Name
	 * */
	public static String evalName(String text)
	{
		Pattern pattern = Pattern.compile("[a-zA-Z0-9\u4e00-\u9fa5]+",Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);
		
		StringBuilder _text = new StringBuilder("");
		int i = 0;
		while(matcher.find())
		{			
			_text.append(capitalizeWord(matcher.group()));
			i++;
			if(i==3) break;				
		}
				
		return _text.toString();
	}
	
	public static String replaceAllSpecialChar(String text)
	{
		
		return text.replaceAll("[-+.^:,?#$&*@!\"{}<>~;']","");
		
	}
	
	
	public static boolean isCamelCased(String text)
	{
		
		return text.matches("^[A-Z].*")&&!text.equals(text.toUpperCase());
	}

	public static String getNameFromSrc(String src)
	{
		if(src==null)
		{
			return null;
		}		
		String[] _src = src.split("/");
		//return a source name
		return _src[_src.length - 1].split("\\.")[0];		
	}
	
	
	public static boolean isXpath(String selector)
	{
		if(selector.startsWith("./") || selector.startsWith("/") || selector.startsWith("(/") || selector.startsWith("(./") || selector.startsWith("html"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public static String EscapeQuate(String text)
	{
		if(text != null && text.contains("\""))
		{
			return text.replace("\"", "\\\"");
		}
		return text;
	}
	
	/**
	 * Convenience method that wraps around Thread.Sleep()
	 * 
	 * @param lSecondsToWait
	 * (long) - Seconds to sleep.
	 */
	public static void sleepFor(long lSecondsToWait)
	{
		try{
			Thread.sleep(lSecondsToWait * 1000);
		}
		catch (Exception e){
		}
	}
	
	/**
	 * Returns a numeric string representation of a randomly generated positive long.
	 * 
	 * @param iLength
	 * (int) - Length of return string.
	 * 
	 * @return
	 * (String) - numeric string.
	 */
	public static String getRandomNumeric(int iLength)
	{
		java.util.Random oRandom = new java.util.Random();
		String oStr = Long.toString(oRandom.nextLong(), 10);
		return oStr.substring(1, 1+iLength);
	}

}
