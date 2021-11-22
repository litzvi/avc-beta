/**
 * 
 */
package com.avc.mis.beta.dto.process.collectionItems;

import java.time.Instant;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.process.collectionItems.ProcessFile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Pointer to file owned by a process. Contains address, description and audit data.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
public class ProcessFileDTO extends DataDTO {

	@EqualsAndHashCode.Exclude
	private Integer processId;
	private String address;

	private String description;
	private String remarks;	
	
	@EqualsAndHashCode.Exclude
	private Instant createdDate;
	@EqualsAndHashCode.Exclude
	private String modifiedBy;


	public ProcessFileDTO(Integer id, Integer version, @NonNull Integer processId, @NonNull String address, 
			String description, String remarks, Instant createdDate, String modifiedBy) {
		super(id, version);
		this.processId = processId;
		this.address = address;
		this.description = description;
		this.remarks = remarks;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ProcessFile.class;
	}
	
	@Override
	public ProcessFile fillEntity(Object entity) {
		ProcessFile processFile;
		if(entity instanceof ProcessFile) {
			processFile = (ProcessFile) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ProcessFile class");
		}
		super.fillEntity(processFile);
		processFile.setAddress(getAddress());
		processFile.setDescription(getDescription());
		processFile.setRemarks(getRemarks());
		
		return processFile;
	}

	

}
