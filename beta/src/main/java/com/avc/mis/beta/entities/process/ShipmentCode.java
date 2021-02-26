/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.ShippingPort;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Basic immutable information that serves as identification for container shipment.
 * Includes a code and destination.
 * 
 * code and id are synonymous in this class
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "SHIPMENT_CODES", uniqueConstraints = 
	{ @UniqueConstraint(columnNames = { "code", "portOfDischargeId" }) })
public class ShipmentCode extends BaseEntity {

//	@EqualsAndHashCode.Include
//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;

//	@Id
//	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "com.avc.mis.beta.utilities.UseExistingIdOtherwiseGenerateUsingIdentity")
//	@GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
	@Column(nullable = false, updatable = false)
//	@EqualsAndHashCode.Include
	private String code;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "portOfDischargeId")
	@NotNull(message = "Port of discharge is mandatory")
	private ShippingPort portOfDischarge;
	
	/**
	 * @return a string representing full Shipment code. e.g. TAN-51284
	 */
	public String getValue() {
		return String.format("%s-%s", this.portOfDischarge.getCode(), this.code);
	}

//	@Override
//	public Integer getId() {
//		return code;
//	}
//
//	@Override
//	public void setId(Integer id) {
//		this.code = id;		
//	}

}
