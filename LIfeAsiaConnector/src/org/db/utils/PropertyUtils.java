package org.db.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {

	private static Properties prop;
	
	static{
		
		FileReader reader;
		try {
			reader = new FileReader("application.properties");
			prop =new Properties();
			prop.load(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("LAConnector Error: application property file is missing",e);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("LAConnector Error: Eror while loading the application property file",e);
		}
	}
	
	public static String getValue(String propertyName){
		String value;
		if(propertyName ==null || "".equalsIgnoreCase(propertyName) || propertyName.length()<=0){
			throw new RuntimeException("Property name can't be empty: "+propertyName);
		}
		value =prop.getProperty(propertyName);
		if(value ==null || "".equalsIgnoreCase(value) || value.length()<=0){
			System.out.println(propertyName +" property value is emplty");
		}
		return value;
	}
}
