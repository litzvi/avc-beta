/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.time.LocalDate;

import com.avc.mis.beta.dto.BaseDTOWithVersion;
import com.avc.mis.beta.entities.data.Country;
import com.avc.mis.beta.entities.data.IdCard;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class IdCardDTO extends BaseDTOWithVersion {
//	@EqualsAndHashCode.Exclude
//	private Integer id;
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
}
