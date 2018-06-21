/*************************************************************
 * This file is part of CB2XML.
 * See the file "LICENSE" for copyright information and the
 * terms and conditions for copying, distribution and
 * modification of CB2XML.
 *************************************************************
 */
package org.dh.p400.translator;

import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
* this class handles converting a Hashtable representation of data into
* its copybook equivalent StringBuffer (mainframe equivalent)
* it recurses the DOM of the copybook definition (as XML) and checks if a
* hashtable key exists for each data node.  if found, it packs the hashtable
* value into the output string buffer and continues
* the weird logic you may see would be to
*
* 1) checking for a leading 1 or 0 in the StringBuffer to identify if there was a child
*    element value in the hashtable corresponding to the branch (or child branches) being traversed
*    resorted to this 'hack' to simplify the signature of the method being recursed
*
* 2) due to 'REDEFINES' complications, a decision has to be made if the hashtable
*    has data for more than one of the redefined instances (should be XOR)
*    below, the code selects the first XOR instance, so attempts to ignore inconsistent inputs
*
* TODO : the special mainframe formats of COMP, packed-decimal etc are not supported yet
*
*  * note that files within the "net.sf.cb2xml.convert" package are not stable
*
* @author Peter Thomas
*/
public class NewHashtableToMainframe
{
    private Hashtable keyValuePairs = null;

    private StringBuffer getRepeatedChars( char charToRepeat, int count )
    {
        StringBuffer sb = new StringBuffer(  );

        for ( int i = 0; i < count; i++ )
        {
            sb.append( charToRepeat );
        }

        return sb;
    }

    public String convert( Hashtable keyValuePairs, Document copyBookXml ) throws InvalidValueException
    {
        this.keyValuePairs = keyValuePairs;

        //Element element = (Element) copyBookXml.getDocumentElement().getFirstChild();
        //System.out.println ("DEBUG" + copyBookXml.getDocumentElement ().getFirstChild());

        //System.out.println ("DEBUG" + copyBookXml.getDocumentElement().getFirstChild().getClass());
        Element element = (Element) copyBookXml.getDocumentElement(  )
                                               .getFirstChild(  );

        //ORIG String xpath = "/" + copyBookXml.getDocumentElement().getTagName() + "/" + element.getTagName();
        String xpath = "/" + copyBookXml.getDocumentElement(  ).getTagName(  ) +
            "/" + element.getAttribute( "name" ); //Sudhakar

        return convertNode( element, xpath ).deleteCharAt( 0 ).toString(  );
    }

