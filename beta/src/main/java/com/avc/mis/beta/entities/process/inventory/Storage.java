/**
 * 
 */
package com.avc.mis.beta.entities.process.inventory;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Null;

import com.avc.mis.beta.entities.process.collection.ProcessItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the form and place an item is stored.
 * e.g. unit/bag amount, location, empty bag/container weight etc.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "STORAGE_FORMS")
@PrimaryKeyJoinColumn(name = "storageBaseId")
public class Storage extends StorageBase {
		
	{
		setDtype("Storage");
	}
	
	@Override
	public ProcessItem getGroup() {
		return (ProcessItem) super.getGroup();
	}

	@Override
	@Null(message = "Internal error: Used item has to be null for storage class")
	public StorageBase getStorage() {
		return super.getStorage();
	}
		
//	@Override
//	@Null(message = "Internal error: Used units has to be null for storage class")
//	public BigDecimal getNumberUsedUnits() {
//		return super.getNumberUsedUnits();
//	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof ProcessItem) {
			super.setReference(referenced); //sets group to the same value
			this.setProcessItem((ProcessItem)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a process item");
		}		
	}
	
}
