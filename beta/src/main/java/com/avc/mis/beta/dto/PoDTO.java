/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.ContractType;
import com.avc.mis.beta.entities.process.PO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class PoDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private ProductionProcessDTO orderProcess;
	private ContractType contractType;
	private SupplierBasic supplier;
	private OrderStatus status;
	private Set<OrderItemDTO> orderItems;
	
	public PoDTO(@NonNull PO po) {
		this.id = po.getId();
		this.orderProcess = new ProductionProcessDTO(po.getOrderProcess());
		this.contractType = po.getContractType();
		this.supplier = new SupplierBasic(po.getSupplier());
		this.status = po.getStatus();
		this.orderItems = Arrays.stream(po.getOrderItems()).map(i->{return new OrderItemDTO(i);}).collect(Collectors.toSet());

	}
}
