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

import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReceiptDTO extends ProductionProcessDTO {

	private Set<ProcessItemDTO> processItems; //can use a SortedSet like ContactDetails to maintain order
	
	public ReceiptDTO(Integer id, Integer version, Instant createdDate, String userRecording, PoCode poCode,
			ProcessType processType, ProductionLine productionLine, OffsetDateTime recordedTime, Duration duration,
			Integer numOfWorkers, ProcessStatus status, String remarks) {
		super(id, version, createdDate, userRecording, poCode, processType, productionLine, recordedTime, duration,
				numOfWorkers, status, remarks);
	}

	/**
	 * @param process
	 */
	public ReceiptDTO(@NonNull Receipt receipt) {
		super(receipt);
		this.processItems = Arrays.stream(receipt.getProcessItems())
				.map(i->{return new ProcessItemDTO(i);}).collect(Collectors.toSet());

	}
	
	

}
