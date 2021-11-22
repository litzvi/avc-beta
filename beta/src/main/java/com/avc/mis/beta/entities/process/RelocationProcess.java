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
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Where;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.process.group.ProcessGroup;
import com.avc.mis.beta.entities.process.group.StorageMovesGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
	
	@OneToMany(mappedBy = "process", targetEntity = ProcessGroup.class, orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@Where(clause = "dtype = 'StorageMovesGroup'")
	@NotEmpty(message = "Has to containe at least one storage move")
	private Set<StorageMovesGroup> storageMovesGroups = new HashSet<>();

	/**
	 * Setter for adding storage move groups, 
	 * receives an array (which can be ordered, for later use to add an order to the groups).
	 * Filters the not legal moves and set needed references to satisfy needed foreign keys of database.
	 * @param StorageMoveGroups the StorageMovesGroups to set
	 */
	public void setStorageMovesGroups(Set<StorageMovesGroup> storageMovesGroups) {
		this.storageMovesGroups = Insertable.setReferences(storageMovesGroups, (t) -> {t.setReference(this);	return t;});
	}
	
	
}
