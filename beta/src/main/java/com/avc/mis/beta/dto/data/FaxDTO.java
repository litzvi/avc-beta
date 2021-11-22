/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.Fax;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying Fax entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FaxDTO extends SubjectDataDTO {

	private String value;
	
	public FaxDTO(@NonNull Fax fax) {
		super(fax.getId(), fax.getVersion(), fax.getOrdinal());
		this.value = fax.getValue();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Fax.class;
	}
	
	@Override
	public Fax fillEntity(Object entity) {
		Fax faxEntity;
		if(entity instanceof Fax) {
			faxEntity = (Fax) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Fax class");
		}
		super.fillEntity(faxEntity);
		faxEntity.setValue(getValue());;
		return faxEntity;
	}

}
