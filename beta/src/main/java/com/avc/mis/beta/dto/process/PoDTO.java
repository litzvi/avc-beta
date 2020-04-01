/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.entities.data.Staff;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.ProcessStatus;
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
	
//	private ContractType contractType;
	private SupplierBasic supplier;
	private OrderStatus orderStatus;
	private Set<OrderItemDTO> orderItems;
	
	public PoDTO(Integer id, Long version, Instant createdDate, Staff staffRecording, 
			PoCode poCode, ProcessType processType, ProductionLine productionLine, 
			LocalDateTime recordedTime, Duration duration, Integer numOfWorkers, 
			ProcessStatus processStatus, String remarks,
			Integer supplierId, Long supplierVersion, String supplierName, 
			OrderStatus orderStatus) {
		super(id, version, createdDate, staffRecording, 
				poCode, processType, productionLine, 
				recordedTime, duration, numOfWorkers, processStatus, remarks);
		this.supplier = new SupplierBasic(supplierId, supplierVersion, supplierName);
//		this.contractType = contractType;
		this.orderStatus = orderStatus;
//		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toSet());

	}
	
	public PoDTO(@NonNull PO po) {
		super(po);
//		this.contractType = po.getPoCode().getContractType();
		this.supplier = new SupplierBasic(po.getSupplier());
		this.orderStatus = po.getOrderStatus();
		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toSet());

	}
}
