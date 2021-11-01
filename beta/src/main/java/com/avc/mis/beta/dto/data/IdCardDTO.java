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
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return IdCard.class;
	}
}
