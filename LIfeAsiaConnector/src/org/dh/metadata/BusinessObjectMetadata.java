package org.dh.metadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.db.utils.XMLUtils;
import org.dh.modal.BusinessObject;
import org.dh.modal.Copybook;
import org.dh.modal.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BusinessObjectMetadata {

	private static Copybook session;
	private static Copybook leader;
	private MetaDataManager manager;

	private XMLUtils utility;
	//private static BusinessObjectMetadata boMetadataBuilder =null;
	private Logger logger =LoggerFactory.getLogger(BusinessObjectMetadata.class);

	public BusinessObjectMetadata() {
		super();
		utility =XMLUtils.getInstance();
		manager =MetaDataManager.getInstance();
	}

	public BusinessObject generateMetadata(String boName,String serviceType,String laFileName) throws Exception{
		logger.info("Generating Metadata for copybook:{}",laFileName);
		BusinessObject modal =new BusinessObject(boName,serviceType); 
		modal.setLeader(getLeaderHeader());
		if("REQUEST".equalsIgnoreCase(serviceType)){
			modal.setSession(getSessionHeader());
		}
		modal.setPayload(getPayload(laFileName));
		return modal;
	}

	/**
	 * This method is use to get the session copybook object. Since session copybook is same for all business object 
	 * instead of creating a new session copybook for each request, we will create it once and store it in the memory so that
	 * we don't have to create it again. Whenever a request is raised for a session object we will check if it is available if yes,
	 * then we will return the same else we will create a new session object store it in memory and return.
	 *
	 * @return Session Copybook Object
	 * @throws IOException
	 * @throws SAXException
	 */
	public Copybook getSessionHeader() throws IOException, SAXException{

		if(session ==null){
			logger.info("Generating session metadata for: SESSIONI.txt");
			session =new Copybook();	
			String copybook =manager.getCopyBookMetaData("SESSIONI.txt");
			logger.info("Session copybook:, {}",copybook);
			Document dSessionCopybook =utility.stringToDom(copybook);
			dSessionCopybook.getDocumentElement().normalize();

			Element rootNode =dSessionCopybook.getDocumentElement();
			session.setName(rootNode.getAttribute("filename"));
			logger.info("Session metadata creation started");
			session.setData(getCopybookMetadata(rootNode));
			logger.info("Session metadata created successfully");
			manager.putMetadata("SESSIONI", utility.prettyPrint(dSessionCopybook));
		}
		return session;
	}

	public List<Item> getCopybookMetadata(Node rootNode){

		List<Item> itemList =new ArrayList<Item>();
		Item item =null;
		NodeList childs =rootNode.getChildNodes();

		for(int i=0;i<childs.getLength();i++){
			Node node =childs.item(i);
			if("condition".equalsIgnoreCase(node.getNodeName())){
				rootNode.removeChild(node);
				i--;
				continue;
			}
			item =new Item();
			//set name
			NamedNodeMap attrs =node.getAttributes();
			item.setName(attrs.getNamedItem("name").getNodeValue());
			Node occurance =attrs.getNamedItem("occurs");
			if(occurance !=null){
				item.setOccurs(Integer.parseInt(occurance.getNodeValue()));
			}else{
				item.setOccurs(0);
			}
			if(attrs.getNamedItem("type") !=null)
				item.setType(attrs.getNamedItem("type").getNodeValue());
			if(node.hasChildNodes()){
				if(!"condition".equalsIgnoreCase(node.getFirstChild().getNodeName()))
					item.setChild(getCopybookMetadata(node));
			}
			itemList.add(item);
		}

		return itemList;
	}

	/**
	 * This method is use to get the leader copybook object. Since leader copybook is same for all business object 
	 * instead of creating a new leader copybook for each request, we will create it once and store it in the memory so that
	 * we don't have to create it again. Whenever a request is raised for a leader object we will check if it is available if yes,
	 * then we will return the same else we will create a new leader object store it in memory and return.
	 *
	 * @return Session Copybook Object
	 * @throws IOException
	 * @throws SAXException
	 */
	public Copybook getLeaderHeader() throws Exception{
		if(leader ==null){
			logger.info("Generating leader metadata for: LDRHDR.txt");
			leader =new Copybook();
			String copybook =manager.getCopyBookMetaData("LDRHDR.txt");
			logger.info("Leader copybook: {}",copybook);
			Document dLeaderCopybook =utility.stringToDom(copybook);
			dLeaderCopybook.getDocumentElement().normalize();
			Element rootNode =dLeaderCopybook.getDocumentElement();
			leader.setName(rootNode.getAttribute("filename"));
			logger.info("Leader metadata creation started");
			leader.setData(getCopybookMetadata(rootNode));
			logger.info("Leader metadata created successfully");
			manager.putMetadata("LDRHDR",utility.prettyPrint(dLeaderCopybook));
		}
		return leader;
	}

	public Copybook getPayload(String filename) throws Exception{
		String copybook =manager.getCopyBookMetaData(filename);
		logger.info("Generating payload metadata for: {}",filename);
		logger.info("Paylod Copybook content: {}",copybook);
		Document dpayloadCopybook =utility.stringToDom(copybook);
		dpayloadCopybook.getDocumentElement().normalize();

		Element rootNode =dpayloadCopybook.getDocumentElement();
		Copybook payloadCB =new Copybook();
		payloadCB.setName(rootNode.getAttribute("filename"));
		payloadCB.setData(getCopybookMetadata(rootNode));
		logger.info("Payload metadata creation started");
		manager.putMetadata(filename.substring(0,filename.indexOf('.')),utility.prettyPrint(dpayloadCopybook));
		logger.info("Payload metadata created successfully");
		return payloadCB;
	}








}
