/**
 * 
 */
package com.avc.mis.beta.entities.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author zvi
 *
 */
public enum ShippingContainerType {

	_40FEET("40'"),
	_20FEET("20'");

	public final String label;
	 
    private ShippingContainerType(String label) {
        this.label = label;
    }
    
	@JsonValue
    @Override 
    public String toString() { 
        return this.label; 
    }
    
    public static ShippingContainerType valueOfLabel(String label) {
        for (ShippingContainerType e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
	
}
