package org.dh.test;

import javax.xml.parsers.ParserConfigurationException;

import org.db.utils.XMLUtils;
import org.w3c.dom.Document;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Schema;
import com.predic8.schema.Sequence;


public class XMLUtilsTest {

	
	
	public static void main(String[] args) throws ParserConfigurationException{
		
		/*XMLUtils utils =XMLUtils.getInstance();
		
		
		String xml ="<copybook><RET-INPUT><INL-COMMUNICATION-DATA><INL-CCT-TTL-NAM>DDDD</INL-CCT-TTL-NAM><INL-CCT-FST-NAM>WWWW</INL-CCT-FST-NAM><INL-CCT-SUR>GGGG</INL-CCT-SUR><INL-NAM-CPY>JJJJ</INL-NAM-CPY><INL-CCS-CCT-TEL>YYYY</INL-CCS-CCT-TEL><INL-COMMUNICATION-KEY><INL-NUM-CMU>3333</INL-NUM-CMU><INL-DAT-CMU>CCCC</INL-DAT-CMU></INL-COMMUNICATION-KEY><INL-CCS-REF-NUM></INL-CCS-REF-NUM><INL-DATE-FROM>VVV</INL-DATE-FROM><INL-DATE-TO>BBB</INL-DATE-TO></INL-COMMUNICATION-DATA><INL-PAF-NEXT-REQUEST>RRR</INL-PAF-NEXT-REQUEST><INL-CS-SN-SOUNDEX-INDIC>A</INL-CS-SN-SOUNDEX-INDIC></RET-INPUT></copybook>";
		Document doc =utils.stringToDom(xml);
		System.out.println("Document Object:"+doc.getDocumentElement().getTagName());
		System.out.println("Document XML:"+utils.prettyPrint(doc));
		*/
		main1();
	}
	
	 public static void main1() {
		 
		    Schema schema = new Schema("http://predic8.com/schema/person/");
		     
		    schema.newElement("person", "personType");
		    
		    ComplexType personType = schema.newComplexType("personType");
		    personType.newAttribute("id", Schema.INT);
		    Sequence seq = personType.newSequence();
		    seq.setMaxOccurs("unbounded");
		    seq.newElement("name", Schema.STRING);
		    seq.newElement("lastname", Schema.STRING);
		    seq.newElement("date-of-birth", Schema.DATE);
		    seq.newElement("address").newComplexType().newSequence().
		    newElement("country", Schema.STRING);
		     
		    System.out.println(schema.getAsString());

}

}
