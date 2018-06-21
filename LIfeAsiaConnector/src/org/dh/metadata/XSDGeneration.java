package org.dh.metadata;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.db.utils.PropertyUtils;
import org.dh.modal.BusinessObject;
import org.dh.modal.Copybook;
import org.dh.modal.Item;

import com.predic8.schema.Attribute;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.ModelGroup;
import com.predic8.schema.Schema;
import com.predic8.schema.Sequence;
import com.predic8.schema.creator.SchemaCreator;
import com.predic8.schema.creator.SchemaCreatorContext;

import groovy.xml.MarkupBuilder;
import groovy.xml.QName;


public class XSDGeneration {


	private String schemaNamespace;
	private String schemaLocation;
	private static XSDGeneration instance;

	private XSDGeneration(){
		schemaNamespace =PropertyUtils.getValue("com.lifeasia.schema.namespace");
		schemaLocation =PropertyUtils.getValue("com.lifeasia.schema.location");
	} 

	public static XSDGeneration getInstance(){
		if(instance == null)
			instance =new XSDGeneration();
		return instance;
	}


	public String getSchemaFile(BusinessObject bo) throws Exception{
		System.out.println("Create New Schema for Business Object:"+bo.getBoName());
		String filename =bo.getBoName() +".xsd";
		File file =new File(schemaLocation);
		if(!file.exists()){
			file.mkdirs();
		}
		file =new File(schemaLocation+File.separator +filename);
		if(!file.exists())
			file.createNewFile();

		FileWriter writer =new FileWriter(file);
		Schema schema =getSchemaObject(bo);
		SchemaCreator creator =new SchemaCreator();
		creator.setBuilder(new MarkupBuilder(writer));
		schema.create(creator,new SchemaCreatorContext());
		System.out.println("Schema file created:"+file.toString());
		return filename;
	}

	/**
	 * This method is used to generate the Schema of the request/response for the lifeasia BO 
	 * based on the BusinessObject definition passed.
	 * 
	 * @param bo : BusinessObject for which the xsd schema has to be generated.
	 * @return
	 */
	public Schema getSchemaObject(BusinessObject bo){
		System.out.println("Get schema file content for bo: "+bo.getBoName());
		Schema schema =new Schema(schemaNamespace);


		Element rootElement =schema.newElement(bo.getBoName());	
		Sequence cbElement =rootElement.newComplexType().newSequence();
		
		int counter=1;
		String element=null;
		if("REQUEST".equalsIgnoreCase(bo.getBoType())){
			element ="REQ";
		}else{
			element ="RES";
		}
		
		createParentSchema(bo.getLeader(),cbElement.newElement(element+"MSG"+counter));
		counter++;
		if("REQUEST".equalsIgnoreCase(bo.getBoType())){
			createParentSchema(bo.getSession(), cbElement.newElement(element+"MSG"+counter));
			counter++;
		}
		createParentSchema(bo.getPayload(), cbElement.newElement(element+"MSG"+counter));

		return schema;	

	}


	public String getSchemaAsString(BusinessObject bo){
		return getSchemaObject(bo).getAsString();
	}

	/**
	 * This will create a complex type xsd element tree for the copybook.
	 * We will fetch the metadata of the copybook and create a complex xsd element by read the metadata.
	 *
	 * @param copybook : Copybook for which the xsd element tree has to be create
	 * @param element  : Parent xsd element inside which we will store the xsd schema of the copybook.
	 * @return
	 */
	public Element createParentSchema(Copybook copybook,Element element){
		Element parentNode = element.newComplexType().newSequence().newElement("copybook");

		//parentNode.setProperty("filename",copybook.getName());

		createChildSchema(copybook.getData(), parentNode,copybook.getName(),true);

		return parentNode;

	}

	/**
	 * 
	 * @param metadata
	 * @param parentElement
	 * @param cbName
	 * @param rootNode
	 */
	public void createChildSchema(List<Item> metadata,Element parentElement,String cbName,boolean rootNode){
		ComplexType cmpxElement =parentElement.newComplexType();
		if(rootNode){
			Attribute attr =new Attribute("copybook",Schema.STRING);
			attr.setUse("required");
			attr.setFixedValue(cbName);
			
			cmpxElement.add(attr);
		}
		Sequence seq =cmpxElement.newSequence();
		
		for(Item item:metadata){
			if(item.getChild() !=null){
				Element childElement=seq.newElement(item.getName());
				if(item.getOccurs()>1){
					childElement.setMaxOccurs("unbounded");
				}
				if(item.getOccurs()==0)
				childElement.setMinOccurs("0");
				
				//All add the occurance as attr here
				createChildSchema(item.getChild(),childElement,null,false);
			}else{
				
				if("double".equalsIgnoreCase(item.getType()) || "int".equalsIgnoreCase(item.getType())){
					seq.newElement(item.getName()).
					setType(Schema.INT);
				}else{
					seq.newElement(item.getName()).
					setType(Schema.STRING);
				}				
			}
		}

	}



}

