package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
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
public class StorageTransfer extends GeneralProcess {

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
		return "Storage transfer";
	}

}
