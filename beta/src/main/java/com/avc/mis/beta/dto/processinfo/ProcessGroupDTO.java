/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.process.inventory.UsedItemDTO;
import com.avc.mis.beta.entities.processinfo.ProcessGroup;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
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
