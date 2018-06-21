package org.db.utils;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class XMLUtils
{
    private DocumentBuilder dBuilder;
    private static XMLUtils utils;
	
    /**
     * So that you can't create object of this class.
     */
    private XMLUtils(){
    	
    }
    
    private void initialize(){
    	try{
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	factory.setIgnoringComments (true);
    	
    	dBuilder =factory.newDocumentBuilder();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
     public static XMLUtils getInstance(){
    	 if(utils ==null){
    		 utils =new XMLUtils();
    		 utils.initialize();
    	 }
    	 return utils;
     }
    
     public DocumentBuilder getDocumentBuilder(){
     	
     	return dBuilder;
     }
	public Document stringToDom(String p_strXmlString)
    {
        Document l_docDom = null;
        try
        {
            l_docDom = dBuilder.parse ( new InputSource( 
                        new StringReader( p_strXmlString ) ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return l_docDom;
    }

    public Document fileToDom ( String fileName )
    {	Document document =null; 
        try
        {
            document = dBuilder.parse ( new File( fileName ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace (  );
        }
        return document;
    }
    
    public Document createDocumentFromElement(Node element){
		Document document =dBuilder.newDocument();
		Node node =document.importNode(element, true);
		document.appendChild(node);
		return document;
	}

	public String prettyPrint(Document document){
		StringWriter sw=null;
		try{
			DOMSource domSource = new DOMSource(document); 
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sw.toString();

	}

	public Document createNewDocument(){
		return dBuilder.newDocument();
	}
	
	

}
