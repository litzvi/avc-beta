/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.SubjectLinkEntity;
import com.avc.mis.beta.entities.item.BomLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *  
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class SubjectLinkDTO extends LinkDTO implements Ordinal {

	private Integer ordinal;
	
	public SubjectLinkDTO(Integer id, Integer ordinal) {
		super(id);
		this.ordinal = ordinal;
	}
	
	public SubjectLinkDTO(Integer id) {
		super(id);
	}

	public SubjectLinkDTO(SubjectLinkEntity entity) {
		super(entity);
		this.ordinal = entity.getOrdinal();
	}
}
