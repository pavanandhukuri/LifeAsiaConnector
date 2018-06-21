package org.dh.modal;

import java.util.List;

/**
 * This class contain the metadata about the copybook instance. Each copybook will have a name and metadata about the copybook.
 * This metadata will contain the structure of the copybook in a list.
 * @author darkhorse
 *
 */
public class Copybook {

	private String name;
	private List<Item> data;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Item> getData() {
		return data;
	}
	public void setData(List<Item> data) {
		this.data = data;
	}
	
}
