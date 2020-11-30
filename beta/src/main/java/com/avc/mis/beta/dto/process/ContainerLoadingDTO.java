/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.LoadedItemDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;
import com.avc.mis.beta.entities.values.ProductionLine;

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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ContainerLoadingDTO extends TransactionProcessDTO<ProcessItemDTO> {
	
	private ShipmentCodeDTO shipmentCode;
	
	private ContainerDetails containerDetails;
	private ShipingDetails shipingDetails;
	
	private List<LoadedItemDTO> loadedItems; 
	
	
	public ContainerLoadingDTO(Integer id, Integer version, Instant createdDate, String userRecording, Integer poCodeId,
			String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName,
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals,
			Integer shipmentCodeId,
			Integer portOfDischargeId, String portOfDischargeCode, String portOfDischargeValue,
			ContainerDetails containerDetails, ShipingDetails shipingDetails) {
		super(id, version, createdDate, userRecording, poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName,
				processName, productionLine, recordedTime, startTime, endTime, 
				duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
		this.shipmentCode = new ShipmentCodeDTO(shipmentCodeId, portOfDischargeId, portOfDischargeCode, portOfDischargeValue);
		this.containerDetails = containerDetails;
		this.shipingDetails = shipingDetails;
	}
	
	
	public ContainerLoadingDTO(@NonNull ContainerLoading loading) {
		super(loading);
		this.shipmentCode = new ShipmentCodeDTO(loading.getShipmentCode());
		this.containerDetails = loading.getContainerDetails();
		this.shipingDetails = loading.getShipingDetails();
		this.loadedItems = Arrays.stream(loading.getLoadedItems())
				.map(i->{return new LoadedItemDTO(i);}).collect(Collectors.toList());
		super.setUsedItemGroups(Arrays.stream(loading.getUsedItemGroups())
				.map(i->{return new UsedItemsGroupDTO((UsedItemsGroup)i);}).collect(Collectors.toList()));
	}
	
	@Override
	public List<UsedItemsGroupDTO> getUsedItemGroups() {
		return super.getUsedItemGroups();
	}

	@Override
	public void setUsedItemGroups(List<UsedItemsGroupDTO> usedItemGroups) {
		super.setUsedItemGroups(usedItemGroups);
	}
	

	@Override
	public String getProcessTypeDescription() {
		return getProcessName().toString();	
	}
	
}
