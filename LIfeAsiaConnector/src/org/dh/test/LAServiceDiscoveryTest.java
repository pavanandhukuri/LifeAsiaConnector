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
		PropertyConfigurator.configure("log4j.properties");
	}

	public static void main(String[] args) throws Exception{

		/*LAServiceDiscoveryTest obj =new LAServiceDiscoveryTest();
		logger.info("Welcome to Life Asia Connector");
		List<Service> opList =new ArrayList<Service>();
		Service op = obj.discovery.createServiceOperation("getLADetail", "TILENQI.TXT","FDVENQO.TXT");
		opList.add(op);
		op = obj.discovery.createServiceOperation("saveLADetail", "TILENQI.TXT","TILENQO.TXT");
		opList.add(op);
		
		obj.discovery.createServiceDefination(opList);*/
		generateDefinationforReliance();
	}
	
	public static void generateDefinationforReliance() throws Exception{
		LAServiceDiscoveryTest obj =new LAServiceDiscoveryTest();
		logger.info("Welcome to Life Asia Connector");
		List<Service> opList =new ArrayList<Service>();
		
		Service op = obj.discovery.createServiceOperation("tsarEnquiry", "AGTCRTI.TXT","AGTCRTO.TXT");
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