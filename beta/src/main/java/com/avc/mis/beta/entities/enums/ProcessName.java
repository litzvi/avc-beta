/**
 * 
 */
package com.avc.mis.beta.entities.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Zvi
 *
 */
public enum ProcessName {
	CASHEW_ORDER("Cashew Order"),
	GENERAL_ORDER("General Order"),
	CASHEW_RECEIPT("Cashew Receipt"),
	GENERAL_RECEIPT("General Receipt"),
	CASHEW_RECEIPT_QC("Cashew Receipt QC"),
	ROASTED_CASHEW_QC("Roasted Cashew QC"),
	VINA_CONTROL_QC("Vina Control QC"),
	SAMPLE_QC("Sample QC"),
	SUPPLIER_QC("Supplier QC"),
	SAMPLE_RECEIPET("Sample Receipt"), 
	STORAGE_TRANSFER("Storage Transfer"), 
	STORAGE_RELOCATION("Storage Relocation"), 
	CASHEW_CLEANING("Cashew Cleaning"),
	CASHEW_ROASTING("Cashew Roasting"),
	CASHEW_TOFFEE("Cashew Toffee"),
	PACKING("Packing"),
	BAD_QUALITY_PACKING("Bad Quality Packing"),
	CONTAINER_BOOKING("Container Booking"),
	CONTAINER_LOADING("Container Loading"),
	CONTAINER_ARRIVAL("Container Arrival"),
	GENERAL_USE("General Use"),
	PRODUCT_USE("Product Use"),
	PRODUCTION_PLAN("Production Plan");
	
	public final String label;
	 
    private ProcessName(String label) {
        this.label = label;
    }
    
	@JsonValue
    @Override 
    public String toString() { 
        return this.label; 
    }

	
}
