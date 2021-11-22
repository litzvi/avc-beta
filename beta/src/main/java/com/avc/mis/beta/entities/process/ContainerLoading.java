/**
 * 
 */
package com.avc.mis.beta.entities.process;

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
import com.avc.mis.beta.entities.codes.ShipmentCode;
import com.avc.mis.beta.entities.process.collectionItems.LoadedItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<LoadedItem> loadedItems = new HashSet<LoadedItem>();

	/**
	 * @param loadedItems array of loaded items in order
	 */
	public void setLoadedItems(Set<LoadedItem> loadedItems) {
		this.loadedItems = Insertable.setReferences(loadedItems, (t) -> {t.setReference(this);	return t;});
	}

}
