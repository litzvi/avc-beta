/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.ShippingPort;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * DTO for shipping port.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ShippingPortDTO extends ValueDTO {

	private String value;
	private String code;

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
	
	@Override
	public ShippingPort fillEntity(Object entity) {
		ShippingPort portEntity;
		if(entity instanceof ShippingPort) {
			portEntity = (ShippingPort) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ShippingPort class");
		}
		super.fillEntity(portEntity);
		portEntity.setValue(getValue());
		portEntity.setCode(getCode());
		
		return portEntity;
	}
}
