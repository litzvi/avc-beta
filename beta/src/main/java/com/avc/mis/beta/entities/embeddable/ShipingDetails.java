/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.enums.ShippingContainerType;
import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.utilities.LocalDateToLong;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ShipingDetails {

	private String vessel;
	private String shippingCompany;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Container type/size is mandatory")
	private ShippingContainerType containerType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destinationPortId")
	private ShippingPort portOfLoading;
	
	@Column(nullable = false)
	@NotNull(message = "Estimated date of departure is mandatory")
	@Convert(converter = LocalDateToLong.class)
	private LocalDate etd;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "portOfDischargeId")
	private ShippingPort portOfDischarge;

	@Column(nullable = false)
	@NotNull(message = "Estimated date of arrival is mandatory")
	@Convert(converter = LocalDateToLong.class)
	private LocalDate eta;
	
	public void setEtd(String etd) {
		if(etd != null)
			this.etd = LocalDate.parse(etd);
	}
	
	public void setEta(String eta) {
		if(eta != null)
			this.eta = LocalDate.parse(eta);
	}
	
	public void setContainerType(String containerType) {
		this.containerType = ShippingContainerType.valueOfLabel(containerType);
	}
	
	public String getContainerType() {
		return this.containerType.toString();
	}
}
