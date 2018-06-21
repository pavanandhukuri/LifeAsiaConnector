package org.dh.modal;

public class BusinessObject {

	private String boName;
	private String boType;
	private Copybook leader;
	private Copybook session;
	private Copybook payload;
	
	public BusinessObject(String boName,String boType) {
		super();
		this.boName = boName;
		this.boType =boType;
	}
	
	public BusinessObject() {
	}
	public String getBoName() {
		return boName;
	}
	public void setBoName(String boName) {
		this.boName = boName;
	}
	public Copybook getLeader() {
		return leader;
	}
	public void setLeader(Copybook leader) {
		this.leader = leader;
	}
	public Copybook getSession() {
		return session;
	}
	public void setSession(Copybook session) {
		this.session = session;
	}
	public Copybook getPayload() {
		return payload;
	}
	public void setPayload(Copybook payload) {
		this.payload = payload;
	}

	public String getBoType() {
		return boType;
	}

	public void setBoType(String boType) {
		this.boType = boType;
	}
	
}
