/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PRODUCTION_PROCESS")
@PrimaryKeyJoinColumn(name = "processId")
public class ProductionProcess extends TransactionProcess {

	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@PrePersist
	@Override
	public void prePersist() {
		super.prePersist();
		if(getUsedItems().length == 0)
			throw new IllegalArgumentException("Has to containe at least one origion storage item");
		if(getProcessItems().length == 0)
			throw new IllegalArgumentException("Has to containe at least one destination storage item");
	}
	
	@Override
	public String getProcessTypeDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
