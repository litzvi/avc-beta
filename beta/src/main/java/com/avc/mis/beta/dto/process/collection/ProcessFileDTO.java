/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.time.Instant;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.GeneralInfoDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.DataEntity;
import com.avc.mis.beta.entities.data.ProcessFile;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	public ProcessFileDTO(ProcessFile processFile) {
		super(processFile.getId(), processFile.getVersion());
		if(processFile.getProcess() != null)
			this.processId = processFile.getProcess().getId();
		this.address = processFile.getAddress();
		this.description = processFile.getDescription();
		this.remarks = processFile.getRemarks();
		this.createdDate = processFile.getCreatedDate();
		if(processFile.getModifiedBy() != null && processFile.getModifiedBy().getPerson() != null)
			this.modifiedBy = processFile.getModifiedBy().getPerson().getName();
		
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
