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

import com.avc.mis.beta.dto.processinfo.OrderItemDTO;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.RecordStatus;
import com.avc.mis.beta.entities.process.PO;
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
public class PoDTO extends ProductionProcessDTO {
	
//	private OrderStatus orderStatus;
	private Set<OrderItemDTO> orderItems; //can use a SortedSet like ContactDetails to maintain order
	
	public PoDTO(Integer id, Integer version, Instant createdDate, String staffRecording, 
			Integer poCodeId, ContractTypeCode contractTypeCode, Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, Duration duration, Integer numOfWorkers, 
			RecordStatus recordStatus, String remarks) {
		super(id, version, createdDate, staffRecording, 
				poCodeId, contractTypeCode, supplierId, supplierVersion, supplierName, 
				processName, productionLine, 
				recordedTime, duration, numOfWorkers, recordStatus, remarks);
//		this.orderStatus = orderStatus;

	}
	
	public PoDTO(@NonNull PO po) {
		super(po);
//		this.orderStatus = po.getOrderStatus();
		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toSet());

	}
}
