/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ShippingContainerType;
import com.avc.mis.beta.entities.process.ShipmentBooking;
import com.avc.mis.beta.entities.processinfo.BookedContainer;
import com.avc.mis.beta.entities.processinfo.OrderItem;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.utilities.LocalDateToLong;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class BookedContainerDTO extends ProcessDTO {
	
	String billNumber;
	String vessel;
	String shippingCompany;
	BasicValueEntity<ShippingPort> destinationPort;
	LocalDate etd;
	String containerType;
	
	public BookedContainerDTO(Integer id, Integer version, 
			String billNumber, String vessel, String shippingCompany,
			Integer destinationPortId, String destinationPortValue, 
			LocalDate etd, ShippingContainerType containerType) {
		super(id, version);
		this.billNumber = billNumber;
		this.vessel = vessel;
		this.shippingCompany = shippingCompany;
		this.destinationPort = new BasicValueEntity<ShippingPort>(destinationPortId, destinationPortValue);
		this.etd = etd;
		this.containerType = containerType.toString();
	}
	
	public BookedContainerDTO(@NonNull BookedContainer container) {
		super(container.getId(), container.getVersion());
		this.billNumber = container.getBillNumber();
		this.vessel = container.getVessel();
		this.shippingCompany = container.getShippingCompany();
		this.destinationPort = new BasicValueEntity<ShippingPort>(container.getDestinationPort());
		this.etd = container.getEtd();
		this.containerType = container.getContainerType();
	}
}
