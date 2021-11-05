/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.BaseEntityDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.ValueInterface;
import com.avc.mis.beta.entities.codes.ShipmentCode;
import com.avc.mis.beta.entities.values.ShippingPort;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class ShipmentCodeDTO extends BaseEntityDTO implements ValueInterface {
	
	private String code;
	private ShippingPortDTO portOfDischarge;
	
	public ShipmentCodeDTO(@NonNull Integer id, String code, Integer portOfDischargeId, String portOfDischargeValue, String portOfDischargeCode) {
		super(id);
		this.code = code;
		this.portOfDischarge = new ShippingPortDTO(portOfDischargeId, portOfDischargeValue, portOfDischargeCode);
	}
	
	public ShipmentCodeDTO(ShipmentCode shipmentCode) {
		super(shipmentCode.getId());
		this.code = shipmentCode.getCode();
		this.portOfDischarge = shipmentCode.getPortOfDischarge() != null ? new ShippingPortDTO(shipmentCode.getPortOfDischarge()) : null;
	}
	
	/**
	 * @return a string representing full Shipment code. e.g. TAN-51284
	 */
	public String getValue() {
		return String.format("%s-%s", portOfDischarge.getCode(), this.getCode());
	}

	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ShipmentCode.class;
	}
	
	@Override
	public ShipmentCode fillEntity(Object entity) {
		ShipmentCode shipmentCode;
		if(entity instanceof ShipmentCode) {
			shipmentCode = (ShipmentCode) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ShipmentCode class");
		}
		super.fillEntity(shipmentCode);
		shipmentCode.setCode(getCode());
		if(getPortOfDischarge() != null)
			shipmentCode.setPortOfDischarge((ShippingPort) getPortOfDischarge().fillEntity(new ShippingPort()));
		
		
		return shipmentCode;
	}



}
