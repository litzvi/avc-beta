/**
 * 
 */
package com.avc.mis.beta.entities;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.validation.groups.OnPersist;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class representing entities that represent a real world interacting object, 
 * with a generated id and an identifying name (unique name). 
 * Usually referenced by other data entities, therefore should only be soft deleted. 
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class ObjectEntityWithIdAndName extends ObjectEntityWithId {

	@Column(unique = true, nullable = false)
	@NotBlank(message = "Name is mandetory", groups = OnPersist.class)
	private String name;

	public void setName(String name) {
		this.name = Optional.ofNullable(name).map(s -> s.trim()).orElse(null);
	}

}

