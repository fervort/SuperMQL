package com.fervort.supermql;

import groovy.lang.Script;
import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.MatrixException;

abstract public class GroovyScriptSupport extends Script{

	String strScriptName;

	public String getScriptName() {
		return strScriptName;
	}

	public void setScriptName(String strScriptName) {
		this.strScriptName = strScriptName;
	}
	
	public String printScriptName() {
		System.out.println("Script name is "+strScriptName);
		return "";
	}
	
	
	
	@Override
	public Object run() {
		System.out.println("GroovyScriptSupport run method called");
		return null;
	}
	Context context;
	public void setCurrentContext(Context context)
	{
		System.out.println("set current context");
		this.context= context;
	}
	
	public Context getCurrentContext()
	{
		return this.context;
	}
	
	MQLCommand mqlCommand;
	
	public String mql(String strMQL) throws Exception
	{
		System.out.println("inside MQL");
		//replace with MQLCommand.exec(context); Need new jar to build with this
		if (!mqlCommand.executeCommand(context, strMQL))
			throw new Exception("SuperMQL Exception: " + strMQL+" , "+mqlCommand.getError());
		return mqlCommand.getResult();
	}
	
	public void buildMQLCommand() throws MatrixException
	{
		mqlCommand = new MQLCommand();
		mqlCommand.open(this.context);
		System.out.println("Build MQL Command");
	}
}
