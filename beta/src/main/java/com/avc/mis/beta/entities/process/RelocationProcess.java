/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Where;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.process.collection.ProcessGroup;
import com.avc.mis.beta.entities.process.collection.StorageMovesGroup;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Process for moving inventory location possibly changing storage form without processing.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "RELOCATION_PROCESSES")
@PrimaryKeyJoinColumn(name = "processId")
public abstract class RelocationProcess extends PoProcess {
	
	@Setter(value = AccessLevel.NONE) 
//	@Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", targetEntity = ProcessGroup.class, orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@Where(clause = "dtype = 'StorageMovesGroup'")
	@NotEmpty(message = "Has to containe at least one storage move")
	private Set<StorageMovesGroup> storageMovesGroups = new HashSet<>();

	/**
	 * Gets the list of storage move groups as an array (can be ordered).
	 * @return the StorageMovesGroup
	 */
//	public StorageMovesGroup[] getStorageMovesGroups() {
//		StorageMovesGroup[] storageMovesGroups = this.storageMovesGroups.toArray(new StorageMovesGroup[this.storageMovesGroups.size()]);
//		Arrays.sort(storageMovesGroups, Ordinal.ordinalComparator());
//		return storageMovesGroups;
//	}

	/**
	 * Setter for adding storage move groups, 
	 * receives an array (which can be ordered, for later use to add an order to the groups).
	 * Filters the not legal moves and set needed references to satisfy needed foreign keys of database.
	 * @param StorageMoveGroups the StorageMovesGroups to set
	 */
	public void setStorageMovesGroups(Set<StorageMovesGroup> storageMovesGroups) {
//		Ordinal.setOrdinals(storageMovesGroups);
		this.storageMovesGroups = Insertable.setReferences(storageMovesGroups, (t) -> {t.setReference(this);	return t;});
	}
	
	
}
