/**
 * 
 */
package com.avc.mis.beta.entities.process.inventory;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "STORAGES_BASE")
@PrimaryKeyJoinColumn(name = "usedItemBaseId")
public class StorageBase extends UsedItemBase {
	
	@JsonIgnore
	private String dtype;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processItemId", nullable = false, updatable = false)
	@NotNull(message = "Storage has to reference a process item")
	private ProcessItem processItem;
	
	@Column(nullable = false, precision = 19, scale = MeasureUnit.SCALE)
	@NotNull(message = "Unit amount is mandatory")
	@Positive(message = "Unit amount has to be positive")
	private BigDecimal unitAmount = BigDecimal.ONE;	

//	@Column(nullable = false, precision = 19, scale = MeasureUnit.SCALE)
//	@NotNull(message = "Number of units is required")
//	@Positive(message = "Number of units has to be positive")
//	private BigDecimal numberUnits;	
	
	@Column(precision = 19, scale = MeasureUnit.SCALE)
	private BigDecimal accessWeight;	
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouseLocationId")
	private Warehouse warehouseLocation;
	
	@JsonIgnore
	@ToString.Exclude 
	@OneToMany(mappedBy = "storage", fetch = FetchType.LAZY)
	private Set<UsedItemBase> usedItems;

	

}
