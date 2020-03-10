/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;

import com.avc.mis.beta.dataobjects.Fax;
import com.avc.mis.beta.dataobjects.Phone;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class FaxDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private String value;
	
	public FaxDTO(@NonNull Fax fax) {
		this.id = fax.getId();
		this.value = fax.getValue();
	}
}
