package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_TRANSFERS")
@PrimaryKeyJoinColumn(name = "processId")
public class StorageTransfer extends ProductionProcess {

	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@Override
	public boolean isLegal() {
		//perhaps check if amounts fit
		return super.isLegal() && getProcessItems().length > 0 && getUsedItems().length > 0;
	}

	@Override
	public String getIllegalMessage() {
		return "Storage transfer must have in and out items";
	}

	@Override
	public String getProcessTypeDescription() {
		return "Storage transfer";
	}

}
