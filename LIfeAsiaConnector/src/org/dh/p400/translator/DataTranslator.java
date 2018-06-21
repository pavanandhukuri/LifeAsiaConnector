
package org.dh.p400.translator;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.db.utils.XMLUtils;
import org.dh.metadata.MetaDataManager;
import org.w3c.dom.Document;

import net.sf.cb2xml.convert.MainframeToXml;

public class DataTranslator {

	private String mainframeBuffer = null;
	private Document resultDocument = null;
	private XMLUtils utility=XMLUtils.getInstance();
	
	public DataTranslator() {
		super();
	}

	public String convertXmltoMainFrame(String l_strRequestXml, String l_strMetadataXml) throws InvalidValueException {
		String l_strMainframeData = "";

		// Create Document using incoming parameters

		org.w3c.dom.Document l_docRequestXml = utility.stringToDom(l_strRequestXml);

		org.w3c.dom.Document l_docMetadataXml = utility.stringToDom(l_strMetadataXml);

		// Params 1) XML source document 2) Copybook as XML
		l_strMainframeData = new NewXmlToMainframe().convert(l_docRequestXml, l_docMetadataXml);

		return l_strMainframeData;

	}
	public String convertXmltoMainFrame(Document l_strRequestXml, Document l_strMetadataXml) throws InvalidValueException {
		String l_strMainframeData = "";

		

		// Params 1) XML source document 2) Copybook as XML
		l_strMainframeData = new NewXmlToMainframe().convert(l_strRequestXml, l_strMetadataXml);

		return l_strMainframeData;

	}


	public String convertMainFrameToXml(String p_strMainFrameResponse,int i, String l_strMetadataXml) throws ParserConfigurationException {
		org.w3c.dom.Document l_docCopyBookXml = utility.stringToDom(l_strMetadataXml);

		// Params 1) XML source document 2) Copybook as XML
		org.w3c.dom.Document l_docXmlDocument = new MainframeToXml().convert(p_strMainFrameResponse, l_docCopyBookXml);
		//org.w3c.dom.Document l_docXmlDocument = convert(p_strMainFrameResponse, l_docCopyBookXml);
		String l_strResponseXml = net.sf.cb2xml.util.XmlUtils.domToString(l_docXmlDocument).toString();

		return l_strResponseXml;
	}

	public Document convertMainFrameToXml(String p_strMainFrameResponse, String l_strMetadataXml) {
		org.w3c.dom.Document l_docCopyBookXml = utility.stringToDom(l_strMetadataXml);

		// Params 1) XML source document 2) Copybook as XML
		return  new MainframeToXml().convert(p_strMainFrameResponse, l_docCopyBookXml);


	}


