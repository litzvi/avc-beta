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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.processinfo.ProcessGroup;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class TransactionProcess<T extends ProcessItem> extends ProcessWithProduct<T> {

	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", targetEntity = ProcessGroup.class, orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@NotEmpty(message = "Has to containe at least one used/origion storage item")
	private Set<UsedItemsGroup> usedItemGroups = new HashSet<>();

	/**
	 * @return array of UsedItemsGroup in order
	 */
	public UsedItemsGroup[] getUsedItemGroups() {
		UsedItemsGroup[] usedItemsGroups = this.usedItemGroups.toArray(new UsedItemsGroup[this.usedItemGroups.size()]);
		Arrays.sort(usedItemsGroups, Ordinal.ordinalComparator());
		return usedItemsGroups;
	}

	/**
	 * @param usedItemGroups array of UsedItemsGroup in order, if required.
	 */
	public void setUsedItemGroups(UsedItemsGroup[] usedItemGroups) {
		Ordinal.setOrdinals(usedItemGroups);
		this.usedItemGroups = Insertable.setReferences(usedItemGroups, (t) -> {t.setReference(this);	return t;});
	}

}
