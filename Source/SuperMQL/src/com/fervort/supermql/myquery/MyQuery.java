package com.fervort.supermql.myquery;

import java.io.IOException;
import java.util.HashMap;
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
		String strResult = sms.mql(queryHolder.getNativeEvaluatedQuery());
		System.out.println("");
		System.out.println(strResult);
		
	}
	public MyQueryHolder getMatchingQuery(String strFiredMyQuery) throws ParserConfigurationException, SAXException, IOException
	{
		NodeList nlQuery = queryReader.getAllQueries();
		
		for(int i=0;i<nlQuery.getLength();i++)
		{
			if (nlQuery.item(i) instanceof Element) {
			Element nQuery = (Element)nlQuery.item(i);
			String strMyQuery = nQuery.getElementsByTagName(MyQueryConstants.MY_QUERY).item(0).getTextContent();
			String strNativeQuery =nQuery.getElementsByTagName(MyQueryConstants.NATIVE_QUERY).item(0).getTextContent();
			
			String strRegExQuery = getRegExForQuery(formatQuery(strMyQuery)); 


	        Pattern pattern = Pattern.compile(strRegExQuery);

	        Matcher matcher = pattern.matcher(formatQuery(strFiredMyQuery));
	        
		    if(matcher.matches())
		    {
		    	MyQueryHolder mqHolder = new MyQueryHolder();
		    	mqHolder.setMyQuery(strMyQuery);
		    	mqHolder.setNativeQuery(strNativeQuery);
		    	mqHolder.setMatcher(matcher);
		    	
		    	mqHolder.setNativeEvaluatedQuery(buildEvaluatedQuery(formatQuery(strNativeQuery), matcher));
		    	
		    	return mqHolder;
		    }
			}
		}
		return null;

	}
	
	private String formatQuery(String query)
	{
		return SuperUtilities.replaceMultipleSpaceToSingle(query.trim().toLowerCase());
	}
	
	private String buildEvaluatedQuery(String nativeQuery,Matcher matcher)
	{
		for(int j=1;j<=matcher.groupCount();j++)
		{
			nativeQuery = nativeQuery.replace("{"+j+"}","\""+matcher.group(j)+"\"");
		}
		System.out.println("Native Evaluated Query: "+nativeQuery);
		
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
