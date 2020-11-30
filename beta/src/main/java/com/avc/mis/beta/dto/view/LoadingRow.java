/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.dto.doc.ContainerPoItemRow;
import com.avc.mis.beta.entities.enums.ShippingContainerType;

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
public class LoadingRow extends BasicDTO {

	private ShipmentCodeBasic shipmentCode;
	
//	private PoCodeBasic poCode;
	private OffsetDateTime recordedTime;
	private Duration duration;
	
	private LocalDate eta;
	private String containerNumber;
	private String sealNumber;
	private String containerSize;
	
	 
	private List<ProductionProcessWithItemAmount> usedItems;
	private List<ProductionProcessWithItemAmount> loadedItems;

	private List<ContainerPoItemRow> loadedTotals;

	
	public LoadingRow(@NonNull Integer id, 
			Integer shipmentCodeId, String portOfDischargeCode, String portOfDischargeValue, 
			OffsetDateTime recordedTime, Duration duration, LocalDate eta,
			String containerNumber, String sealNumber, ShippingContainerType containerType) {
		super(id);
		this.shipmentCode = new ShipmentCodeBasic(shipmentCodeId, portOfDischargeCode, portOfDischargeValue);
		this.recordedTime = recordedTime;
		this.duration = duration;
		
		this.eta = eta;
		
		this.containerNumber = containerNumber;
		this.sealNumber = sealNumber;
		this.containerSize = containerType.toString();
		
	}
	
}
