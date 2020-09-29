/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Null;

import com.avc.mis.beta.entities.Ordinal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the form and place an item is stored.
 * e.g. unit/bag amount, location, empty bag/container weight etc.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_FORMS")
//@Inheritance(strategy=InheritanceType.JOINED)
//@PrimaryKeyJoinColumn(name = "storageBaseId")
public class Storage extends StorageBase {
		
	public Storage() {
		super();
		setDtype("Storage");
	}

		
	@Override
	@Null(message = "Internal error: Used item has to be null for storage class")
	public StorageBase getStorage() {
		return super.getStorage();
	}
		
	@Override
	@Null(message = "Internal error: Used units has to be null for storage class")
	public BigDecimal getNumberUsedUnits() {
		return super.getNumberUsedUnits();
	}
	
}
