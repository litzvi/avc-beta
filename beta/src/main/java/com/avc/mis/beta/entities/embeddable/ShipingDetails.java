/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import java.time.LocalDate;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.utilities.LocalDateToLong;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shipping details, ports, dates, vessel and shipping company.
 * 
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
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destinationPortId")
	private ShippingPort portOfLoading;
	
	@Convert(converter = LocalDateToLong.class)
	private LocalDate etd;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "portOfDischargeId")
	private ShippingPort portOfDischarge;

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
	
}
