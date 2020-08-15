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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.processinfo.BookedContainer;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "SHIPPMENT_BOOKINGS")
@PrimaryKeyJoinColumn(name = "processId")
public class ShipmentBooking extends GeneralProcess {
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "booking", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.ALL, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	@NotEmpty(message = "Shipment booking has to have at least one container bookd")
	private Set<BookedContainer> bookedContainers = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "logisticsCompanyId")
	private Supplier logisticsCompany;
		
	private String personInCharge;
	
	/**
	 * Gets the list of Containers as an array (can be ordered).
	 * @return the bookedContainers
	 */
	public BookedContainer[] getBookedContainers() {
		return this.bookedContainers.toArray(new BookedContainer[this.bookedContainers.size()]);
	}

	/**
	 * Setter for adding booked containers to shipment booking, 
	 * receives an array (which can be ordered, for later use to add an order to the booking lines).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param bookedContainers the bookedContainers to set
	 */
	public void setBookedContainers(BookedContainer[] bookedContainers) {
		this.bookedContainers = Insertable.setReferences(bookedContainers, (t) -> {t.setReference(this);	return t;});
	}

	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Container Booking";
	}
	
}
