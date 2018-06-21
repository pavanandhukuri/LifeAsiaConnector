package org.dh.test;

import java.util.ArrayList;
import java.util.List;

import org.dh.LAServiceDiscovery;
import org.dh.LATransform;
import org.dh.metadata.XSDGeneration;
import org.dh.modal.Service;

public class XSDSchemaTest {
	
	public static void main(String[] args){
		
		
		String wsdlName ="laInterface";
		String wsdlNamespace ="http://lifeasia/wsdl";

		LAServiceDiscovery discovery =new LAServiceDiscovery();
		Service op;
		try {
			System.out.println("Create Service Operation");
			op = discovery.createServiceOperation("getLAData", "TILENQI.TXT","TILENQO.TXT");
		
		List<Service> opList =new ArrayList<Service>();
		opList.add(op);
		
		
		XSDGeneration generate =XSDGeneration.getInstance();
		System.out.println("FileName:"+generate.getSchemaFile(op.getRequest()));
		//test the service request creation
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
