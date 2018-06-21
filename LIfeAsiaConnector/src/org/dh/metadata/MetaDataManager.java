package org.dh.metadata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.db.utils.PropertyUtils;
import org.db.utils.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.cb2xml.Cb2Xml;

public class MetaDataManager {

	private String m_strFilesDirectory; 		//directory where copybooks are stored
	private Map<String, String> cbMetadata =new HashMap<String,String>();
	private XMLUtils utils=XMLUtils.getInstance();

	private static MetaDataManager manager;
	private Logger logger =LoggerFactory.getLogger(MetaDataManager.class);

	private MetaDataManager() {
		m_strFilesDirectory =PropertyUtils.getValue("com.lifeasia.bo.location");
	}

	public static MetaDataManager getInstance(){
		if(manager ==null){
			manager =new MetaDataManager();
		}
		return manager;
	}

	/**
	 * This method is used to get the metadata of the copybook name, the metadata generated also contain
	 * the condition tags in it, to remove the condition tag either call the removeCondionTag method 
	 * or iterate over the metadata to delete the condition tag yourself.
	 *
	 * @param copybookFilename 	:Name of the copybook for which you want metadata.
	 * @return					:Copybook Metadata
	 * @throws IOException
	 */
	public String getCopyBookMetaData(String copybookFilename) throws IOException {

		String l_strCopyBookAbsolutePath = m_strFilesDirectory + "/" + copybookFilename;
		File l_fileCopyBook = new File(l_strCopyBookAbsolutePath);
		if (!l_fileCopyBook.exists()) {
			throw new IOException("CopyBook: " + l_strCopyBookAbsolutePath + " not found");
		}

		return Cb2Xml.convertToXMLString(l_fileCopyBook);
	}


	public Map<String, String> getCopybookMetadata(String[] copybookNames) throws IOException{
		String metadata;
		String cbName;
		for (int i = 0; i < copybookNames.length; i++) {
			cbName =copybookNames[i];
			metadata =removeConditionTag(getCopyBookMetaData(cbName));
			cbMetadata.put(cbName.substring(0, cbName.indexOf('.')),metadata);
		}
		return cbMetadata;
	} 

	/**
	 * This method will read the copybook files from the directory and load the copybook metadata in a hashmap againt each copybook for further use, 
	 * so that we do not have to generate the metadata from the copybook again.
	 * @throws IOException
	 */
	public void loadAllCopybookMetada() throws IOException{
		String[] cbList =getCopyBooksList();
		getCopybookMetadata(cbList);
	}

	/*
	 * Method returns list of copybooks present in given folder
	 *
	 * @returns Xml node containign list of copybook file
	 */
	public String[] getCopyBooksList() throws IOException {
		File l_fileDirectory = new File(m_strFilesDirectory);

		if (l_fileDirectory.exists()) {
			String[] l_straFiles = l_fileDirectory.list();
			return l_straFiles;
		} else {
			throw new IOException("Meta Data folder not found");
		}
	}


	/**
	 * This method is called to removed the condition node from the copybook metadata, 
	 * these condition tag are not used while transforming the request/response to mainframe format so these
	 * tag has to be remove from the metadata structure.
	 * We will iterator over the metadata passed and remove all the condition tag from the nested structure
	 * 
	 * @param metadata : Copybook metadata from which the condition tag has to be removed.
	 * @return		   : Copybook metadata without any condition tag.
	 */
	public String removeConditionTag(String metadata){
		logger.info("Removing all the conditional tag from metadats");
		Document document =utils.stringToDom(metadata);
		Element rooElement =document.getDocumentElement();
		rooElement.normalize();
		remove88(rooElement);

		return utils.prettyPrint(document);

	}
	protected void remove88(Node element) {
		NodeList childs =element.getChildNodes();
		for(int i=0;i<childs.getLength();i++){
			Node node =childs.item(i);
			if("condition".equalsIgnoreCase(node.getNodeName())){
				element.removeChild(node);
				i--;
			}
			if(node.hasChildNodes()){
				
				remove88(node);
			}
				
			String l_strDataType =((Element)node).getAttribute("picture");
				
				if (l_strDataType != null) {
					if (l_strDataType.startsWith("X")) {
						// PICTURE type is X i.e. string type
						((Element)node).setAttribute("type", "string");
						((Element)node).setAttribute("spaces", "true");
					}

					else if (l_strDataType.startsWith("9")) {
						if ((l_strDataType.indexOf(".") > 0) || (l_strDataType.indexOf("V") > 0)) {
							// PICTURE type is Numeric and real type
							((Element)node).setAttribute("type", "double");
							((Element)node).setAttribute("zeros", "true");
						} else {
							// PICTURE type is Numeric and integer type
							// Node.removeAttribute ( p_iItemNode, "picture" );
							((Element)node).setAttribute("type", "int");
							((Element)node).setAttribute("zeros", "true");
						}
					}
				}
		}	

	}

