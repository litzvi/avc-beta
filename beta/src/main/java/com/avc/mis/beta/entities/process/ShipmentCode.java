/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.values.ShippingPort;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "SHIPMENT_CODES")
public class ShipmentCode extends BaseEntity {

	@Id
	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "com.avc.mis.beta.utilities.UseExistingIdOtherwiseGenerateUsingIdentity")
	@GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
	@Column(nullable = false, updatable = false)
	@EqualsAndHashCode.Include
	private Integer code;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "portOfDischargeId")
	@NotNull(message = "Port of discharge is mandatory")
	private ShippingPort portOfDischarge;
	
	/**
	 * @return a string representing full Shipment code. e.g. TAN-51284
	 */
	public String getValue() {
		return String.format("%s-%d", this.portOfDischarge.getCode(), this.code);
	}

	@Override
	public Integer getId() {
		return code;
	}

	@Override
	public void setId(Integer id) {
		this.code = id;		
	}
	
	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return
	 */
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
}
