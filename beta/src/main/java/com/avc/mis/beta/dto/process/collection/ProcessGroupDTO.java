/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.process.collection.ProcessGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO class for ProcessGroup which contains information used by all groups in collection of groups in a processes.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class ProcessGroupDTO extends SubjectDataDTO {

	private String groupName;

	@JsonIgnore
	private boolean tableView;

	public ProcessGroupDTO(Integer id, Integer version, Integer ordinal,
			String groupName, boolean tableView) {
		super(id, version, ordinal);
		this.groupName = groupName;
		this.tableView = tableView;
	}	

	public ProcessGroupDTO(ProcessGroup group) {
		super(group.getId(), group.getVersion(), group.getOrdinal());
		this.groupName = group.getGroupName();
		this.tableView = group.isTableView();
	}
	
}
