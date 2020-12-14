/**
 * 
 */
package com.avc.mis.beta.entities.process.inventory;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.processinfo.StorageMovesGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_MOVES")
@PrimaryKeyJoinColumn(name = "storageBaseId")
public class StorageMove extends StorageBase {
	
	public StorageMove() {
		super();
		super.setNumberUnits(BigDecimal.ONE);
//		setDtype("StorageMove");
	}
	
	{
		setDtype("StorageMove");
	}

	
	@Override
	public StorageMovesGroup getGroup() {
		return (StorageMovesGroup) super.getGroup();
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
		if(referenced instanceof StorageMovesGroup) {
			super.setReference(referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a storage move group");
		}		
	}

}
