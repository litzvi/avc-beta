/**
 * 
 */
package com.avc.mis.beta.entities.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Zvi
 *
 */
public enum DecisionType {
	NOT_ATTENDED("NOT ATTENDED"),
	EDIT_NOT_ATTENDED("EDIT NOT ATTENDED"),
	APPROVED("APPROVED"),
	DECLINED("DECLINED");
//	SUSPENDED	
	
	private String value;
	
	private DecisionType(String value)
	{
		this.value = value;
	}

	@JsonValue
	@Override
	public String toString()
	{
		return this.value;
	}
}
