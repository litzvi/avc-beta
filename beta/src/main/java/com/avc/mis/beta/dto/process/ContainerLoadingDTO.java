/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.PoProcessDTO;
import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.process.collection.LoadedItemDTO;
import com.avc.mis.beta.dto.process.collection.ProcessItemDTO;
import com.avc.mis.beta.dto.processInfo.ContainerLoadingInfo;
import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
import com.avc.mis.beta.entities.process.ContainerLoading;

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
public class ContainerLoadingDTO extends RelocationProcessDTO {
	
	private ContainerArrivalBasic arrival;
	private ShipmentCodeDTO shipmentCode;
	
	private List<LoadedItemDTO> loadedItems; 
	
	public ContainerLoadingDTO(@NonNull ContainerLoading loading) {
		super(loading);
		this.arrival = new ContainerArrivalBasic(loading.getArrival());
		this.shipmentCode = new ShipmentCodeDTO(loading.getShipmentCode());
		this.loadedItems = Arrays.stream(loading.getLoadedItems())
				.map(i->{return new LoadedItemDTO(i);}).collect(Collectors.toList());
//		super.setUsedItemGroups(Arrays.stream(loading.getUsedItemGroups())
//				.map(i->{return new UsedItemsGroupDTO((UsedItemsGroup)i);}).collect(Collectors.toList()));
	}
	
	public void setContainerLoadingInfo(ContainerLoadingInfo info) {
		this.arrival = info.getArrival();
		this.shipmentCode = info.getShipmentCode();
	}

	@Override
	public String getProcessTypeDescription() {
		return "Container Loading";	
	}
	
}
