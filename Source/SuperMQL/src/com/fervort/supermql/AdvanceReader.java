package com.fervort.supermql;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.Completer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.*;



public class AdvanceReader {

	SuperMQLSupport gss;
	
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
		} else if ("exit".equals(strUserInput)) {
			
			AttributedStringBuilder a = new AttributedStringBuilder()
			.append("You will be returned to normal mode. You can use ")
			.append("mode adv", AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
			.append(" again to enter into advance mode.");
			
			System.out.println(a.toAnsi());
			
			return false;
		}
		else if(strUserInput.startsWith("myq "))
		{
			//storeMyQuery(strUserInput);
		}
		
		else {
			
		}
		
		return true;
	}
	private String readLine(LineReader reader, int iCounter) {
		try {
			String line = reader.readLine("Smql<"+iCounter+"> ");
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
		
		// enable ansi
		AnsiConsole.systemInstall(); 
		
		// setup commands
		commandsToComplete = new String[] { "exit", "mql", ". temp query bus TYPE NAME REV", ". print bus" };
		
		System.out.println("You are in Advance mode. Press TAB for list of auto complete commands.");
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