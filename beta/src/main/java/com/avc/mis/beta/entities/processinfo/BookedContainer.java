/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.AuditedEntity;
import com.avc.mis.beta.entities.enums.ShippingContainerType;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ShipmentBooking;
import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.utilities.LocalDateToLong;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "BOOKED_CONTAINERS")
public class BookedContainer extends AuditedEntity {
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processId", updatable = false, nullable = false)
	private ShipmentBooking booking;
	
	private String billNumber;
	private String vessel;
	private String shippingCompany;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destinationPortId")
	private ShippingPort destinationPort;

	@Column(nullable = false)
	@NotNull(message = "Estimated date of departure is mandatory")
	@Convert(converter = LocalDateToLong.class)
	private LocalDate etd;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Container type/size is mandatory")
	private ShippingContainerType containerType;
	
//	@OneToMany(mappedBy = "bookedContainer", fetch = FetchType.LAZY)
//	private ContainerShipping containerShipping;
		
	public void setEtd(String etd) {
		if(etd != null)
			this.etd = LocalDate.parse(etd);
	}
	
	public void setContainerType(String containerType) {
		this.containerType = ShippingContainerType.valueOfLabel(containerType);
	}
	
	public String getContainerType() {
		return this.containerType.toString();
	}
	
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof PO) {
			this.setBooking((ShipmentBooking)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a shipment booking");
		}		
	}
}
