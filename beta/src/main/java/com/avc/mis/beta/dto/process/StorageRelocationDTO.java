/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.ItemCountDTO;
import com.avc.mis.beta.dto.processinfo.StorageMoveDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.processinfo.ItemCount;
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
	
	private Set<StorageMoveDTO> storageMoves; //can use a SortedSet like ContactDetails to maintain order

	public StorageRelocationDTO(Integer id, Integer version, Instant createdDate, String staffRecording,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName, 
			ProcessName processName, ProductionLine productionLine,
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks,
			String approvals, BigDecimal accessWeight) {
		super(id, version, createdDate, staffRecording, poCodeId, contractTypeCode, contractTypeSuffix, supplierId,
				supplierVersion, supplierName, processName, productionLine, recordedTime, startTime, endTime, duration,
				numOfWorkers, processStatus, editStatus, remarks, approvals);
		this.setAccessWeight(accessWeight);
	}



	public StorageRelocationDTO(@NonNull StorageRelocation relocation) {
		super(relocation);
		this.storageMoves = Arrays.stream(relocation.getStorageMoves())
				.map(i->{return new StorageMoveDTO(i);}).collect(Collectors.toSet());
		ItemCount[] itemCounts = relocation.getItemCounts();
		if(itemCounts != null)
			this.setItemCounts(Arrays.stream(itemCounts)
					.map(i->{return new ItemCountDTO(i);}).collect(Collectors.toSet()));
		this.setAccessWeight(relocation.getAccessWeight());
	}


	@Override
	public String getProcessTypeDescription() {
		return "Storage relocation";
	}

}
