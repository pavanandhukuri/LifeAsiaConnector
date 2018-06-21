/*************************************************************
 * This file is part of CB2XML.
 * See the file "LICENSE" for copyright information and the
 * terms and conditions for copying, distribution and
 * modification of CB2XML.
 *************************************************************
 */
package org.dh.p400.translator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Hashtable;



public class NewXmlToMainframe
{
    private Hashtable keyValuePairs = new Hashtable(  );

    //    public String convert( Document sourceDocument, Document copyBookXml )
    //    {
    //        Node l_node = sourceDocument.getDocumentElement(  ).getFirstChild(  );
    //        System.out.println( l_node.getClass(  ) );
    //
    //        Node element = sourceDocument.getDocumentElement(  )
    //                                                    .getFirstChild(  );
    //        String xpath = "/" +
    //            sourceDocument.getDocumentElement(  ).getTagName(  ) + "/" +
    //            element.getTagName(  );
    //        convertNode( element, xpath );
    //
    //        //FileUtils.writeFile(keyValuePairs.toString(), "hashtable.txt", false);
    //        return new HashtableToMainframe(  ).convert( keyValuePairs, copyBookXml );
    //    }
    public String convert ( Document sourceDocument, Document copyBookXml ) throws InvalidValueException
    {
        Element l_element = sourceDocument.getDocumentElement (  );

        //System.out.println ( sourceDocument.toString() );

        //ORIG Element element = (Element) sourceDocument.getDocumentElement ().getFirstChild ();
        Element element = (Element) sourceDocument.getDocumentElement (  )
                                                  .getFirstChild (  );
        String xpath = "/" +
            sourceDocument.getDocumentElement (  ).getTagName (  ) + "/" +
            element.getTagName (  );
        convertNode ( element, xpath );

        //FileUtils.writeFile(keyValuePairs.toString(), "hashtable.txt", false);
        return new NewHashtableToMainframe(  ).convert ( keyValuePairs,
            copyBookXml );
    }

    private void convertNode ( Element element, String xpath )
    {
        NodeList nodeList = element.getChildNodes (  );
        //System.out.println(xpath);
        Hashtable childHash = new Hashtable( nodeList.getLength (  ), 1 );
        int index = 0;

        for ( int i = 0; i < nodeList.getLength (  ); i++ )
        {
            org.w3c.dom.Node childNode = nodeList.item ( i );

            if ( childNode.getNodeType (  ) == org.w3c.dom.Node.ELEMENT_NODE )
            {
                Element childElement = (Element) childNode;
                String childElementName = childElement.getTagName (  );
                

                if ( childHash.containsKey ( childElementName ) )
                {
                    index = Integer.parseInt ( childHash.get ( childElementName )
                                                        .toString (  ) ) + 1;
                    childHash.put ( childElementName, index + "" );
                    
                }
                else
                {
                    childHash.put ( childElementName, "0" );
                    
                }

                childElement.setAttribute ( "index", index + "" );
            }
            else if ( childNode.getNodeType (  ) == org.w3c.dom.Node.TEXT_NODE )
            {
                keyValuePairs.put ( xpath, childNode.getNodeValue (  ) );
            }
        }

        for ( int i = 0; i < nodeList.getLength (  ); i++ )
        {
            org.w3c.dom.Node childNode = nodeList.item ( i );

            if ( childNode.getNodeType (  ) == org.w3c.dom.Node.ELEMENT_NODE )
            {
                Element childElement = (Element) childNode;
                String childElementName = childElement.getTagName (  );

                if ( !childHash.get ( childElementName ).equals ( "0" ) )
                {
                	//System.out.println(childHash.get ( childElementName ).toString());
                	//System.out.println(childElementName+"  :"+childElement.getAttribute("index"));
                	
                    convertNode ( childElement,
                        xpath + "/" + childElementName + "[" +
                        childElement.getAttribute ( "index" ) + "]" );
                }
                else
                {
                    convertNode ( childElement, xpath + "/" + childElementName );
                    
                }
            }
        }
    }
}
