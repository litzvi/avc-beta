package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents items used for processing (input items of a process). 
 * Used items refer to existing stored inventory.
 * e.g. raw cashew, sugar, oil, bags etc.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "USED_ITEMS")
@PrimaryKeyJoinColumn(name = "UsedItemBaseId")
public class UsedItem extends UsedItemBase {
	
//	public UsedItem() {
//		super();
//		super.setNumberUsedUnits(BigDecimal.ONE);
//	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupId")
	@NotNull(message = "Used items have to belong to a group categery")
	private UsedItemsGroup group;
	
	@Override
	@NotNull(message = "Internal error: Used item has no referance to storage")
	public StorageBase getStorage() {
		return super.getStorage();
	}
	
	@Override
	@NotNull(message = "Used number of units is required")
	@Positive(message = "Used number of units has to be positive")
	public BigDecimal getNumberUsedUnits() {
		return super.getNumberUsedUnits();
	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof UsedItemsGroup) {
			this.setGroup((UsedItemsGroup)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a used item group");
		}		
	}
	
}
