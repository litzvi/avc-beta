/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ShippingContainerType;
import com.avc.mis.beta.entities.values.ShippingPort;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ContainerArrivalRow extends BasicDTO {

	private LocalDateTime recordedTime;
	private Duration duration;
	private ProcessStatus status;
	private String[] approvals;

	private LocalDate eta;
	private String containerNumber;
	private String sealNumber;
	private String containerSize;
	private ShippingPort portOfDischarge;
	
	private ShipmentCodeBasic shipmentCode;
	
	private DataObjectWithName<Supplier> productCompany; 


	public ContainerArrivalRow(@NonNull Integer id, 
			LocalDateTime recordedTime, Duration duration, ProcessStatus status, String approvals, 			
			LocalDate eta, String containerNumber, String sealNumber, ShippingContainerType containerType,
			Integer productCompanyId, Integer productCompanyVersion, String productCompanyName, 
			Integer shipmentCodeId, String shipmentCodeCode, String portOfDischargeCode, String portOfDischargeValue) {
		super(id);
		this.recordedTime = recordedTime;
		this.duration = duration;
		this.status = status;
		if(approvals == null || approvals.startsWith(":")) {
			this.approvals = null;
		}
		else {
			this.approvals = Stream.of(approvals.split(",")).distinct().toArray(String[]::new);
		}
		
		this.eta = eta;
		
		this.containerNumber = containerNumber;
		this.sealNumber = sealNumber;
		if(containerType != null)
			this.containerSize = containerType.toString();
		
		if(productCompanyId != null)
			this.productCompany = new DataObjectWithName<Supplier>(productCompanyId, productCompanyVersion, productCompanyName);
		
		if(shipmentCodeId != null)
			this.shipmentCode = new ShipmentCodeBasic(shipmentCodeId, shipmentCodeCode, portOfDischargeCode, portOfDischargeValue);

	}

}
