/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.LoadedItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
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
public class ContainerLoadingDTO extends PoProcessDTO {

	private Set<LoadedItemDTO> loadedItems; //can use a SortedSet like ContactDetails to maintain order
	private Set<UsedItemsGroupDTO> usedItemGroups; //can use a SortedSet like ContactDetails to maintain order
	
	
	public ContainerLoadingDTO(Integer id, Integer version, Instant createdDate, String userRecording, Integer poCodeId,
			String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName,
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
		super(id, version, createdDate, userRecording, poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName,
				processName, productionLine, recordedTime, startTime, endTime, 
				duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
	}
	
	
	public ContainerLoadingDTO(@NonNull ContainerLoading loading) {
		super(loading);
		this.loadedItems = Arrays.stream(loading.getLoadedItems())
				.map(i->{return new LoadedItemDTO(i);}).collect(Collectors.toSet());
		this.usedItemGroups = Arrays.stream(loading.getUsedItemGroups())
				.map(i->{return new UsedItemsGroupDTO((UsedItemsGroup)i);}).collect(Collectors.toSet());
	}
	

	@Override
	public String getProcessTypeDescription() {
		return getProcessName().toString();	
	}
	
}
