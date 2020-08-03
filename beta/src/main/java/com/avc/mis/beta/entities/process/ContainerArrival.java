/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.processinfo.BookedContainer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
public class ContainerArrival extends GeneralProcess {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookingId")
	@NotNull(message = "Container arrival has to reference a booking")
	private BookedContainer bookedContainer;
	
	@NotNull(message = "Container number is mandatory")
	private String containerNumber;

	@NotNull(message = "Seal number is mandatory")
	private String sealNumber;
	
	//@Many to one, verify, cascade
	@NotNull(message = "Container arrival has to record vehicle information")
	private Vehicle vehicle;
	
	//@Many to one, verify, cascade
	@NotNull(message = "Container arrival has to record driver information")
	private Driver driver;
	
	//loaded photo
	//sealed photo
	//closed photo

	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Container Arrival";
	}

}
