package com.avc.mis.beta.entities.process.inventory;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@ToString(callSuper = true)
@Entity
@Table(name = "USED_ITEMS")
@PrimaryKeyJoinColumn(name = "UsedItemBaseId")
public class UsedItem extends UsedItemBase {
		
	@Override
	public UsedItemsGroup getGroup() {
		return (UsedItemsGroup) super.getGroup();
	}
	
	@Override
	@NotNull(message = "Internal error: Used item has no referance to storage")
	public StorageBase getStorage() {
		return super.getStorage();
	}
	
//	@Override
//	@NotNull(message = "Used number of units is required")
//	@Positive(message = "Used number of units has to be positive")
//	public BigDecimal getNumberUsedUnits() {
//		return super.getNumberUsedUnits();
//	}
	
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
