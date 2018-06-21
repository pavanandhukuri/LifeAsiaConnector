package org.dh.modal;

public class Service {

	/**
	 * Operation Name
	 */
	private String name;
	private BusinessObject request;
	private BusinessObject response;
	private BusinessObject fault;
	
	
	public Service(String name){
		this.name =name;
	}
	
	public Service(){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BusinessObject getRequest() {
		return request;
	}
	public void setRequest(BusinessObject request) {
		this.request = request;
	}
	public BusinessObject getResponse() {
		return response;
	}
	public void setResponse(BusinessObject response) {
		this.response = response;
	}
	public BusinessObject getFault() {
		return fault;
	}
	public void setFault(BusinessObject fault) {
		this.fault = fault;
	}
	
	
	

}
