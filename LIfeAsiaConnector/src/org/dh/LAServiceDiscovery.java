package org.dh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.PropertyConfigurator;
import org.db.utils.PropertyUtils;
import org.dh.metadata.BusinessObjectMetadata;
import org.dh.metadata.XSDGeneration;
import org.dh.modal.BusinessObject;
import org.dh.modal.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhdigital.commons.wsdl.api.commons.Operations;
import com.dhdigital.commons.wsdl.config.BOConfiguration;
import com.dhdigital.commons.wsdl.membrane.MembraneAPI;




public class LAServiceDiscovery {
	
	static{
		PropertyConfigurator.configure("log4j.properties");
	}
	
	private Logger logger =LoggerFactory.getLogger(LAServiceDiscovery.class);
	XSDGeneration generate =XSDGeneration.getInstance();
	
	public static Random generator = new Random();
	/**
	 * This method will generate a wsdl file and the necessary xsd schema file need for the service
	 * operation
	 * @param name		: name of the wsdl file
	 * @param namespace	: namespace of the wsdl file
	 * @throws IOException 
	 */
	public void createServiceDefination(List<Service> services) throws IOException{
		/**
		 * load all the bo in filesystem file
		 */
		logger.info("Generating WSDL File");
		List<Operations> operations =new ArrayList<Operations>();
		Operations op =null;
		BOConfiguration config = new BOConfiguration();
		config.setDefaultFolderLocationForWSDLs(PropertyUtils.getValue("com.lifeasia.wsdl.location"));
		config.setTargetNamespace(PropertyUtils.getValue("com.lifeasia.wsdl.namespace"));
		config.setWsdlName(PropertyUtils.getValue("com.lifeasia.wsdl.name"));
		
		for(Service service :services){
			op =config.createNewOperation(service.getName(),service.getRequest().getBoName()+".xsd",service.getResponse().getBoName()+".xsd", true);
			operations.add(op);
		}
		config.setOperations(operations);
		config.setXsds(config.getXsdInformation());
		
		MembraneAPI membObj = new MembraneAPI(MembraneAPI.getMembraneAPIBuilder());
		membObj = membObj.generateWSDL(config);
		logger.info("WSDL Created Successfullly");
		
	//	generator.nextInt(99999999);
		
	}

	public Service createServiceOperation(String serviceName,String laRequestFileName,String laResponseFileName) throws Exception{
		BusinessObjectMetadata boMetadata=new BusinessObjectMetadata();
		Service op =new Service(serviceName);
		
		logger.info("Generate Request Metadata for service:, {} ",serviceName);
		BusinessObject bo =boMetadata.generateMetadata(serviceName+"ReqBO", "REQUEST", laRequestFileName);
		logger.info("Request Schems filename:, {}",generate.getSchemaFile(bo));
		op.setRequest(bo);
		
		logger.info("Generate Response Metadata for service: {}",serviceName);
		bo =boMetadata.generateMetadata(serviceName+"ResBO","RESPONSE", laResponseFileName);
		logger.info("Request Schems filename:, {}",generate.getSchemaFile(bo));
		op.setResponse(bo);
		
		return op;
	}
	
	
	
	
}
