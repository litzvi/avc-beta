/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.DataEntity;
import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.utilities.LocalDateToLong;
import com.avc.mis.beta.validation.groups.OnPersist;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="ID_INFORMATION")
public class IdCard extends DataEntity {
	
//	@EqualsAndHashCode.Include
//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
	
	@ToString.Exclude
	@JsonBackReference(value = "person_idCard")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
//	@MapsId
	@NotNull(message = "Internal failure: trying to add Id card without person", groups = OnPersist.class)
	private Person person;
	
	@Column(nullable = false, updatable = false, unique = true)
	private String idNumber;
	
	@Convert(converter = LocalDateToLong.class)
	private LocalDate dob;
	
	@Convert(converter = LocalDateToLong.class)
	private LocalDate dateOfIssue;
	private String placeOfIssue;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nationality")
	private Country nationality;
//	
//	public void setDob(String dob) {
//		if(dob != null)
//			this.dob = LocalDate.parse(dob);
//	}
//	
//	public void setDateOfIssue(String dateOfIssue) {
//		if(dateOfIssue != null)
//			this.dateOfIssue = LocalDate.parse(dateOfIssue);
//	}
	
	@Override
	public void setReference(Object referenced) {
		this.setPerson((Person) referenced);
	}

}
