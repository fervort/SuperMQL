package com.fervort.supermql.myquery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fervort.supermql.SuperMQLSupport;
import com.fervort.supermql.SuperUtilities;
import com.fervort.supermql.xml.MyQueryReader;

public class MyQuery {
	
	MyQueryReader queryReader;
	
	public MyQuery() {
		 queryReader = new MyQueryReader();
	}

	public void processMyQuery(SuperMQLSupport sms,String strFiredMyQuery) throws Exception
	{
		MyQueryHolder queryHolder = getMatchingQuery(strFiredMyQuery);
		
		List<String> lNativeEvaluatedQuery = queryHolder.getNativeEvaluatedQuery();
		
		for (String sEvaluatedQuery : lNativeEvaluatedQuery) {
			
			System.out.println("Native: "+sEvaluatedQuery);
			String strResult = sms.mql(sEvaluatedQuery);
			System.out.println("");
			System.out.println(strResult);
			System.out.println("");
			System.out.println("");
		}
		
		
	}
	public MyQueryHolder getMatchingQuery(String strFiredMyQuery) throws ParserConfigurationException, SAXException, IOException
	{
		NodeList nlQuery = queryReader.getAllQueries();
		
		for(int i=0;i<nlQuery.getLength();i++)
		{
			if (nlQuery.item(i) instanceof Element) {
			Element nQuery = (Element)nlQuery.item(i);
			String strMyQuery = nQuery.getElementsByTagName(MyQueryConstants.MY_QUERY).item(0).getTextContent();
			
			NodeList nlNativeQueries = nQuery.getElementsByTagName(MyQueryConstants.NATIVE_QUERY);
			
			List<String> lNativeQueries = new ArrayList<String>();
			
			for(int j=0;j<nlNativeQueries.getLength();j++)
			{
				Node nNativeQuery = nlNativeQueries.item(j);
				lNativeQueries.add(nNativeQuery.getTextContent());
			}
			
			String strRegExQuery = getRegExForQuery(formatQuery(strMyQuery)); 


	        Pattern pattern = Pattern.compile(strRegExQuery);

	        Matcher matcher = pattern.matcher(formatQuery(strFiredMyQuery));
	        
		    if(matcher.matches())
		    {
		    	MyQueryHolder mqHolder = new MyQueryHolder();
		    	mqHolder.setMyQuery(strMyQuery);
		    	mqHolder.setNativeQuery(lNativeQueries);
		    	mqHolder.setMatcher(matcher);
		    	
		    	mqHolder.setNativeEvaluatedQuery(buildEvaluatedQueryByList(formatQueryList(lNativeQueries), matcher));
		    	
		    	return mqHolder;
		    }
			}
		}
		return null;

	}
	private List<String> formatQueryList(List<String> lList)
	{
		List<String> lFormatedQueries = new ArrayList<String>(); 
		
		for (String string : lList) {
			lFormatedQueries.add(formatQuery(string));
		}
		return lFormatedQueries;
	}
	// TODO should we replace last occurance of ; with blank ?
	private String formatQuery(String query)
	{
		return SuperUtilities.replaceMultipleSpaceToSingle(query.trim());
	}
	
	private List<String> buildEvaluatedQueryByList(List<String> lList,Matcher matcher)
	{
		List<String> lEvaluatedQueryList = new ArrayList<String>(); 
		
		for (String string : lList) {
			lEvaluatedQueryList.add(buildEvaluatedQuery(string,matcher));
		}
		return lEvaluatedQueryList;
	}
	
	private String buildEvaluatedQuery(String nativeQuery,Matcher matcher)
	{
		for(int j=1;j<=matcher.groupCount();j++)
		{
			nativeQuery = nativeQuery.replace("{"+j+"}","\""+matcher.group(j)+"\"");
		}
		//System.out.println("Native Evaluated Query: "+nativeQuery);
		
		return nativeQuery;
	}
	
	/**
	 * Validate both my query as well as native query before saving inn XML.
	 * Native query and my query both should start with {1} and not with {0} 
	 * 
	 */
	private String validateNewQuery()
	{
		return null;
	}
	/**
	 * Replace all occurances of {1} {2} with regex "(.*?)"
	 * 
	 * Used \{\d\} regular expression to replace all occurances.
	 * 
	 * @param strQuery
	 * @return
	 */
	public String getRegExForQuery(String strQuery)
	{
		return strQuery.replaceAll("\\{\\d\\}", "\"(.*?)\""); // "(.*?)"
	}
}
