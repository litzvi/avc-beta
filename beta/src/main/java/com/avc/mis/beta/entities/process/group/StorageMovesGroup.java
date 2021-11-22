/**
 * 
 */
package com.avc.mis.beta.entities.process.group;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.process.storages.StorageMove;
import com.avc.mis.beta.entities.process.storages.UsedItemBase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Groups storage moves of a relocation process, 
 * in order to connect between them for display purposes.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_MOVES_GROUP")
@PrimaryKeyJoinColumn(name = "groupId")
public class StorageMovesGroup extends ProcessGroup {
	
	{
		setDtype("StorageMovesGroup");
	}
	
	@OneToMany(mappedBy = "group", targetEntity = UsedItemBase.class, orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@NotEmpty(message = "Has to containe at least one storage move")
	private Set<StorageMove> storageMoves = new HashSet<>();
	
	/**
	 * Setter for adding storage moves, 
	 * receives an array (which can be ordered, for later use to add an order to the storage moves).
	 * Filters the not legal moves and set needed references to satisfy needed foreign keys of database.
	 * @param storageMoves the storageMoves to set
	 */
	public void setStorageMoves(Set<StorageMove> storageMoves) {
		this.storageMoves = Insertable.setReferences(storageMoves, (t) -> {t.setReference(this);	return t;});
	}

}
