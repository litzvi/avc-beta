/**
 * 
 */
package com.avc.mis.beta.dto.process.info;

import com.avc.mis.beta.dto.basic.DataObjectWithName;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.embeddable.ContainerDetails;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;

import lombok.Value;

/**
 * For fetching ContainerArrivalDTO fields.
 * 
 * @author zvi
 *
 */
@Value
public class ContainerArrivalInfo {

	ContainerDetails containerDetails;
	ShipingDetails shipingDetails;
	DataObjectWithName<Supplier> productCompany; 
	
	
	public ContainerArrivalInfo(
			ContainerDetails containerDetails, ShipingDetails shipingDetails, 
			Integer productCompanyId, Integer productCompanyVersion, String productCompanyName) {
		this.containerDetails = containerDetails;
		this.shipingDetails = shipingDetails;
		if(productCompanyId != null)
			this.productCompany = new DataObjectWithName<Supplier>(productCompanyId, productCompanyVersion, productCompanyName);
		else
			this.productCompany = null;
	}
}
