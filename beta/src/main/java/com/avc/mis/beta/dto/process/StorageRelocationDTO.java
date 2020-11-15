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

import com.avc.mis.beta.dto.processinfo.ItemCountDTO;
import com.avc.mis.beta.dto.processinfo.StorageMovesGroupDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.processinfo.ItemCount;
import com.avc.mis.beta.entities.processinfo.StorageMovesGroup;
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
public class StorageRelocationDTO extends PoProcessDTO {
	
	private List<StorageMovesGroupDTO> storageMovesGroups;

	public StorageRelocationDTO(Integer id, Integer version, Instant createdDate, String staffRecording,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName, 
			ProcessName processName, ProductionLine productionLine,
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks,
			String approvals) {
		super(id, version, createdDate, staffRecording, poCodeId, contractTypeCode, contractTypeSuffix, supplierId,
				supplierVersion, supplierName, processName, productionLine, recordedTime, startTime, endTime, duration,
				numOfWorkers, processStatus, editStatus, remarks, approvals);
	}



	public StorageRelocationDTO(@NonNull StorageRelocation relocation) {
		super(relocation);
		this.storageMovesGroups = Arrays.stream(relocation.getStorageMovesGroups())
				.map(i->{return new StorageMovesGroupDTO((StorageMovesGroup)i);}).collect(Collectors.toList());
		ItemCount[] itemCounts = relocation.getItemCounts();
		if(itemCounts != null)
			this.setItemCounts(Arrays.stream(itemCounts)
					.map(i->{return new ItemCountDTO(i);}).collect(Collectors.toList()));
	}


	@Override
	public String getProcessTypeDescription() {
		return "Storage relocation";
	}

}
