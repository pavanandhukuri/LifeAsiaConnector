package org.dh.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.dh.LAServiceDiscovery;
import org.dh.modal.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LAServiceDiscoveryTest {

	public LAServiceDiscovery discovery=new LAServiceDiscovery(); 
	static Logger logger =LoggerFactory.getLogger(LAServiceDiscovery.class);
	static{
		//PropertyConfigurator.configure("log4j.properties");
		PropertyConfigurator.configure(LAServiceDiscoveryTest.class.getResourceAsStream("log4j.properties"));
	}

	public static void main(String[] args) throws Exception{

		LAServiceDiscoveryTest obj =new LAServiceDiscoveryTest();
		logger.info("Welcome to Life Asia Connector");
		List<Service> opList =new ArrayList<Service>();
		Service op = obj.discovery.createServiceOperation("getAquaDetail", "AUIENQI.txt","AUIENQO.txt");
		opList.add(op);
		/*op = obj.discovery.createServiceOperation("saveLADetail", "TILENQI.TXT","TILENQO.TXT");
		opList.add(op);
		*/
		obj.discovery.createServiceDefination(opList);
		//generateDefinationforReliance();
		//MetaDataManager.getInstance().loadAllCopybookMetada();
		//String mainframeRequest ="55250052CCMSUSRUATQPADEV000JTIL       ENQ       007811EIAN0N 000000781                    210E000000TILENQO   0065100001          0334845600000000082849900+T4604557475ICICI Pru iProtect Smart SP   20180601201806010000000000000000000000000000000000+00000000082849900+00000000082849900INR010000000000000000000000000082849900+INR01    00000000000000000+    IPSS      S00  00000000000000000     0000000000000000000000000000000000+         00000000000000000+                   00000000000000000     0000000000000000000000000000000000+         00000000000000000+                   00000000000000000     0000000000000000000000000000000000+         00000000000000000+                   00000000000000000     0000000000000000000000000000000000+         00000000000000000+                   ";
		//LATransform transform =new LATransform();
		//System.out.println("Output:"+transform.convertMainframeToBusinessObject(mainframeRequest, "getLAData"));
		
	}
	
	public static void generateDefinationforReliance() throws Exception{
		LAServiceDiscoveryTest obj =new LAServiceDiscoveryTest();
		logger.info("Welcome to Life Asia Connector");
		List<Service> opList =new ArrayList<Service>();
		
		Service op = obj.discovery.createServiceOperation("getLAData", "AUIENQI.txt","AUIENQO.txt");
		opList.add(op);
		
		/*op = obj.discovery.createServiceOperation("autoUwEnquiry", "AUIENQI.TXT","AUIENQO.TXT");
		opList.add(op);
		op = obj.discovery.createServiceOperation("contractIssuance", "CTISSI.TXT","CTISSO.TXT");
		opList.add(op);
		op = obj.discovery.createServiceOperation("getRefundData", "NBRENQI.TXT","NBRENQO.TXT");
		opList.add(op);
		
		op = obj.discovery.createServiceOperation("bopCalculation", "PIVENQI.TXT","PIVENQO.TXT");
		opList.add(op);
		op = obj.discovery.createServiceOperation("rejectPaymentApproval", "PMTAPRI.TXT","PMTAPRO.TXT");
		opList.add(op);
		op = obj.discovery.createServiceOperation("rejectPaymentCreation", "PMTCRNI.TXT","PMTCRNO.TXT");
		opList.add(op);
		
		op = obj.discovery.createServiceOperation("notepadUpdate", "POLADDI.TXT","POLADDO.TXT");
		opList.add(op);
		op = obj.discovery.createServiceOperation("rejectPolicy", "PWDPRPI.TXT","PWDPRPO.TXT");
		opList.add(op);
		op = obj.discovery.createServiceOperation("uwApprovalAndReversal", "UWNGUWRI.TXT","UWNGUWRO.TXT");
		opList.add(op);
*/
		
		/*op = obj.discovery.createServiceOperation("saveLADetail", "TILENQI.TXT","TILENQO.TXT");
		opList.add(op);
		*/
		obj.discovery.createServiceDefination(opList);
	}
}