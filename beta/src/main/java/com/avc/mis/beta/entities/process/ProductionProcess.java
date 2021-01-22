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
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.ProductWeightedPo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Po Process for production processing. e.g. cashew cleaning, roasting, packing.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "PRODUCTION_PROCESS")
@PrimaryKeyJoinColumn(name = "processId")
public class ProductionProcess extends TransactionProcess<ProcessItem> {
	
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<ProductWeightedPo> productWeightedPos = new HashSet<>();

	
	/**
	 * Setter for adding items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param processItems the processItems to set
	 */
	public void setProcessItems(ProcessItem[] processItems) {
		super.setProcessItems(processItems);
	}
	
	/**
	 * Gets the list of Process Items as an array (can be ordered).
	 * @return the processItems
	 */
	@NotEmpty(message = "Has to containe at least one destination-storage-item (process item)")
	@Override
	public ProcessItem[] getProcessItems() {
		return super.getProcessItems();
	}
	
	
	public ProductWeightedPo[] getProductWeightedPos() {
		if(this.productWeightedPos == null)
			return null;
		ProductWeightedPo[] productWeightedPos = this.productWeightedPos.toArray(new ProductWeightedPo[this.productWeightedPos.size()]);
		Arrays.sort(productWeightedPos, Ordinal.ordinalComparator());
		return productWeightedPos;
	}

	public void setProductWeightedPos(ProductWeightedPo[] productWeightedPos) {
		Ordinal.setOrdinals(productWeightedPos);
		this.productWeightedPos = Insertable.setReferences(productWeightedPos, (t) -> {t.setReference(this);	return t;});
	}
	
	@NotNull(message = "Receipt has to reference a po code")
	@Override
	public BasePoCode getPoCode() {
		return super.getPoCode();
	}


}
