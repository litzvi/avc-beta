/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Po Process for production processing. e.g. cashew cleaning, roasting, packing.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PRODUCTION_PROCESS")
@PrimaryKeyJoinColumn(name = "processId")
public class ProductionProcess extends TransactionProcess<ProcessItem> {

	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
		
	@Override
	public String getProcessTypeDescription() {
		return "Production";
	}

}
