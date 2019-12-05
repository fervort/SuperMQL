package com.fervort.supermql.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MyQueryReader {

	public static Document doc;
	public static String configPath;
	
	private static final String XML_CONFIG_FILE_NAME = "SuperMQL.MyQuery.xml" ;
	
	/*
	public static String getConfigPath() { 
		return configPath;
	}
	
	public static void setConfigPath(String configPath) {
		configPath = configPath;
	}
	*/
	
	public static void addNewMyQuery(String strMyQuery,String strNativeQuery) throws ParserConfigurationException, IOException, SAXException, TransformerException
	{
		File xmlFile = new File(XML_CONFIG_FILE_NAME);
		
		if(xmlFile.exists())
		{
			//DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			//DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			//doc = docBuilder.parse(xmlFile);
			doc = getXMLDocument(MODE_XML_UPDATE,xmlFile);
			
			doc.getDocumentElement().normalize();
			
			NodeList nQueries = (NodeList) doc.getElementsByTagName("Queries");	
			if(nQueries.getLength()>=1)
			{
				Element eQueries = (Element) nQueries.item(0);
				
				addNewMyQuery(eQueries,strMyQuery,strNativeQuery);
				
				saveXMLDocument(doc, XML_CONFIG_FILE_NAME);
			}
			
		}else
		{

			doc = getXMLDocument(MODE_XML_CREATE);
            Element superMQLMyQuery = doc.createElement("SuperMQLMyQuery");
            doc.appendChild(superMQLMyQuery);
            
            //addNodeToXML(doc,superMQLMyQuery,"IsConfigCreated","yes");
            
            Element eQueries = doc.createElement("Queries");
            superMQLMyQuery.appendChild(eQueries);
            
            addNewMyQuery(eQueries,strMyQuery, strNativeQuery);
            
            configPath=saveXMLDocument(doc, XML_CONFIG_FILE_NAME);
           
            /*
            openFileAndUpdateElement("SuperMQL.Config.xml","IsConfigCreated2","hh");
            openFileAndCreateElement("SuperMQL.Config.xml","abc","pqr");
            openFileAndUpdateElement("SuperMQL.Config.xml","abc","xyz");
            */
		}

	}
	private static void addNewMyQuery(Element eQueries,String strMyQuery,String strNativeQuery)
	{
		Element eQuery = doc.createElement("Query");
		eQueries.appendChild(eQuery);
		
		addNodeWithTextContentToXML(doc,eQuery,"MyQuery",strMyQuery);
		addNodeWithTextContentToXML(doc,eQuery,"NativeQuery",strNativeQuery);
	}
	
	private static void openFileAndCreateElement2(String strFileName,String elementName,String elementValue) throws ParserConfigurationException, SAXException, IOException, TransformerException
	{
		File xmlFile = new File(strFileName);
		Document document = getXMLDocument(MODE_XML_UPDATE,xmlFile);
        
		Element superMQLMyQuery = doc.createElement("SuperMQLMyQuery");
        doc.appendChild(superMQLMyQuery);
        
        //addNodeToXML(doc,superMQLMyQuery,"IsConfigCreated","yes");
        
        Element eQueries = doc.createElement("Queries");
        superMQLMyQuery.appendChild(eQueries);
        
        addNewMyQuery(eQueries,"bus * * *", "temp query bus * * *");
        
        saveXMLDocument(doc, strFileName);
        
	}
	
	private static void openFileAndCreateElement(String strFileName,String elementName,String elementValue) throws ParserConfigurationException, SAXException, IOException, TransformerException
	{
		File xmlFile = new File(strFileName);
		Document document = getXMLDocument(MODE_XML_UPDATE,xmlFile);
        
		NodeList node = document.getElementsByTagName("SuperMQLConfiguration");
		Element parentNode = (Element) node.item(0);
        addNodeWithTextContentToXML(document, parentNode,elementName, elementValue);
        
        saveXMLDocument(document, strFileName);
	}
	
	private static void openFileAndUpdateElement(String strFileName,String elementName,String elementValue) throws ParserConfigurationException, SAXException, IOException, TransformerException
	{
		File xmlFile = new File(strFileName);
		Document document = getXMLDocument(MODE_XML_UPDATE,xmlFile);
        
        updateXMLElement(document,elementName, elementValue);
        
        saveXMLDocument(document, strFileName);
	}
	private static String saveXMLDocument(Document document,String strFileName) throws ParserConfigurationException, TransformerException, IOException
	{
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
        
        DOMSource domSource = new DOMSource(document);
        File fXML = new File(strFileName);
        StreamResult streamResult = new StreamResult(fXML);

        transformer.transform(domSource, streamResult);
        
        return fXML.getCanonicalPath();
	}
	
	private static String MODE_XML_CREATE = "CREATE";
	private static String MODE_XML_UPDATE = "UPDATE";
	
	private static Document getXMLDocument(String sMode,File... xmlFile) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        if(sMode.equals(MODE_XML_CREATE))
        	return documentBuilder.newDocument();
        else
        {	if(xmlFile.length>=1)
        	{
        		return documentBuilder.parse(xmlFile[0]);
        	}
        	else
        	{
        		System.out.println("File object required as second parameter");
        		return null;
        	}
        	
        }
	}
	private static void addNodeWithTextContentToXML(Document document,Element parentNode,String nodeName,String textNode)
	{
        Element eElement = document.createElement(nodeName);
        eElement.appendChild(document.createTextNode(textNode));
        parentNode.appendChild(eElement);
	}
	
	private static void updateXMLElement(Document document,String nodeName,String textNode)
	{
		NodeList childElement = document.getElementsByTagName(nodeName);
		if(childElement.getLength()>1)
		{
			Element element = (Element) childElement.item(0);
			element.setTextContent(textNode);
		}else
		{
			System.out.println("Element is not exist in the XML= "+nodeName);
		}
		// if want to update attribute by parent node
		
		/*
		NodeList childElement = parentNode.getElementsByTagName(nodeName);
		
		Element element = (Element) childElement.item(0);
		element.setTextContent(textNode);
		*/
	}
	
	public static String readConfigKey(String key)
	{
		NodeList elementNode = doc.getElementsByTagName(key);
		
		if(elementNode.getLength()>=1)
		{
			Element element = (Element) elementNode.item(0);
			return element.getTextContent();
		}else
		{
			return "";
		}
	}
	
}