	public static void main(String[] args) throws InvalidValueException, IOException, ParserConfigurationException {
		/*String implString = "<copybook filename='Input.txt'><item display-length='290' level='05' name='RET-INPUT' position='1'><item display-length='161' level='10' name='INL-COMMUNICATION-DATA' position='1'><item display-length='4' level='15' name='INL-CCT-TTL-NAM' picture='X(4)' position='1' spaces='true'/><item display-length='12' level='15' name='INL-CCT-FST-NAM' picture='X(12)' position='5' spaces='true'/><item display-length='25' level='15' name='INL-CCT-SUR' picture='X(25)' position='17' spaces='true'/><item display-length='60' level='15' name='INL-NAM-CPY' picture='X(60)' position='42' spaces='true'/><item display-length='12' level='15' name='INL-CCS-CCT-TEL' picture='X(12)' position='102' spaces='true'/><item display-length='16' level='15' name='INL-COMMUNICATION-KEY' position='114'><item display-length='6' level='20' name='INL-NUM-CMU' numeric='true' picture='S9(5)' position='114' zeros='false'/><item display-length='10' level='20' name='INL-DAT-CMU' picture='X(10)' position='120' spaces='true'/></item><item display-length='12' level='15' name='INL-CCS-REF-NUM' numeric='true' picture='S9(11)' position='130' zeros='false'/><item display-length='10' level='15' name='INL-DATE-FROM' picture='X(10)' position='142' spaces='true'/><item display-length='10' level='15' name='INL-DATE-TO' picture='X(10)' position='152' spaces='true'/></item><item display-length='128' level='10' name='INL-PAF-NEXT-REQUEST' picture='X(128)' position='162' spaces='true'/><item display-length='1' level='10' name='INL-CS-SN-SOUNDEX-INDIC' picture='X(1)' position='290' spaces='true'/></item></copybook>";
		String reqString = "<copybook><RET-INPUT><INL-COMMUNICATION-DATA><INL-CCT-TTL-NAM>DDDD</INL-CCT-TTL-NAM><INL-CCT-FST-NAM>WWWW</INL-CCT-FST-NAM><INL-CCT-SUR>GGGG</INL-CCT-SUR><INL-NAM-CPY>JJJJ</INL-NAM-CPY><INL-CCS-CCT-TEL>YYYY</INL-CCS-CCT-TEL><INL-COMMUNICATION-KEY><INL-NUM-CMU>3333</INL-NUM-CMU><INL-DAT-CMU>CCCC</INL-DAT-CMU></INL-COMMUNICATION-KEY><INL-CCS-REF-NUM></INL-CCS-REF-NUM><INL-DATE-FROM>VVV</INL-DATE-FROM><INL-DATE-TO>BBB</INL-DATE-TO></INL-COMMUNICATION-DATA><INL-PAF-NEXT-REQUEST>RRR</INL-PAF-NEXT-REQUEST><INL-CS-SN-SOUNDEX-INDIC>A</INL-CS-SN-SOUNDEX-INDIC></RET-INPUT></copybook>";

		DataTranslator translator = new DataTranslator();

		String main = translator.convertXmltoMainFrame(reqString, implString);

		System.out.println(main);*/

		DataTranslator translator = new DataTranslator();
		//translator.common =new Common();
		MetaDataManager metadata =MetaDataManager.getInstance();
		String leaderCopybook ="<copybook filename='LDRHDR.txt' dialect='Mainframe'><item level='01' name='LEADER-HEADER' position='1' storage-length='100' display-length='100'><item level='03' name='MSGREFNO' picture='9(08)' position='1' storage-length='8' display-length='8' numeric='true' /><item level='03' name='USRPRF' picture='X(10)' position='9' storage-length='10' display-length='10' /><item level='03' name='WKSID' picture='X(10)' position='19' storage-length='10' display-length='10' /><item level='03' name='OBJID' picture='X(10)' position='29' storage-length='10' display-length='10' /><item level='03' name='VRBID' picture='X(10)' position='39' storage-length='10' display-length='10' /><item level='03' name='TOTMSGLNG' picture='9(05)' position='49' storage-length='5' display-length='5' numeric='true' /><item level='03' name='OPMODE' picture='X(01)' position='54' storage-length='1' display-length='1'></item><item level='03' name='CMTCONTROL' picture='X(01)' position='55' storage-length='1' display-length='1'></item><item level='03' name='RSPMODE' picture='X(01)' position='56' storage-length='1' display-length='1' /><item level='03' name='MSGINTENT' picture='X(01)' position='57' storage-length='1' display-length='1' /><item level='03' name='MORE-IND' picture='X(01)' position='58' storage-length='1' display-length='1'></item><item level='03' name='ERRLVL' picture='X(01)' position='59' storage-length='1' display-length='1'></item><item level='03' name='IGNORE-DRIVER-HELD' picture='X(01)' position='60' storage-length='1' display-length='1' /><item level='03' name='SUPPRESS-RCLRSC' picture='X(01)' position='61' storage-length='1' display-length='1' /><item level='03' name='FILLER' picture='X(39)' position='62' storage-length='39' display-length='39' /></item></copybook>";
		System.out.println("Copybook:"+leaderCopybook);
		String mainframeResponse ="84811006CCMSUSRUATQPADEV000JTIL       ENQ       025001Y1RN0                                         SESSIONI  0001000001          210E000000TILENQI   0001100001          C03184907 ";
		System.out.println("Response:"+translator.convertMainFrameToXml(mainframeResponse.substring(0,100), 0, leaderCopybook));
		//84811006CCMSUSRUATQPADEV000JTIL       ENQ       005501EIAN2N 000000550                    210E00000		
	}
}
