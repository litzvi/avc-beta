/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.CashewGrade;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for bank
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CashewGradeDTO extends ValueDTO {
	
	private String value;
	
	public CashewGradeDTO(Integer id, String value) {
		super(id);
		this.value = value;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return CashewGrade.class;
	}
	
	@Override
	public CashewGrade fillEntity(Object entity) {
		CashewGrade gradeEntity;
		if(entity instanceof CashewGrade) {
			gradeEntity = (CashewGrade) entity;
		}
		else {
			throw new IllegalStateException("Param has to be CashewGrade class");
		}
		super.fillEntity(gradeEntity);
		gradeEntity.setValue(getValue());
		return gradeEntity;
	}

}
