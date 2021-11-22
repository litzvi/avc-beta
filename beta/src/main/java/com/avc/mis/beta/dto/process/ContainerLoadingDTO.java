/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.codes.ShipmentCodeDTO;
import com.avc.mis.beta.dto.process.collectionItems.LoadedItemDTO;
import com.avc.mis.beta.dto.process.info.ContainerLoadingInfo;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.codes.ShipmentCode;
import com.avc.mis.beta.entities.process.ContainerArrival;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.entities.process.collectionItems.LoadedItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for container loading process.
 * 
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
	
	public void setContainerLoadingInfo(ContainerLoadingInfo info) {
		this.arrival = info.getArrival();
		this.shipmentCode = info.getShipmentCode();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ContainerLoading.class;
	}
	
	@Override
	public ContainerLoading fillEntity(Object entity) {
		ContainerLoading loading;
		if(entity instanceof ContainerLoading) {
			loading = (ContainerLoading) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ContainerLoading class");
		}
		super.fillEntity(loading);
		
		try {
			loading.setArrival(getArrival().fillEntity(new ContainerArrival()));
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Container Arrival is mandatory");
		}
		
		try {
			loading.setShipmentCode(getShipmentCode().fillEntity(new ShipmentCode()));
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Shipment code is mandatory");
		}
		
		if(getLoadedItems() != null && !getLoadedItems().isEmpty()) {
			Ordinal.setOrdinals(getLoadedItems());
			loading.setLoadedItems(getLoadedItems().stream().map(i -> i.fillEntity(new LoadedItem())).collect(Collectors.toSet()));
		}
		
		return loading;
	}

	
}
