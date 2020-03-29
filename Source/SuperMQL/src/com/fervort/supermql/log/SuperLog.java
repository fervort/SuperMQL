package com.fervort.supermql.log;

import com.fervort.supermql.xml.ConfigReader;

public class SuperLog {

	private static boolean isDebugEnabled = false;
	
	public static void init()
	{
		if(ConfigReader.readConfigKey("EnableDebugging").equalsIgnoreCase("yes"))
		{
			isDebugEnabled = true;
			System.out.println("=====Debugging Enabled=====");
		}
	}
	
	public static void debug(String str)
	{
		if(isDebugEnabled)
		{
			System.out.println(str);
		}
	}
}
