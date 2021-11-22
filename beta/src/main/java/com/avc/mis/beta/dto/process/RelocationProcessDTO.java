/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.group.StorageMovesGroupDTO;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.process.RelocationProcess;
import com.avc.mis.beta.entities.process.group.StorageMovesGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Abstract DTO for processes that move inventory between locations.
 * Could be abstract locations, like Shipping or Inventory Use.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class RelocationProcessDTO extends PoProcessDTO {
	
	private List<StorageMovesGroupDTO> storageMovesGroups;
	
	@Override
	public RelocationProcess fillEntity(Object entity) {
		RelocationProcess process;
		if(entity instanceof RelocationProcess) {
			process = (RelocationProcess) entity;
		}
		else {
			throw new IllegalStateException("Param has to be RelocationProcess class");
		}
		super.fillEntity(process);
		if(getStorageMovesGroups() == null || getStorageMovesGroups().isEmpty()) {
			throw new IllegalArgumentException("Has to containe at least one storage move");
		}
		else {
			Ordinal.setOrdinals(getStorageMovesGroups());
			process.setStorageMovesGroups(getStorageMovesGroups().stream().map(i -> i.fillEntity(new StorageMovesGroup())).collect(Collectors.toSet()));
		}
		
		return process;
	}

}
