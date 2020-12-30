/**
 * 
 */
package com.avc.mis.beta.entities.enums;

/**
 * @author zvi
 *
 */
public enum QcCompany {

	AVC_LAB("avc lab"),
	SUPPLIER_SAMPLE("supllier sample"),
	SUPPLIER_CHECK("supllier check"),
	VINA_CONTROL("vina control");
	

	public final String label;
	 
    private QcCompany(String label) {
        this.label = label;
    }
    
    @Override 
    public String toString() { 
        return this.label; 
    }
    
    public static QcCompany valueOfLabel(String label) {
        for (QcCompany e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }

}
