/**
 * 
 */
package com.avc.mis.beta.entities.process.inventory;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.RankedAuditedEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.ProcessGroup;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "USED_ITEMS_BASE")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class UsedItemBase extends RankedAuditedEntity {

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storageId", updatable = false) //can't update so to not confuse the processItem of a storageMove
	private StorageBase storage;

	@Column(nullable = false, precision = 19, scale = MeasureUnit.SCALE)
	@NotNull(message = "Number of units is required")
	@Positive(message = "Number of units has to be positive")
	private BigDecimal numberUnits;	

//	@Column(precision = 19, scale = MeasureUnit.SCALE)
//	private BigDecimal numberUsedUnits;	
	
	@ToString.Exclude
	@Setter(value = AccessLevel.PROTECTED) @Getter(value = AccessLevel.PROTECTED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupId", nullable = false)
	@NotNull(message = "Used items have to belong to a group categery") //storage does not have a group (maybe processItem)
	private ProcessGroup group;
	
	/**
	 * Synonym for numberUnits 
	 * @param numberUsedUnits
	 */
	public void setNumberUsedUnits(BigDecimal numberUsedUnits) {
		this.numberUnits = numberUsedUnits;
	}
	
	/**
	 * Synonym for numberUnits 
	 * @return numberUnits
	 */
	public BigDecimal getNumberUsedUnits() {
		return this.numberUnits;
	}

	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof ProcessGroup) {
			this.setGroup((ProcessGroup)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a process group");
		}		
	}
	
}
