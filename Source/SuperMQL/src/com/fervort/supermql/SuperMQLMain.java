package com.fervort.supermql;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import matrix.db.Context;
import matrix.util.MatrixException;

public class SuperMQLMain {

	public static void main(String[] args) throws IOException, MatrixException {
	
		System.out.println("Invalid Call !");
		System.out.println("Call function invokeSuperMQL(Context context,String[] args) from JPO");
		new SuperMQLMain().invokeSuperMQL(null, args);
		
	}
	
	public void invokeSuperMQL(Context context,String[] args) throws IOException, MatrixException
	{
		// if passed directly execute as script
		if(args.length>=1)
		{	
			String fileContent = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8); 
			
			new GroovyScriptBuilder().buildGroovyScript(context,fileContent);
		}else
		{	
			SuperMQLSupport gss = new SuperMQLSupport();
			gss.setCurrentContext(context);
			gss.buildMQLCommand();
			// Show tip of the day before starting
			Scanner scanner = new Scanner(System.in);
			System.out.println("Welcome to SuperMQL");
			System.out.println("");
			System.out.println("To execute mql start with dot, For ex=> .temp query bus or .print bus");
			System.out.println("");
			System.out.println("SuperMQL, Version 0.0.0.alpha");
			System.out.println("");
			int i=1;
			
			String strUserInput;
			do
			{
				System.out.print("Smql<"+i+">");
				strUserInput = scanner.nextLine();
				
				if(strUserInput.trim().startsWith("."))
				{
					String strAsMQL = strUserInput.trim();
					try
					{
						String strMQL = strAsMQL.substring(1, strAsMQL.length()).trim();
						String strResult = gss.mql(strMQL);
						System.out.println(strResult);
					}catch(Exception ex)
					{
						System.out.println(ex);
					}
				}else //it is script
				{
					new GroovyScriptBuilder().buildGroovyScript(context,strUserInput);
				}
				
				
				
				i++;
			}while(!strUserInput.trim().equalsIgnoreCase("quit") && !strUserInput.trim().equalsIgnoreCase("exit"));
			System.out.println("Bye Bye :)");
			
		}
	}

}
