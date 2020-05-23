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

import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.entities.process.RawItemQuality;
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
public class QualityCheckDTO extends ProductionProcessDTO {

	private Set<RawItemQualityDTO> checkItems; //can use a SortedSet like ContactDetails to maintain order
	
	public QualityCheckDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, ContractTypeCode contractTypeCode, Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessName processName, ProductionLine productionLine, OffsetDateTime recordedTime, Duration duration,
			Integer numOfWorkers, ProcessStatus status, String remarks) {
		super(id, version, createdDate, userRecording, 
				poCodeId, contractTypeCode, supplierId, supplierVersion, supplierName, 
				processName, productionLine, recordedTime, duration,
				numOfWorkers, status, remarks);
	}
	
	public QualityCheckDTO(@NonNull QualityCheck check) {
		super(check);
		this.checkItems = Arrays.stream(check.getProcessItems())
				.map(i->{return new RawItemQualityDTO((RawItemQuality)i);}).collect(Collectors.toSet());
	}

	
}
