/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.ShippingPort;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ShippingPortDTO extends ValueDTO {

	String value;
	String code;

	public ShippingPortDTO(ShippingPort portOfDischarge) {
		super(portOfDischarge.getId());
		this.value = portOfDischarge.getValue();
		this.code = portOfDischarge.getCode();
	}
	
	public ShippingPortDTO(@NonNull Integer id, String value, String code) {
		super(id);
		this.value = value;
		this.code = code;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ShippingPort.class;
	}
}
