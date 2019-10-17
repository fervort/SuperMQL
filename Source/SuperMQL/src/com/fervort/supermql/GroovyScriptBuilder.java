package com.fervort.supermql;

import org.codehaus.groovy.control.CompilerConfiguration;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import matrix.db.Context;
import matrix.util.MatrixException;


public class GroovyScriptBuilder {

	public void buildGroovyScript(Context context,String strScriptText) throws MatrixException
	{
	
		CompilerConfiguration config = new CompilerConfiguration();                                    
		config.setScriptBaseClass("com.fervort.supermql.GroovyScriptSupport");     
		

		GroovyShell shell = new GroovyShell(GroovyScriptBuilder.class.getClassLoader(), new Binding(), config) ; 
		GroovyScriptSupport script = (GroovyScriptSupport) shell.parse(strScriptText) ;                                        
		script.setScriptName("MQLScript");
		
		// Sequence should not be change
		script.setCurrentContext(context);
		script.buildMQLCommand();
		
		
		script.run() ;
		
		
	}
}
