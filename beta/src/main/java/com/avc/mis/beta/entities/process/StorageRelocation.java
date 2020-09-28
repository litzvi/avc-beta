/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.processinfo.StorageMove;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_RELOCATIONS")
@PrimaryKeyJoinColumn(name = "processId")
public class StorageRelocation extends PoProcess {
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<StorageMove> storageMoves = new HashSet<>();

	/**
	 * Gets the list of storage moves as an array (can be ordered).
	 * @return the storageMoves
	 */
	public StorageMove[] getStorageMoves() {
		return this.storageMoves.toArray(new StorageMove[this.storageMoves.size()]);
	}

	/**
	 * Setter for adding storage moves, 
	 * receives an array (which can be ordered, for later use to add an order to the storage moves).
	 * Filters the not legal moves and set needed references to satisfy needed foreign keys of database.
	 * @param storageMoves the storageMoves to set
	 */
	public void setStorageMoves(StorageMove[] storageMoves) {
		this.storageMoves = Insertable.setReferences(storageMoves, (t) -> {t.setReference(this);	return t;});
	}

	@Override
	public String getProcessTypeDescription() {
		return "Storage Relocation";
	}

}
