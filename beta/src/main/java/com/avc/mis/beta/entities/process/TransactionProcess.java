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
import com.avc.mis.beta.entities.codes.ProductPoCode;
import com.avc.mis.beta.entities.process.group.ProcessGroup;
import com.avc.mis.beta.entities.process.group.ProcessItem;
import com.avc.mis.beta.entities.process.group.UsedItemsGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * An abstract po process that uses items from inventory in order to for producing items.
 * Has used items and process items.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "TRANSACTION_PROCESSES")
@PrimaryKeyJoinColumn(name = "processId")
public abstract class TransactionProcess<T extends ProcessItem> extends ProcessWithProduct<T> {

	@OneToMany(mappedBy = "process", 
		targetEntity = ProcessGroup.class,  //caused it to persist the group instead of merge
		orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@Where(clause = "dtype = 'UsedItemsGroup'")
	@NotEmpty(message = "Has to containe at least one used/origion storage item")
	private Set<UsedItemsGroup> usedItemGroups = new HashSet<>();

	/**
	 * Setter of poCode for transaction processes.
	 * Has to be PoCode of a product process (not GeneralPoCode)
	 * @param poCode
	 */
	public void setPoCode(ProductPoCode poCode) {
		super.setPoCode(poCode);
	}

	/**
	 * @param usedItemGroups array of UsedItemsGroup in order, if required.
	 */
	public void setUsedItemGroups(Set<UsedItemsGroup> usedItemGroups) {
		this.usedItemGroups = Insertable.setReferences(usedItemGroups, (t) -> {t.setReference(this);	return t;});
	}

}
