package com.fervort.supermql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.fervort.supermql.log.SuperLog;
import com.fervort.supermql.myquery.MyQuery;
import com.fervort.supermql.xml.ConfigReader;
import com.fervort.supermql.xml.MyQueryReader;

import matrix.db.Context;
import matrix.util.MatrixException;

public class SuperMQLMain {

	public static void main(String[] args) throws IOException, MatrixException {
		
		new SuperMQLMain().startAsStandalone(args);
		
	}
	
	public void startAsStandalone(String[] args)
	{
		//System.out.println("Invalid Call !");
		//System.out.println("Call function invokeSuperMQL(Context context,String[] args) from JPO");
		SuperMQLSupport sms = new SuperMQLSupport();
		if(args.length>=5 && args[0].trim().equalsIgnoreCase("-login"))
		{
			try {
				//Context localContext = 	null ; //sms.createEnoviaContext(args);
				Context localContext = sms.createEnoviaContext(args);
				invokeSuperMQL(localContext, args);
				// TODO Can't close command here as it is started from another instance
				//sms.closeCommand();
				sms.shutdownByContext(localContext);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else
		{
			System.out.println("Syntax: java -jar SuperMQL.jar -login host username password vault");
			System.out.println("");
			System.out.println("Example: java -jar SuperMQL.jar -login \"http://3dspace:8070/internal\" \"creator\" \"pass123\" \"eService Production\" ");
		}
	}
	
	public void invokeSuperMQL(Context context,String[] args) throws IOException, MatrixException, ParserConfigurationException, SAXException, TransformerException
	{
		// if passed directly execute as script
		if(args.length>=1 && !args[0].trim().equalsIgnoreCase("-login"))
		{	
			String fileContent = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8); 
			
			new GroovyScriptBuilder().buildGroovyScript(context,fileContent);
		}else
		{	
			
			SuperMQLSupport gss = new SuperMQLSupport();
			gss.setCurrentContext(context);
			gss.buildMQLCommand();
			
			ConfigReader.initializeConfiguration();
			
			SuperLog.init();
			
			if(ConfigReader.readConfigKey("EditorMode").equalsIgnoreCase("basic"))
			{
				
				Scanner scanner = new Scanner(System.in);
				
				printWelcomeMessage();
				
				System.out.println();
				
				int i=1;
				
				String strUserInput;
				
				MyQuery myQuery = new MyQuery();
				do
				{
					System.out.print("Smql<"+i+">");
					strUserInput = scanner.nextLine().trim();
					
					//if(strUserInput.length()==0)
					//	continue;
					
					if(strUserInput.startsWith("."))
					{
						String strAsMQL = strUserInput;
						try
						{
							String strMQL = strAsMQL.substring(1, strAsMQL.length()).trim();
							String strResult = gss.mql(strMQL);
							System.out.println(strResult);
						}catch(Exception ex)
						{
							System.out.println(ex);
						}
					//it is script	
					}
					else if(strUserInput.startsWith("f ")) 
					{
						try
						{
							String filePath = strUserInput.substring(1, strUserInput.length()).trim();
							SuperLog.debug("File path: "+filePath); 
							// TODO check file is valid ?
							String fileContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8); 
							new GroovyScriptBuilder().buildGroovyScript(context,fileContent);
						}catch(Exception ex)
						{
							System.out.println(ex);	
						}
					}
					//else if(!strUserInput.equalsIgnoreCase("quit") && !strUserInput.equalsIgnoreCase("exit"))
					else if(strUserInput.startsWith("i "))
					{
						try
						{
						// TODO add try catch block otherwise if something will fail, control will go out of loop
						new GroovyScriptBuilder().buildGroovyScript(context,strUserInput.substring(1, strUserInput.length()).trim());
						System.out.println("");
						
						}catch(Exception ex)
						{
							System.out.println(ex);	
						}
					}
					else if(strUserInput.startsWith("config"))
					{
						ConfigHandler.processConfigCommand(strUserInput);
					}
					else if(strUserInput.startsWith("myq "))
					{
						String strMyQuery = strUserInput.substring(4, strUserInput.length()).trim();
						System.out.println("MyQuery: "+strMyQuery);
						try
						{
							myQuery.processMyQuery(gss,strMyQuery);
							
						}catch(Exception ex)
						{
							System.out.println(ex);
						}
						//storeMyQuery(strUserInput);
					}
					
					
					
					i++;
				}while(!strUserInput.equalsIgnoreCase("quit") && !strUserInput.equalsIgnoreCase("exit"));
				System.out.println("Bye Bye :)");
				
				// Don't close Scanner as System.in is used in Scanner constructor.
				//scanner.close();
				
			}else
			{
				AdvanceReader.startAdvanceMode(gss);
			}
			gss.closeCommand();
			// No need to close context as MQL context is being used
			//gss.shutdownContext();
		}
	}
	List myQueries = new ArrayList<String>();
	private void storeMyQuery(String myQuery)
	{
		if(myQuery.contains("=>"))
		{
			String[] aMyQueries = myQuery.split("=>");
			
			File file = new File("SuperMQL.properties");
			
			System.out.println("Writing to "+file.getAbsolutePath());
			
			try (OutputStream outProperties = new FileOutputStream(file,true)) {

				
	            Properties prop = new Properties();

	            // set the properties value
	            prop.setProperty(aMyQueries[0], aMyQueries[1]);
	         
	            prop.store(outProperties, null);

	            System.out.println(prop);

	        } catch (IOException io) {
	            io.printStackTrace();
	        }
			
		}else
		{
			System.out.println("Wrong Syntax. Correct syntax is:");
		}
		
	}
	private void printWelcomeMessage()
	{
		// TODO Show tip of the day before starting
		System.out.println("Welcome to SuperMQL");
		System.out.println("");
		System.out.println("To execute native MQL, start with dot, For ex=> Smql<1>.temp query bus or Smql<3>.print bus");
		System.out.println("");
		System.out.println("To execute java/groovy script from file: Smql<1>f D:\\Project\\script\\RenameBus.groovy");
		System.out.println("");
		System.out.println("To execute java/groovy script in Interactive mode: Smql<1>i def x = 100; println x;");
		System.out.println("MQL funcion: Smql<1>i def result=mql('list vault *'); print result");
		System.out.println("");
		System.out.println("EditorMode : 'basic' is deprecated. It will be removed in next release. Please use advance mode ");
		System.out.println("");
		System.out.println("SuperMQL, Version 2.0.0.beta");
		
		System.out.println("\n\n\n");
	}

}
