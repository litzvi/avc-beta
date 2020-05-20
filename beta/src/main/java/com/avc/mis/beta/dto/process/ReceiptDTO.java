/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.ReceiptItem;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ReceiptDTO extends ProductionProcessDTO {

	private Set<ReceiptItemDTO> receiptItems; //can use a SortedSet like ContactDetails to maintain order
	
	public ReceiptDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, ContractType contractType, Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessType processType, ProductionLine productionLine, OffsetDateTime recordedTime, Duration duration,
			Integer numOfWorkers, ProcessStatus status, String remarks) {
		super(id, version, createdDate, userRecording, 
				poCodeId, contractType, supplierId, supplierVersion, supplierName, 
				processType, productionLine, recordedTime, duration,
				numOfWorkers, status, remarks);
	}

	/**
	 * @param process
	 */
	public ReceiptDTO(@NonNull Receipt receipt) {
		super(receipt);
		this.receiptItems = Arrays.stream(receipt.getProcessItems())
				.map(i->{return new ReceiptItemDTO((ReceiptItem) i);}).collect(Collectors.toSet());

	}
	
	

}
