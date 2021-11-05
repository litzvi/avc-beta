/**
 * 
 */
package com.avc.mis.beta.entities.process.collection;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Base abstract class for grouping process's collections.
 * Used for display purposes. e.g. group name and if it's in table view.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "PROCESS_GROUPS"
, indexes = @Index(columnList = "dtype")
)
@Inheritance(strategy=InheritanceType.JOINED)
//@MappedSuperclass
public abstract class ProcessGroup extends ProcessInfoEntity {

	@JsonIgnore
	private String dtype;

//	@Setter(value = AccessLevel.PROTECTED) 
	@JsonIgnore
	@Column(nullable = false)
	private boolean tableView = false;
	
	private String groupName;

	public void setGroupName(String groupName) {
		this.groupName = Optional.ofNullable(groupName).map(s -> s.trim()).orElse(null);
	}
	
}
