/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.dto.doc.ContainerPoItemRow;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ShippingContainerType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
	
//	private PoCodeBasic poCode;
	private int[] poCodeIds;
	private String[] poCodes;
	private String[] suppliers;
	private OffsetDateTime recordedTime;
	private Duration duration;
	private ProcessStatus status;
	private String[] approvals;
	
	private ShipmentCodeBasic shipmentCode;
	private LocalDate eta;
	private String containerNumber;
	private String sealNumber;
	private String containerSize;
	
	private List<ProductionProcessWithItemAmount> usedItems;
//	private List<ProductionProcessWithItemAmount> loadedItems;

	private List<ContainerPoItemRow> loadedTotals;

	
	public LoadingRow(@NonNull Integer id, 
			String poCodeIds, String poCodes, String suppliers,
			OffsetDateTime recordedTime, Duration duration, ProcessStatus status, String approvals, 			
			Integer shipmentCodeId, String shipmentCodeCode, String portOfDischargeCode, String portOfDischargeValue, 
			LocalDate eta, String containerNumber, String sealNumber, ShippingContainerType containerType) {
		super(id);
		if(poCodeIds != null)
			this.poCodeIds = Stream.of(poCodeIds.split(",")).filter(i -> i != null).mapToInt(j -> Integer.valueOf(j)).distinct().toArray();
		if(poCodes != null)
			this.poCodes = Stream.of(poCodes.split(",")).distinct().toArray(String[]::new);
		if(suppliers != null)
			this.suppliers = Stream.of(suppliers.split(",")).distinct().toArray(String[]::new);
		this.recordedTime = recordedTime;
		this.duration = duration;
		this.status = status;
		if(approvals == null || approvals.startsWith(":")) {
			this.approvals = null;
		}
		else {
			this.approvals = Stream.of(approvals.split(",")).distinct().toArray(String[]::new);
		}
		
		this.shipmentCode = new ShipmentCodeBasic(shipmentCodeId, shipmentCodeCode, portOfDischargeCode, portOfDischargeValue);
		this.eta = eta;
		
		this.containerNumber = containerNumber;
		this.sealNumber = sealNumber;
		this.containerSize = containerType.toString();
		
	}
	
}
