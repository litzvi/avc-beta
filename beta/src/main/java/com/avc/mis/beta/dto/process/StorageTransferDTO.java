package com.avc.mis.beta.dto.process;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.ItemCountDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.processinfo.ItemCount;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO(Data Access Object) for sending or displaying StorageTransfer entity data.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class StorageTransferDTO extends TransactionProcessDTO {
	

	
	public StorageTransferDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName,
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, 
			String approvals) {
		super(id, version, createdDate, userRecording, poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName,
				processName, productionLine, recordedTime, startTime, endTime, duration, 
				numOfWorkers, processStatus, editStatus, remarks, approvals);
	}
	
	
	public StorageTransferDTO(@NonNull StorageTransfer transfer) {
		super(transfer);
		ItemCount[] itemCounts = transfer.getItemCounts();
		if(itemCounts != null)
			this.setItemCounts(Arrays.stream(itemCounts)
					.map(i->{return new ItemCountDTO(i);}).collect(Collectors.toList()));

	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Storage transfer";
	}

}
