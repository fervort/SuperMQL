package com.fervort.supermql;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SuperMQLMain {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Super MQL called ");
		
		
		//String fileContent = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8); 
		//System.out.println("Contents  : " + fileString);
			
		//new GroovyScriptBuilder().buildGroovyScript(fileContent);
		new GroovyScriptBuilder().buildGroovyScript("printScriptName()");
		
	}

}
