/**
 * 
 */
package com.avc.mis.beta.entities.enums;

/**
 * @author Zvi
 *
 */
public enum OrderStatus {
	OPEN_PENDING,
	OPEN_APPROVED;
	
	public String toString() {
		switch(this) {
		case OPEN_PENDING:
			return "OPEN ORDER - PENDING APPROVAL";
		case OPEN_APPROVED:
			return "OPEN ORDER - APPROVED";
		default:
			return null;
		}
	}
}
