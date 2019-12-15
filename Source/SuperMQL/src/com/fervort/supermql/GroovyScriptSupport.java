package com.fervort.supermql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
		//System.out.println("GroovyScriptSupport run method called");
		return null;
	}
	Context context;
	public void setCurrentContext(Context context)
	{
		//System.out.println("set current context");
		this.context= context;
	}
	
	public Context getCurrentContext()
	{
		return this.context;
	}
	
	MQLCommand mqlCommand;
	
	public String mql(String strMQL) throws Exception
	{
		//System.out.println("inside MQL");
		//replace with MQLCommand.exec(context); Need new jar to build with this
		if (!mqlCommand.executeCommand(context, strMQL))
			throw new Exception("SuperMQL Exception: " + strMQL+" , "+mqlCommand.getError());
		return mqlCommand.getResult();
	}
	
	/**
	 * Returns MQL result as List separated by \n
	 * @param strMQL
	 * @return
	 * @throws Exception
	 */
	public List<String> mqlAsL(String strMQL) throws Exception
	{
		String strResult = mql(strMQL).trim();
		if(strResult.length()!=0)
		{
			String[] aResult = strResult.split("\n");
			return Arrays.asList(aResult);
		}
		return Collections.emptyList();
	}
	/**
	 * Returns MQL result as List of List. 
	 * Each record is separated by \n and selectables are separated by | 
	 * 
	 * @param strMQL
	 * @return
	 * @throws Exception
	 */
	// TODO Make separator character configurable. Read it from configuration and set 
	// TODO Also write function which gives same result but seprator will be passed by function like mqlAsLLBySeparator(mql,separator)
	public List<List<String>> mqlAsLL(String strMQL) throws Exception
	{
		List<List<String>> lFinalList = new ArrayList<List<String>>();
		
		List<String> lResult = mqlAsL(strMQL);
		for (String sResultRow : lResult) {
			String[] aRows = sResultRow.split("\\|",-1);
			List<String> lRows = Arrays.asList(aRows);
			lFinalList.add(lRows);
		}
		return lFinalList;
	}

	public void buildMQLCommand() throws MatrixException
	{
		mqlCommand = new MQLCommand();
		mqlCommand.open(this.context);
		//System.out.println("Build MQL Command");
	}
}
