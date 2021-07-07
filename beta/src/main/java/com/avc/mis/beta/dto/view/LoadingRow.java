/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.dto.exportdoc.ContainerPoItemRow;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ShippingContainerType;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@JsonIgnore private int[] poCodeIds;
	private String[] poCodes;
	@JsonIgnore private String[] suppliers;
	private LocalDateTime recordedTime;
	private Duration duration;
	private ProcessStatus status;
	private String[] approvals;

	private ShipmentCodeBasic shipmentCode;
	private LocalDate eta;
	private String containerNumber;
	private String sealNumber;
	private String containerSize;
	
	private List<ContainerPoItemRow> loadedTotals;

	
	public LoadingRow(@NonNull Integer id, 
			String poCodeIds, String poCodes, String suppliers,
			LocalDateTime recordedTime, Duration duration, ProcessStatus status, String approvals, 			
			Integer shipmentCodeId, String shipmentCodeCode, String portOfDischargeCode, String portOfDischargeValue, 
			LocalDate eta, String containerNumber, String sealNumber, ShippingContainerType containerType) {
		super(id);
		if(poCodeIds != null)
			this.poCodeIds = Stream.of(poCodeIds.split(",")).mapToInt(j -> Integer.valueOf(j)).toArray();
		if(poCodes != null)
			this.poCodes = Stream.of(poCodes.split(",")).toArray(String[]::new);
		if(suppliers != null)
			this.suppliers = Stream.of(suppliers.split(",")).toArray(String[]::new);
		this.recordedTime = recordedTime;
		this.duration = duration;
		this.status = status;
		if(approvals == null || approvals.startsWith(":")) {
			this.approvals = null;
		}
		else {
			this.approvals = Stream.of(approvals.split(",")).toArray(String[]::new);
		}
		
		this.shipmentCode = new ShipmentCodeBasic(shipmentCodeId, shipmentCodeCode, portOfDischargeCode, portOfDischargeValue);
		this.eta = eta;
		
		this.containerNumber = containerNumber;
		this.sealNumber = sealNumber;
		if(containerType != null)
			this.containerSize = containerType.toString();
		
	}
	
	public void setLoadedTotals(List<ContainerPoItemRow> loadedTotals) {
		this.loadedTotals = loadedTotals;
		if(poCodeIds == null && loadedTotals != null) {
			this.poCodes= null;
			loadedTotals.forEach(i -> this.poCodes = ArrayUtils.addAll(this.poCodes, i.getPoCodes()));
		}
	}
	
}
