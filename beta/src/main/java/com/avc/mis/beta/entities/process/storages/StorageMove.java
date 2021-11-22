/**
 * 
 */
package com.avc.mis.beta.entities.process.storages;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.process.group.StorageMovesGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Storage move is a combination of used item and and storages, 
 * moves the inventory between physical or abstract locations.
 * e.g. moving to processing station, loading/exporting etc.
 * 
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
