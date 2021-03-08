/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.embedable.ContainerLoadingInfo;
import com.avc.mis.beta.dto.processinfo.LoadedItemDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ContainerLoadingDTO extends TransactionProcessDTO<ProcessItemDTO> {
	
	private ShipmentCodeDTO shipmentCode;
	
	private List<LoadedItemDTO> loadedItems; 
	
	public ContainerLoadingDTO(@NonNull ContainerLoading loading) {
		super(loading);
		this.shipmentCode = new ShipmentCodeDTO(loading.getShipmentCode());
		this.loadedItems = Arrays.stream(loading.getLoadedItems())
				.map(i->{return new LoadedItemDTO(i);}).collect(Collectors.toList());
		super.setUsedItemGroups(Arrays.stream(loading.getUsedItemGroups())
				.map(i->{return new UsedItemsGroupDTO((UsedItemsGroup)i);}).collect(Collectors.toList()));
	}
	
	public void setContainerLoadingInfo(ContainerLoadingInfo info) {
		this.shipmentCode = info.getShipmentCode();
	}

	@Override
	public String getProcessTypeDescription() {
		return getProcessName().toString();	
	}
	
}
