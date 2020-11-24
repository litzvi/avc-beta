/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.time.LocalDate;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.enums.ShippingContainerType;
import com.avc.mis.beta.entities.processinfo.BookedContainer;
import com.avc.mis.beta.entities.values.ShippingPort;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@Deprecated
public class BookedContainerDTO extends SubjectDataDTO {
	
	String billNumber;
	String vessel;
	String shippingCompany;
	BasicValueEntity<ShippingPort> destinationPort;
	LocalDate etd;
	String containerType;
	
	public BookedContainerDTO(Integer id, Integer version, Integer ordinal,
			String billNumber, String vessel, String shippingCompany,
			Integer destinationPortId, String destinationPortValue, 
			LocalDate etd, ShippingContainerType containerType) {
		super(id, version, ordinal);
		this.billNumber = billNumber;
		this.vessel = vessel;
		this.shippingCompany = shippingCompany;
		this.destinationPort = new BasicValueEntity<ShippingPort>(destinationPortId, destinationPortValue);
		this.etd = etd;
		this.containerType = containerType.toString();
	}
	
	public BookedContainerDTO(@NonNull BookedContainer container) {
		super(container.getId(), container.getVersion(), container.getOrdinal());
		this.billNumber = container.getBillNumber();
		this.vessel = container.getVessel();
		this.shippingCompany = container.getShippingCompany();
		this.destinationPort = new BasicValueEntity<ShippingPort>(container.getDestinationPort());
		this.etd = container.getEtd();
		this.containerType = container.getContainerType();
	}
}
