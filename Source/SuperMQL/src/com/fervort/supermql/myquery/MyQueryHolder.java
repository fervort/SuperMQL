package com.fervort.supermql.myquery;

import java.util.List;
import java.util.regex.Matcher;

public class MyQueryHolder {

	private String strMyQuery ;
	private List<String> lNativeQuery;
	private List<String> lNativeEvaluatedQuery;
	
	private Matcher matcher;
	
	
	public Matcher getMatcher() {
		return matcher;
	}
	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}
	
	public List<String> getNativeQuery() {
		return lNativeQuery;
	}
	public void setNativeQuery(List<String> lNativeQuery) {
		this.lNativeQuery = lNativeQuery;
	}
	public String getMyQuery() {
		return strMyQuery;
	}
	public void setMyQuery(String strMyQuery) {
		this.strMyQuery = strMyQuery;
	}
	
	public List<String> getNativeEvaluatedQuery() {
		return lNativeEvaluatedQuery;
	}
	public void setNativeEvaluatedQuery(List<String> lNativeEvaluatedQuery) {
		this.lNativeEvaluatedQuery = lNativeEvaluatedQuery;
	}
	
	
	
}
