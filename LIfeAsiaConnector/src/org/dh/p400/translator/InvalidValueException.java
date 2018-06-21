package org.dh.p400.translator;

public class InvalidValueException
		extends Exception {
	
	String fieldName = "";
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8031059594714912058L;
	
	InvalidValueException(String msg) {
        super(msg);
    }
	
	InvalidValueException(String msg, String fieldName) {
        super(msg);
        this.fieldName = fieldName;
    }

	public String getFieldName() {
		return this.fieldName;
	}
}
