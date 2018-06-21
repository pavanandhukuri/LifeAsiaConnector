package org.dh;

import org.db.utils.XMLUtils;
import org.dh.metadata.MetaDataManager;
import org.dh.p400.translator.DataTranslator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




public class LATransform {

	private XMLUtils utility =XMLUtils.getInstance();
	private MetaDataManager manager =MetaDataManager.getInstance();
	private DataTranslator translator =new DataTranslator();
	//Logger logger =LoggerFactory.getLogger(LATransform.class);

	/**
	 * This method will take the business object as input and will return the request in lifeasia format based on copybooks metadata.
	 * Each business object has copybook elements which contain the name of the copybooks use to generate the business object, 
	 * we will read these elements to get the name
	 * s of the copybook used and from the copybooks we will get the metadata which will help 
	 * in converting the business object into lifeasia request format.
	 *  
	 * @param strRequestXML : Pass string representation of Business Object 
	 * @param serviceName	: Name of the service
	 * @return 				: lifeasia request object
	 */
	public String convertBusinessObjectToMainframe(String strRequestXML,String serviceName){
		String mainframeRequest="",mainframeSessionRequest="",mainframeLeaderRequest="";
		/**
		 * convert the strRequestXML to Document Object
		 * 	get all copybook elements
		 * 
		 * 	for session copybook	
		 * 	sessionXML =Get the Session Element from the Document Object
		 * 	sessionCopybook =get session copybook String
		 * 	get sessionRequestStr =from convertXMLtoMainframe(sessionXML,sessionCopybook);
		 * 
		 * 	for leader copybook
		 * 	leaderXML =get the leader Element from the Document Object
		 * 	leaderCopybook =get leader copybook String
		 * 	get leaderRequestStr =from convertXMLtoMainframe(leaderXML,leaderCopybook)
		 * 
		 * 
		 * 	for payload copybook
		 * 	payloadXML =get the payload element from the Document Object
		 * 	payloadCopybook -get payload copybook String
		 * get payloadRequestString =from convertXMltoMainframe(payloadXML,payloadCopybook);
		 * 	
		 * return leader +session +payload
		 */
		Document document =utility.stringToDom(strRequestXML);
		NodeList copybookList =document.getDocumentElement().getElementsByTagName("copybook");
		Node copybookNode;
		try{

			for(int i=0;i<copybookList.getLength();i++){
				copybookNode =copybookList.item(i);

				String copybookRootNode =copybookNode.getFirstChild().getNodeName();
				String copybookName =copybookNode.getAttributes().getNamedItem("filename").getNodeValue();

				//Get copybook xml metadata for the bo filename
				String cbMetadata =manager.getMetadata(copybookName.substring(0, copybookName.indexOf('.')));
				System.out.println("Metadata:" +cbMetadata);
				//Generate the mainframe string
				if("SESSIONI-REC".equalsIgnoreCase(copybookRootNode)){
					//session header
					mainframeSessionRequest +=
							translator.convertXmltoMainFrame(utility.createDocumentFromElement(copybookNode),
									utility.stringToDom(cbMetadata));

				}else if("LEADER-HEADER".equalsIgnoreCase(copybookRootNode)){
					//leader header
					mainframeLeaderRequest +=
							translator.convertXmltoMainFrame(utility.createDocumentFromElement(copybookNode),
									utility.stringToDom(cbMetadata));
				}else{
					mainframeRequest +=
							translator.convertXmltoMainFrame(utility.createDocumentFromElement(copybookNode),
									utility.stringToDom(cbMetadata));
				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("leader Length:"+mainframeLeaderRequest.length());
		System.out.println("session Length:"+mainframeSessionRequest.length());
		System.out.println("payload Length:"+mainframeRequest.length());
		mainframeRequest =mainframeLeaderRequest +mainframeSessionRequest +mainframeRequest;

		return mainframeRequest;
	}



//	protected String fillSessionI(Document sessionRequest,
//			Document sessionMetadat,
//			int sessionID) {
//		//logger.debug("fillSession - start");
//		//logger.debug("In fillSessionI "+ utility.prettyPrint(sessionRequest) + "\n"+ utility.prettyPrint(sessionMetadat));
//		
//		String l_strSessionI = "";
//		int l_iTempNode = 0;
//
//		l_iTempNode = Find.firstMatch(p_iRequestCopyBook, "?<MSGID>");
//		if (Node.getDataWithDefault(l_iTempNode, "").equals(""))
//		setNodeValue(l_iTempNode,"SESSIONI");
//		l_iTempNode = Find
//				.firstMatch(p_iRequestCopyBook, "?<SESSIONI-COMPANY>");
//		if (Node.getDataWithDefault(l_iTempNode, "").equals(""))
//		setNodeValue(l_iTempNode, m_mhiMessageHeaderInfo.getCompany());
//		l_iTempNode = Find.firstMatch(p_iRequestCopyBook,
//				"?<SESSIONI-LANGUAGE>");
//		if (Node.getDataWithDefault(l_iTempNode, "").equals(""))
//		setNodeValue(l_iTempNode, m_mhiMessageHeaderInfo.getLanguage());
//		l_iTempNode = Find.firstMatch(p_iRequestCopyBook, "?<SESSIONI-ACCTYR>");
//		if (Node.getDataWithDefault(l_iTempNode, "").equals(""))
//		setNodeValue(l_iTempNode, Integer.toString(m_mhiMessageHeaderInfo
//				.getAccountingYear()));
//		l_iTempNode = Find.firstMatch(p_iRequestCopyBook, "?<SESSIONI-ACCTMN>");
//		if (Node.getDataWithDefault(l_iTempNode, "").equals(""))
//		setNodeValue(l_iTempNode, Integer.toString(m_mhiMessageHeaderInfo
//				.getAccountingMonth()));
//		l_iTempNode = Find.firstMatch(p_iRequestCopyBook, "?<MSGLNG>");
//		if (Node.getDataWithDefault(l_iTempNode, "").equals(""))
//		setNodeValue(l_iTempNode, Integer.toString(10));
//		int l_iMsgCntNode = Find.firstMatch(p_iRequestCopyBook, "?<MSGCNT>");
//		if (Node.getDataWithDefault(l_iTempNode, "").equals(""))
//		setNodeValue(l_iMsgCntNode, Integer.toString(1));
//
//		l_strSessionI = p_dtDataTranslator.convertXmltoMainFrame(
//				p_iRequestCopyBook, p_iCopyBookImplNode);
//		if (P400ApplicationConnector.itgiLogger.isDebugEnabled()) {
//			P400ApplicationConnector.itgiLogger.debug("SESSION-I:"
//					+ l_strSessionI.length() + ":" + l_strSessionI);
//		}
//		if (P400ApplicationConnector.itgiLogger.isDebugEnabled()) {
//			P400ApplicationConnector.itgiLogger
//					.debug("fillSessionI(int, int, DataTranslator, int) - end");
//		}
//		return l_strSessionI;
//	}
//
//	 	

	/**
	 * This method will take the lifeasia response string as input and will return the business Object response.
	 * From the response we can get the name of the copybook used to prepare the response by lifeasia and from that copybook we will get the metadata
	 * which will help in converting the lifeasia response into business object response.
	 * 
	 * @param mainframeResponse : Lifeasia response
	 * @param serviceName		: Name of the service
	 * @return					: Business Object response.
	 */
	public String convertMainframeToBusinessObject(String mainframeResponse,String serviceName){
		/**
		 * 
		 * responseXML
		 * Extract the first 100 character for leader part
		 * leaderXML =convertmainframetoXML for leader
		 * responseXML =append(leaderXML)
		 * Read the next 10 character to check if any error is there
		 * if(yes), then 
		 * 	prepare error response
		 * else
		 * 	get response metadata
		 * 	extract response string from mainframe string
		 * payloadXML =convertmainframetoXML for payload
		 * responseXML =append(responseXML)
		 * 
		 * return responseXML
		 */
		int _LeaderHeaderLength =100;
		int responseCount =1;
		String additionalResponseNode ="RESMSG";

		Document document =utility.createNewDocument();		
		//Create Root Element
		Element root =document.createElement(serviceName+"Response");
		document.appendChild(root);

		String leaderContent =mainframeResponse.substring(0,_LeaderHeaderLength);
		//Get the Metadata for the leader
		String metadata =manager.getMetadata("LDRHDR");
		Element headerNode =document.createElement(additionalResponseNode+responseCount);
		responseCount++;

		//Get the XML Document for the Header from the copybook
		Node headercontent =translator.convertMainFrameToXml(leaderContent,metadata).getDocumentElement();
		headercontent =document.importNode(headercontent, true);
		headerNode.appendChild(headercontent);
		root.appendChild(headerNode);

		//Extract Payload data from the response
		mainframeResponse =mainframeResponse.substring(_LeaderHeaderLength);
		//Get the copybook name from the response
		String copyBookName = mainframeResponse.substring(0, 10).trim();

		if (copyBookName.equalsIgnoreCase("SFERRREC")
				|| copyBookName.equalsIgnoreCase("BOVERR")){
			throw new RuntimeException("LifeAsia Error: Copybook: "+copyBookName +" Message: "+mainframeResponse);
		}else{
			headerNode =document.createElement(additionalResponseNode+responseCount);

			metadata =manager.getMetadata(copyBookName);
			headercontent =translator.convertMainFrameToXml(mainframeResponse,metadata).getDocumentElement();
			headercontent =document.importNode(headercontent, true);
			headerNode.appendChild(headercontent);
			root.appendChild(headerNode);



		}


		return utility.prettyPrint(document);
	}


}
