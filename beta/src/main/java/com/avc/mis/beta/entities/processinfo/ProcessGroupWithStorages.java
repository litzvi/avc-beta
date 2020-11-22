/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * ProcessGroup for collections that contain storages.
 * Has information describing joint information of all storages.
 * e.g. measure unit
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PROCESS_GROUPS_W_STORAGES")
@PrimaryKeyJoinColumn(name = "groupId")
public abstract class ProcessGroupWithStorages extends ProcessGroup {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Measure unit required")
	private MeasureUnit measureUnit;

}
