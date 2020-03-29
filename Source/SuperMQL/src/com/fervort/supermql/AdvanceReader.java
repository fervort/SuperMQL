package com.fervort.supermql;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import com.fervort.supermql.log.SuperLog;
import com.fervort.supermql.myquery.MyQuery;

import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.Completer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.*;



public class AdvanceReader {

	SuperMQLSupport gss;
	MyQuery myQuery;
	
	public void execute() {
		
		LineReaderBuilder readerBuilder = LineReaderBuilder.builder();
		List<Completer> completors = new LinkedList<Completer>();

		completors.add(new StringsCompleter(commandsToComplete));
		readerBuilder.completer(new ArgumentCompleter(completors));

		LineReader reader = readerBuilder.build();

		String line;
		
		int iCounter=1;
		while ((line = readLine(reader, iCounter)) != null) {
			
			if(!executeCommands(line))
				return;
			iCounter++;
		}

		
	}

	private boolean executeCommands(String strUserInput)
	{
		
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
				// TODO check file is valid ?
				SuperLog.debug("File path: "+filePath); 
				String fileContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8); 
				new GroovyScriptBuilder().buildGroovyScript(gss.getCurrentContext(),fileContent);
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
			new GroovyScriptBuilder().buildGroovyScript(gss.getCurrentContext(),strUserInput.substring(1, strUserInput.length()).trim());
			System.out.println("");
			
			}catch(Exception ex)
			{
				System.out.println(ex);	
			}
		} else if ("exit".equalsIgnoreCase(strUserInput)) {
			
			AttributedStringBuilder a = new AttributedStringBuilder()
			//.append("You will be returned to normal mode. You can use ")
			.append("Bye Bye !", AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
			//.append(" again to enter into advance mode.");
			;
				
			System.out.println(a.toAnsi());
			
			return false;
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
		
		else {
			
		}
		
		return true;
	}
	
	AttributedStringBuilder rightPrompt = new AttributedStringBuilder()
			.append("adv", AttributedStyle.BOLD.foreground(AttributedStyle.MAGENTA))
			;
	
	private String readLine(LineReader reader, int iCounter) {
		try {
			String line = reader.readLine("Smql<"+iCounter+"> ",rightPrompt.toAnsi(),(MaskingCallback) null, null);
			return line.trim();
		}
		catch (UserInterruptException e) {
			// when pressed control C (ctrl-C)
			return null;
		}
		catch (EndOfFileException e) {
			// When pressed control and D (ctrl-D)
			return null;
		}
	}

	private String[] commandsToComplete;

	public void initAdvanceReader(SuperMQLSupport gss) {
		
		this.gss = gss;
		
		this.myQuery = new MyQuery();
		// enable ansi
		AnsiConsole.systemInstall(); 
		
		// setup commands
		commandsToComplete = new String[] { "exit", "mql", ". temp query bus TYPE NAME REV", ". print bus" };
		
		printAdanceReaderHelp();
		//System.out.println("You are in Advance mode. Press TAB for list of auto complete commands.");
	}
	
	private void printAdanceReaderHelp()
	{
		// TODO Show tip of the day before starting
		AttributedStringBuilder aHelp = new AttributedStringBuilder()
		.append("Welcome to SuperMQL", AttributedStyle.BOLD.foreground(AttributedStyle.GREEN)).append("\n")
		;
		System.out.println(aHelp.toAnsi());
		System.out.println("");
		System.out.println("To execute native MQL, start with dot, For ex=> Smql<1>.temp query bus or Smql<3>.print bus");
		System.out.println("");
		System.out.println("To execute java/groovy script from file: Smql<1>f D:\\Project\\script\\RenameBus.groovy");
		System.out.println("");
		System.out.println("To execute java/groovy script in Interactive mode: Smql<1>i def x = 100; println x;");
		System.out.println("MQL funcion: Smql<1>i def result=mql('list vault *'); print result");
		System.out.println("");
		System.out.println("SuperMQL, Version 2.0.0.beta");
		System.out.println("");
		System.out.println("\n\n\n");
	}
	private void closeAdvanceReader()
	{
		AnsiConsole.systemUninstall();
	}
	
	public static void startAdvanceMode(SuperMQLSupport gss) {
		AdvanceReader avReader = new AdvanceReader();
		
		avReader.initAdvanceReader(gss);
		
		avReader.execute();
		
		avReader.closeAdvanceReader();
	}
}