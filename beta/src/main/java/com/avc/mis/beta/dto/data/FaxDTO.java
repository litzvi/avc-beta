/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.io.Serializable;

import com.avc.mis.beta.entities.data.Fax;

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
