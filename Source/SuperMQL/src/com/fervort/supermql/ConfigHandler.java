package com.fervort.supermql;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fervort.supermql.xml.ConfigReader;

public class ConfigHandler {

	// replace with http://jcommander.org/ or apache cli 
	
	public static void processConfigCommand(String command)
	{
		String cleanCommand = SuperUtilities.replaceMultipleSpaceToSingle(command);
		
		//System.out.println("command "+cleanCommand);
		if(cleanCommand.equalsIgnoreCase("config list") || cleanCommand.equalsIgnoreCase("config"))
		{
			NodeList nlConfig = ConfigReader.doc.getElementsByTagName("SuperMQLConfiguration");
			NodeList nChildren = nlConfig.item(0).getChildNodes();
			
			System.out.println("");
			System.out.println("Config Path => "+ConfigReader.configPath);
			System.out.println("");
			for(int i=0;i<nChildren.getLength();i++)
			{
				Node node = nChildren.item(i);
				if(!node.getNodeName().equals("#text"))
				{
					System.out.println("\t"+node.getNodeName()+"\t=>\t"+node.getTextContent());
				}
			}
			System.out.println("");
		}
		
	}
	
}
