package com.fervort.supermql.myquery;

import java.util.regex.Matcher;

public class MyQueryHolder {

	private String strMyQuery ;
	private String strNativeQuery;
	private String strNativeEvaluatedQuery;
	
	private Matcher matcher;
	
	
	public Matcher getMatcher() {
		return matcher;
	}
	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}
	
	public String getNativeQuery() {
		return strNativeQuery;
	}
	public void setNativeQuery(String strNativeQuery) {
		this.strNativeQuery = strNativeQuery;
	}
	public String getMyQuery() {
		return strMyQuery;
	}
	public void setMyQuery(String strMyQuery) {
		this.strMyQuery = strMyQuery;
	}
	
	public String getNativeEvaluatedQuery() {
		return strNativeQuery;
	}
	public void setNativeEvaluatedQuery(String strNativeQuery) {
		this.strNativeQuery = strNativeQuery;
	}
	
	
	
}
