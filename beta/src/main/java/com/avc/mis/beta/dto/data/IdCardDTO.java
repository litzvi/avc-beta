/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.time.LocalDate;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.IdCard;
import com.avc.mis.beta.entities.values.Country;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying IdCard entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class IdCardDTO extends DataDTO {

	private String idNumber;
	private LocalDate dob;
	private LocalDate dateOfIssue;
	private String placeOfIssue;
	private Country nationality;
	
	public IdCardDTO(@NonNull IdCard idCard) {
		super(idCard.getId(), idCard.getVersion());
		this.idNumber = idCard.getIdNumber();
		this.dob = idCard.getDob();
		this.dateOfIssue = idCard.getDateOfIssue();
		this.placeOfIssue = idCard.getPlaceOfIssue();
		this.nationality = idCard.getNationality();
	}
	
	public void setDob(String dob) {
		if(dob != null)
			this.dob = LocalDate.parse(dob);
	}
	
	public void setDateOfIssue(String dateOfIssue) {
		if(dateOfIssue != null)
			this.dateOfIssue = LocalDate.parse(dateOfIssue);
	}

	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return IdCard.class;
	}
	
	@Override
	public IdCard fillEntity(Object entity) {
		IdCard idCardEntity;
		if(entity instanceof IdCard) {
			idCardEntity = (IdCard) entity;
		}
		else {
			throw new IllegalStateException("Param has to be IdCard class");
		}
		super.fillEntity(idCardEntity);
		idCardEntity.setIdNumber(getIdNumber());
		idCardEntity.setDob(getDob());
		idCardEntity.setDateOfIssue(getDateOfIssue());
		idCardEntity.setPlaceOfIssue(getPlaceOfIssue());
		idCardEntity.setNationality(getNationality());;		

		return idCardEntity;
	}

}
