package com.avc.mis.beta.dto.process;

import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.group.ProcessItemDTO;
import com.avc.mis.beta.dto.process.group.UsedItemsGroupDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.process.group.ProcessItem;
import com.avc.mis.beta.entities.process.group.UsedItemsGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
public class StorageTransferDTO extends TransactionProcessDTO<ProcessItemDTO> {
		

	
//	@Override
//	public List<ProcessItemDTO> getProcessItems() {
//		return super.getProcessItems();
//	}
	
	@Override
	public void setProcessItems(List<ProcessItemDTO> processItems) {
		super.setProcessItems(processItems);
	}

	
	@Override
	public List<UsedItemsGroupDTO> getUsedItemGroups() {
		return super.getUsedItemGroups();
	}

	@Override
	public void setUsedItemGroups(List<UsedItemsGroupDTO> usedItemGroups) {
		super.setUsedItemGroups(usedItemGroups);
	}

	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return StorageTransfer.class;
	}
	
	@Override
	public StorageTransfer fillEntity(Object entity) {
		StorageTransfer transfer;
		if(entity instanceof StorageTransfer) {
			transfer = (StorageTransfer) entity;
		}
		else {
			throw new IllegalStateException("Param has to be StorageTransfer class");
		}
		super.fillEntity(transfer);	
		if(getProcessItems() == null || getProcessItems().isEmpty()) {
			throw new IllegalArgumentException("Has to containe at least one process item");
		}
		else {
			Ordinal.setOrdinals(getProcessItems());
			transfer.setProcessItems(getProcessItems().stream().map(i -> i.fillEntity(new ProcessItem())).collect(Collectors.toSet()));
		}
		if(getUsedItemGroups() == null || getUsedItemGroups().isEmpty()) {
			throw new IllegalArgumentException("Has to containe at least one used item group");
		}
		else {
			Ordinal.setOrdinals(getUsedItemGroups());
			transfer.setUsedItemGroups(getUsedItemGroups().stream().map(i -> i.fillEntity(new UsedItemsGroup())).collect(Collectors.toSet()));
		}

		return transfer;
	}
	
}