    private StringBuffer convertNode( Element element, String xpath ) throws InvalidValueException
    {
        StringBuffer segment = new StringBuffer(  );
        segment.append( '0' );
        boolean test=true;

        //ORIG String elementName = element.getTagName ();
        String elementName = element.getAttribute( "name" ); //Sudhakar
        

       
        int position = Integer.parseInt( element.getAttribute( "position" ) );
        int length = Integer.parseInt( element.getAttribute( "display-length" ) ); //Integer.parseInt(element.getAttribute("length"));
        String numeric = element.getAttribute( "numeric" );
        String picture = element.getAttribute( "picture" );

        int childElementCount = 0;
        NodeList nodeList = element.getChildNodes(  );

        for ( int i = 0; i < nodeList.getLength(  ); i++ )
        {
            org.w3c.dom.Node node = nodeList.item( i );

            if ( node.getNodeType(  ) == org.w3c.dom.Node.ELEMENT_NODE )
            {
                Element childElement = (Element) node;

                //XXX HACK to skip <condition> elements
             
                if ( "condition".equals( childElement.getNodeName(  ) ) )
                {
                    continue;
                }

                //XXX END OF HACK
                if ( !childElement.getAttribute( "level" ).equals( "88" ) )
                {
                    //DEBUG System.out.println( childElement.getAttribute( "name" ) );

                    //ORIG String childElementName = childElement.getTagName ();
                    String childElementName = childElement.getAttribute( "name" ); //Sudhakar
                    childElementCount++;

                    int childPosition = Integer.parseInt( childElement.getAttribute( 
                                "position" ) );
                    StringBuffer tempBuffer = null;
                   

                    if ( childElement.hasAttribute( "occurs" ))
                    {
                        tempBuffer = new StringBuffer(  );
                        
                        tempBuffer.append( '0' );

                        int childOccurs = Integer.parseInt( childElement.getAttribute( 
                                    "occurs" ) );

                        //ORIG int childLength = Integer.parseInt (childElement.getAttribute ( "length" ));
                        int childLength = Integer.parseInt( childElement.getAttribute( 
                                    "display-length" ) );
                        int singleChildLength = childLength / childOccurs;

                        for ( int j = 0; j < childOccurs; j++ )
                        {
                        	
                        	StringBuffer	occursBuffer = convertNode( childElement,
                                    xpath + "/" + childElementName + "[" + j +
                                    "]" );
                        	
                           

                            if ( occursBuffer.charAt( 0 ) == '1' )
                            {
                                tempBuffer.setCharAt( 0, '1' );
                            }

                            occursBuffer.deleteCharAt( 0 );
                            tempBuffer.append( occursBuffer );
                        	
                            
                        }
                    }
                    else
                    {
                        tempBuffer = convertNode( childElement,
                                xpath + "/" + childElementName );
                        
                        
                    }

                    if ( childElement.hasAttribute( "redefines" ) &&
                            ( tempBuffer.charAt( 0 ) == '1' ) )
                    {
                        tempBuffer.deleteCharAt( 0 );

                        int replacePosition = childPosition - position;
                        segment.replace( replacePosition,
                            replacePosition + tempBuffer.length(  ),
                            tempBuffer.toString(  ) );
                    }
                    else
                    {
                        if ( tempBuffer.charAt( 0 ) == '1' )
                        {
                            segment.setCharAt( 0, '1' );
                        }

                        tempBuffer.deleteCharAt( 0 );
                        segment.append( tempBuffer );
                    }
                }
            }
        }

        if ( childElementCount == 0 )
        {
        	//System.out.println(xpath);
        	
            if ( keyValuePairs.containsKey( xpath ) )
            {
            	System.out.println(xpath);
            	String value = (String) keyValuePairs.get( xpath );
                segment.setCharAt( 0, '1' );
                segment.append( XmlToMainframePadding.transformXmlToMainframe( 
                        elementName, picture,value ) );

            
            }
            else
            {
                if ( element.hasAttribute( "value" ) )
                {
                	                	
                    //segment.append( element.getAttribute( "value" ) );
                    
                    String value = element.getAttribute( "value" );
                    //segment.setCharAt( 0, '1' );
                    segment.append( XmlToMainframePadding.transformXmlToMainframe( 
                            elementName, picture, value ) );
                    
                }
                else if ( element.hasAttribute( "spaces" ) )
                {
                	// ORIG segment.append( getRepeatedChars( ' ', length ) );
                	
                	
                	Element node = (Element) element.getParentNode();
                	if (!node.hasAttribute("redefines"))
                	{
                		
                		segment.append( XmlToMainframePadding.transformXmlToMainframe( 
                                elementName, picture, " " ) );	
                	}
                	
                }
                else if ( element.hasAttribute( "zeros" ) )
                {
                	//ORIG segment.append( getRepeatedChars( '0', length ) );
                	
                	
                	Element node = (Element) element.getParentNode();
                	if (!node.hasAttribute("redefines"))
                	{
                		
                		if(element.getAttribute("zeros").equals("false"))
                		{
                			 segment.append( XmlToMainframePadding.transformXmlToMainframe( 
                                     elementName, picture, "S" ) );
                		}else
                             segment.append( XmlToMainframePadding.transformXmlToMainframe( 
                                elementName, picture, "0" ) );
                	}
                	

                }
            }
        }

        return segment;
    }
}
