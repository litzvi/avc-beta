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
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.enums.SupplyGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Supply group entity - used for distinguishing the items that can be supplied by different kinds of suppliers.
 * Used to filter out items for purchase orders of a supplier.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="SUPPLY_CATEGORIES")
@NamedQuery(name = "SupplyCategory.findAll", query = "select sc from SupplyCategory sc")
public class SupplyCategory extends ValueEntity {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Supply group is mandatory")
	private SupplyGroup supplyGroup;
	
}