	public void putMetadata(String cbName,String metadata){
		this.cbMetadata.put(cbName, metadata);
	}
	public Map<String, String> getCopybookMetadataCollection() throws IOException{
		if(this.cbMetadata ==null || this.cbMetadata.isEmpty()){
			loadAllCopybookMetada();
		}
		return this.cbMetadata;
	}

	public String getMetadata(String cbName){
		return this.cbMetadata.get(cbName);
	}

	/*
	 * Internal method for normalizing copybook format from <item>
	 *
	 * @param Xml node containing copybook metadata
	 */
	//	protected int createRequestFormatforMetaData(int p_iMetaDataXml) {
	//		int l_iRequestFormat = Node.clone(p_iMetaDataXml, true);
	//		int l_iCopyBookNode = Node.getFirstChild(l_iRequestFormat);
	//		recurrRenameNode(l_iCopyBookNode);
	//		return l_iRequestFormat;
	//	}

	/*
	 * Internal method for normalizing copybook "item" nodes data
	 *
	 * @param item XMl node to be normalized
	 */
	//	protected void recurrRenameNode(int p_iItemNode) {
	//
	//		// To avoid level 88 related nodes
	//		int[] l_iaConditions = Find.match(p_iItemNode, "?<condition>parent");
	//		int l_iNoOfConditions = l_iaConditions.length;
	//
	//		for (int l_iCnt = 0; l_iCnt < l_iNoOfConditions; l_iCnt++) {
	//			Node.delete(Node.getFirstChildElement(l_iaConditions[l_iCnt]));
	//			l_iaConditions[l_iCnt] = 0;
	//		}
	//
	//		int l_iChildItemNode = Node.getFirstChild(p_iItemNode);
	//
	//		if (l_iChildItemNode > 0) {
	//			recurrRenameNode(l_iChildItemNode);
	//		}
	//
	//		int l_iSiblingNode = 0;
	//
	//		l_iSiblingNode = Node.getNextSibling(l_iChildItemNode);
	//
	//		while (l_iSiblingNode > 0) {
	//			recurrRenameNode(l_iSiblingNode);
	//
	//			l_iSiblingNode = Node.getNextSibling(l_iSiblingNode);
	//		}
	//
	//		translateNode(p_iItemNode);
	//	}

	/*
	 * Method to change item node name and attributes
	 *
	 * @param item xml node
	 */
	//	protected void translateNode(int p_iItemNode) {
	//
	//		String l_strNodeName;
	//
	//		l_strNodeName = Node.getAttribute(p_iItemNode, "name");
	//
	//		/*
	//		 * rename item node from "item" to name retrieved from "name" attribute
	//		 *
	//		 */
	//		Node.setName(p_iItemNode, "element"); // l_strNodeName
	//		Node.setAttribute(p_iItemNode, "xmlns", "http://www.w3.org/2001/XMLSchema");
	//		String l_strDataType = Node.getAttribute(p_iItemNode, "picture");
	//
	//		if (l_strDataType != null) {
	//			if (l_strDataType.startsWith("X")) {
	//				// PICTURE type is X i.e. string type
	//				Node.setAttribute(p_iItemNode, "type", "string");
	//			}
	//
	//			else if (l_strDataType.startsWith("9")) {
	//				if ((l_strDataType.indexOf(".") > 0) || (l_strDataType.indexOf("V") > 0)) {
	//					// PICTURE type is Numeric and real type
	//					Node.setAttribute(p_iItemNode, "type", "double");
	//				} else {
	//					// PICTURE type is Numeric and integer type
	//					// Node.removeAttribute ( p_iItemNode, "picture" );
	//					Node.setAttribute(p_iItemNode, "type", "int");
	//				}
	//			}
	//		}
	//
	//		// Remove unnecessary attributes
	//		Node.removeAttribute(p_iItemNode, "display-length");
	//		Node.removeAttribute(p_iItemNode, "numeric");
	//		Node.removeAttribute(p_iItemNode, "length");
	//		Node.removeAttribute(p_iItemNode, "scale");
	//		Node.removeAttribute(p_iItemNode, "sign-separate");
	//		Node.removeAttribute(p_iItemNode, "signed");
	//		Node.removeAttribute(p_iItemNode, "redefines");
	//		Node.removeAttribute(p_iItemNode, "occurs");
	//		Node.removeAttribute(p_iItemNode, "display");
	//		Node.removeAttribute(p_iItemNode, "level");
	//		Node.removeAttribute(p_iItemNode, "picture");
	//		Node.removeAttribute(p_iItemNode, "position");
	//
	//	}



}
