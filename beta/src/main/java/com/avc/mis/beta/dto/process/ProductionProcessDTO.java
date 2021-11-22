package com.avc.mis.beta.dto.process;

import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.group.ProcessItemDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.process.group.ProcessItem;
import com.avc.mis.beta.entities.process.group.UsedItemsGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for production processes. e.g. cleaning, roasting, packing etc.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ProductionProcessDTO extends TransactionProcessDTO<ProcessItemDTO> {
	
		
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ProductionProcess.class;
	}
	
	@Override
	public ProductionProcess fillEntity(Object entity) {
		ProductionProcess productionProcess;
		if(entity instanceof ProductionProcess) {
			productionProcess = (ProductionProcess) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ProductionProcess class");
		}
		super.fillEntity(productionProcess);
		if(getProcessItems() == null || getProcessItems().isEmpty()) {
			throw new IllegalArgumentException("Has to containe at least one process item");
		}
		else {
			Ordinal.setOrdinals(getProcessItems());
			productionProcess.setProcessItems(getProcessItems().stream().map(i -> i.fillEntity(new ProcessItem())).collect(Collectors.toSet()));
		}
		if(getUsedItemGroups() == null || getUsedItemGroups().isEmpty()) {
			throw new IllegalArgumentException("Has to containe at least one used item group");
		}
		else {
			Ordinal.setOrdinals(getUsedItemGroups());
			productionProcess.setUsedItemGroups(getUsedItemGroups().stream().map(i -> i.fillEntity(new UsedItemsGroup())).collect(Collectors.toSet()));
		}
		
		return productionProcess;
	}
	
}
