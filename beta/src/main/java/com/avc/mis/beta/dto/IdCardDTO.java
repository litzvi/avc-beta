/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avc.mis.beta.entities.data.Country;
import com.avc.mis.beta.entities.data.IdCard;
import com.avc.mis.beta.entities.data.Person;

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
	private Date dob;
	private Date dateOfIssue;
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
