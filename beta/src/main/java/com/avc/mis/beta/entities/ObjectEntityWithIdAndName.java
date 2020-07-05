/**
 * 
 */
package com.avc.mis.beta.entities;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.entities.settings.OnPersist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class ObjectEntityWithIdAndName extends ObjectEntityWithId {

	@Column(unique = true, nullable = false, updatable = false)
	@NotBlank(message = "Name is mandetory", groups = OnPersist.class)
	private String name;

	public void setName(String name) {
		this.name = Optional.ofNullable(name).map(s -> s.trim()).orElse(null);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
//		return StringUtils.isNotBlank(name);
		return true;
	}
	
	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Name can't be blank";
	}
}

