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

import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
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
public class PoDTO extends ProductionProcessDTO {
	
	private OrderStatus orderStatus;
	private Set<OrderItemDTO> orderItems; //can use a SortedSet like ContactDetails to maintain order
	
	public PoDTO(Integer id, Integer version, Instant createdDate, String staffRecording, 
			Integer poCodeId, ContractType contractType, Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessType processType, ProductionLine productionLine, 
			OffsetDateTime recordedTime, Duration duration, Integer numOfWorkers, 
			ProcessStatus processStatus, String remarks,
			OrderStatus orderStatus) {
		super(id, version, createdDate, staffRecording, 
				poCodeId, contractType, supplierId, supplierVersion, supplierName, 
				processType, productionLine, 
				recordedTime, duration, numOfWorkers, processStatus, remarks);
		this.orderStatus = orderStatus;

	}
	
	public PoDTO(@NonNull PO po) {
		super(po);
		this.orderStatus = po.getOrderStatus();
		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toSet());

	}
}
