/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.time.LocalDate;

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
@NoArgsConstructor
public class IdCardDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private String idNumber;
	private LocalDate dob;
	private LocalDate dateOfIssue;
	private String placeOfIssue;
	private Country nationality;
	
	public IdCardDTO(@NonNull IdCard idCard) {
		this.id = idCard.getId();
		this.idNumber = idCard.getIdNumber();
		this.dob = idCard.getDob();
		this.dateOfIssue = idCard.getDateOfIssue();
		this.placeOfIssue = idCard.getPlaceOfIssue();
		this.nationality = idCard.getNationality();
	}
}
