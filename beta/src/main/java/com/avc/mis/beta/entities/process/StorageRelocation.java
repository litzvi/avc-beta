/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.codes.BasePoCode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Process for moving inventory location possibly changing storage form without processing.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_RELOCATIONS")
@PrimaryKeyJoinColumn(name = "processId")
public class StorageRelocation extends RelocationProcess {
		
	@NotNull(message = "Storage relocation has to reference a po code")
	@Override
	public BasePoCode getPoCode() {
		return super.getPoCode();
	}
	
}
