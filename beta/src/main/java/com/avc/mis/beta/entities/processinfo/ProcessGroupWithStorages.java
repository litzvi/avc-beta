/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PROCESS_GROUPS_W_STORAGES")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class ProcessGroupWithStorages extends ProcessGroup {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Measure unit required")
	private MeasureUnit measureUnit;

}
