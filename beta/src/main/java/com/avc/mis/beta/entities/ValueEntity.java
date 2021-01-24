/**
 * 
 */
package com.avc.mis.beta.entities;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class representing an entity usually already exists in the database. e.g. country, city.
 * Typically referenced by user data but only references other value entities.
 * Referenced by user data, therefore should only be soft deleted but not physically deleted.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class ValueEntity extends BaseEntity implements SoftDeleted, ValueInterface {
	
//	@EqualsAndHashCode.Include
//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
	
	@Column(nullable = false, updatable = false, columnDefinition = "boolean default true")
	private boolean active = true;
	
	@Column(name = "name", unique = true, nullable = false)
	@NotBlank(message = "Object value(name) can't be blank")
	private String value;
	
	public void setValue(String value) {
		this.value = Optional.ofNullable(value).map(s -> s.trim()).orElse(null);
	}
}
