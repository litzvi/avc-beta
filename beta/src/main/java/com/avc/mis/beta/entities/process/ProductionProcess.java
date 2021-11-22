/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.process.group.ProcessItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
	
	/**
	 * Setter for adding items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param processItems the processItems to set
	 */
	public void setProcessItems(Set<ProcessItem> processItems) {
		super.setProcessItems(processItems);
	}
	

}
