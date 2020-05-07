/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Zvi
 *
 */
@Entity
@Table(name = "ORDER_RECEIPTS")
@PrimaryKeyJoinColumn(name = "processId")
public class Receipt extends ProductionProcess {

	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@Override
	public boolean isLegal() {
		return super.isLegal() && getProcessItems().length > 0;
	}

	@Override
	public String getIllegalMessage() {
		return super.getIllegalMessage() + " or no items received ";
	}

}
