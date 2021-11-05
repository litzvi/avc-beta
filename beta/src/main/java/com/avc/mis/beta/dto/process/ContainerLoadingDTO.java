/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.process.collection.LoadedItemDTO;
import com.avc.mis.beta.dto.processInfo.ContainerLoadingInfo;
import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.codes.ShipmentCode;
import com.avc.mis.beta.entities.process.ContainerArrival;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.entities.process.collection.LoadedItem;

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
		this.loadedItems = loading.getLoadedItems().stream()
				.map(i->{return new LoadedItemDTO(i);}).collect(Collectors.toList());
	}
	
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


	@Override
	public String getProcessTypeDescription() {
		return "Container Loading";	
	}
	
}
