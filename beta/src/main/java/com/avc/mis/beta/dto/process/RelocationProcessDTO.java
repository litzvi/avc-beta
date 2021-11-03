/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.PoProcessDTO;
import com.avc.mis.beta.dto.process.collection.ItemCountDTO;
import com.avc.mis.beta.dto.process.collection.StorageMovesGroupDTO;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.process.RelocationProcess;
import com.avc.mis.beta.entities.process.collection.ItemCount;
import com.avc.mis.beta.entities.process.collection.StorageMovesGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class RelocationProcessDTO extends PoProcessDTO {
	
	private List<StorageMovesGroupDTO> storageMovesGroups;

	public RelocationProcessDTO(@NonNull RelocationProcess relocation) {
		super(relocation);
		this.storageMovesGroups = Arrays.stream(relocation.getStorageMovesGroups())
				.map(i->{return new StorageMovesGroupDTO((StorageMovesGroup)i);}).collect(Collectors.toList());
		ItemCount[] itemCounts = relocation.getItemCounts();
		if(itemCounts != null)
			this.setItemCounts(Arrays.stream(itemCounts)
					.map(i->{return new ItemCountDTO(i);}).collect(Collectors.toList()));
	}
	
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
			process.setStorageMovesGroups(getStorageMovesGroups().stream().map(i -> i.fillEntity(new StorageMovesGroup())).toArray(StorageMovesGroup[]::new));
		}
		
		return process;
	}

}
