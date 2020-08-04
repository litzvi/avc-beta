/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.enums.ShippingContainerType;

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
public class ContainerDetails {

	@NotNull(message = "Container number is mandatory")
	private String containerNumber;

	@NotNull(message = "Seal number is mandatory")
	private String sealNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Container type/size is mandatory")
	private ShippingContainerType containerType;
	
	
//	//@Many to one, verify, cascade
//	@NotNull(message = "Container arrival has to record vehicle information")
//	private Vehicle vehicle;
//	
//	//@Many to one, verify, cascade
//	@NotNull(message = "Container arrival has to record driver information")
//	private Driver driver;
	
	public void setContainerType(String containerType) {
		this.containerType = ShippingContainerType.valueOfLabel(containerType);
	}
	
	public String getContainerType() {
		return this.containerType.toString();
	}

	
}
