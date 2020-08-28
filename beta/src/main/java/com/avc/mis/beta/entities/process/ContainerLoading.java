/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.Arrays;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.processinfo.LoadedItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CONTAINER_LOADINGS")
@PrimaryKeyJoinColumn(name = "processId")
public class ContainerLoading extends TransactionProcess<LoadedItem> {
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private ShipmentCode shipmentCode;
	
	@Valid
	@Embedded
	@NotNull(message = "Container details is mandatory")
	private ContainerDetails containerDetails;
	
	@Valid
	@Embedded
	@NotNull(message = "Shipping details is mandatory")
	private ShipingDetails shipingDetails;
	
		
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "containerArrivalId", nullable = false)
//	@NotNull(message = "Container loading has to reference container arrival")
//	private ContainerArrival containerArrival;
	
	public void setLoadedItems(LoadedItem[] loadedItems) {
		super.setProcessItems(loadedItems);
	}
	
	public LoadedItem[] getLoadedItems() {
		Object[] processItems = super.getProcessItems();
		return Arrays.copyOf(processItems, processItems.length, LoadedItem[].class);
	}
	
	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Container Loading";
	}
}
