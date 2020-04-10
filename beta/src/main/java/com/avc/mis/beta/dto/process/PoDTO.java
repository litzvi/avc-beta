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
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
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
public class PoDTO extends ProductionProcessDTO {
	
	private SupplierBasic supplier;
	private OrderStatus orderStatus;
	private Set<OrderItemDTO> orderItems; //can use a SortedSet like ContactDetails to maintain order
	
	public PoDTO(Integer id, Long version, Instant createdDate, UserEntity staffRecording, 
			PoCode poCode, ProcessType processType, ProductionLine productionLine, 
			OffsetDateTime recordedTime, Duration duration, Integer numOfWorkers, 
			ProcessStatus processStatus, String remarks,
			Integer supplierId, Long supplierVersion, String supplierName, 
			OrderStatus orderStatus) {
		super(id, version, createdDate, staffRecording, 
				poCode, processType, productionLine, 
				recordedTime, duration, numOfWorkers, processStatus, remarks);
		this.supplier = new SupplierBasic(supplierId, supplierVersion, supplierName);
		this.orderStatus = orderStatus;

	}
	
	public PoDTO(@NonNull PO po) {
		super(po);
		this.supplier = new SupplierBasic(po.getSupplier());
		this.orderStatus = po.getOrderStatus();
		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toSet());

	}
}
