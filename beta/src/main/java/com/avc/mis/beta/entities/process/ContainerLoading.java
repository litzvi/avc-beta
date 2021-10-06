/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.codes.ShipmentCode;
import com.avc.mis.beta.entities.process.collection.LoadedItem;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Process of loading a container for export.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CONTAINER_LOADINGS")
@PrimaryKeyJoinColumn(name = "processId")
public class ContainerLoading extends RelocationProcess {

	@NotNull(message = "Container is mandatory")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "arrivalId")
	private ContainerArrival arrival;
	
	@NotNull(message = "Shipment code is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "shipment_code_code")
	private ShipmentCode shipmentCode;

//	@Valid
//	@Embedded
//	@NotNull(message = "Container details is mandatory")
//	private ContainerDetails containerDetails;
	
//	@Valid
//	@Embedded
//	@NotNull(message = "Shipping details is mandatory")
//	private ShipingDetails shipingDetails;

	//not used for now
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@NotEmpty(message = "Loaded item line has to contain at least one storage line")
	private Set<LoadedItem> loadedItems = new HashSet<LoadedItem>();

	/**
	 * @param loadedItems array of loaded items in order
	 */
	public void setLoadedItems(LoadedItem[] loadedItems) {
		Ordinal.setOrdinals(loadedItems);
		this.loadedItems = Insertable.setReferences(loadedItems, (t) -> {t.setReference(this);	return t;});
	}
	
	/**
	 * @return array of loaded items in given order
	 */
	public LoadedItem[] getLoadedItems() {
		LoadedItem[] loadedItems = this.loadedItems.toArray(new LoadedItem[this.loadedItems.size()]);
		Arrays.sort(loadedItems, Ordinal.ordinalComparator());
		return loadedItems;
	}

}
