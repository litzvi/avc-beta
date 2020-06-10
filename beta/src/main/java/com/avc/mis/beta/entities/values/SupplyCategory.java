/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="SUPPLY_CATEGORIES")
@NamedQuery(name = "SupplyCategory.findAll", query = "select sc from SupplyCategory sc")
public class SupplyCategory extends ValueEntity {
	
	@Column(name = "name", unique = true, nullable = false)
	private String value;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SupplyGroup supplyGroup;
	
	public void setValue(String value) {
		this.value = Optional.ofNullable(value).map(s -> s.trim()).orElse(null);
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}

	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue());
	}
	
	@PrePersist @PreUpdate
	@Override
	public void prePersist() {
		if(!isLegal())
			throw new IllegalArgumentException("Category name can't be blank");
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Category name can't be blank";
	}
	
}
