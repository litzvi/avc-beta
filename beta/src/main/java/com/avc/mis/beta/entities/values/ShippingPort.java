/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.ValueInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Shipping port entity - has a value for representation and a code
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="SHIPPING_PORT")
public class ShippingPort extends ValueEntity implements ValueInterface {

	@Column(name = "code", unique = true, nullable = false)
	@NotBlank(message = "Shipping port has to have a code")
	private String code;

}
