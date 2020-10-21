/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.validation.groups.OnPersist;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_MOVES")
//@Inheritance(strategy=InheritanceType.JOINED)
//@PrimaryKeyJoinColumn(name = "storageBaseId")
public class StorageMove extends StorageBase {
	
	public StorageMove() {
		super();
		super.setNumberUsedUnits(BigDecimal.ONE);
		setDtype("StorageMove");
	}
	
	@ToString.Exclude
	@NotNull(message = "System error: Process not referenced", groups = OnPersist.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processId", updatable = false)
	private StorageRelocation process;
	
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
		if(referenced instanceof StorageRelocation) {
			this.setProcess((StorageRelocation)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a storage relocation");
		}		
	}


}
