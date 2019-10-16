package com.fervort.supermql;

import groovy.lang.Script;

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
}
