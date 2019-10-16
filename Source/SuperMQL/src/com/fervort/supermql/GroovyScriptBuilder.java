package com.fervort.supermql;

import org.codehaus.groovy.control.CompilerConfiguration;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;


public class GroovyScriptBuilder {

	public void buildGroovyScript(String strScriptText)
	{
	
		CompilerConfiguration config = new CompilerConfiguration();                                    
		config.setScriptBaseClass("com.fervort.supermql.GroovyScriptSupport");     
		

		GroovyShell shell = new GroovyShell(GroovyScriptBuilder.class.getClassLoader(), new Binding(), config) ; 
		GroovyScriptSupport script = (GroovyScriptSupport) shell.parse(strScriptText) ;                                        
		script.setScriptName("MQLScript");
		
		script.run() ;
		
		
	}
}
