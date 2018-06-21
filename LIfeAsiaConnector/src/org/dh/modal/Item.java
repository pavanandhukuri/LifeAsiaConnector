package org.dh.modal;

import java.util.List;
import java.util.Map;

public class Item {

	private String name;
	private Map<String, String> attr;
	private List<Item> child;
	private Integer occurs;
	private String type;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getAttr() {
		return attr;
	}
	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}
	public List<Item> getChild() {
		return child;
	}
	public void setChild(List<Item> child) {
		this.child = child;
	}
	public Integer getOccurs() {
		return occurs;
	}
	public void setOccurs(Integer occurs) {
		this.occurs = occurs;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
