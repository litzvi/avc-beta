/**
 * 
 */
package com.avc.mis.beta.dto.tableRows;

import com.avc.mis.beta.dto.BaseDTO;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
public class ReferenceWrapper<T extends BaseDTO> {
	
	private Integer reference;	
	private T obj; 

}
