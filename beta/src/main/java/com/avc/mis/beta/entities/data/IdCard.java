/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.DataEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.values.Country;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@EqualsAndHashCode.Include
	@Id
	private Integer id;
	
	@ToString.Exclude
	@JsonBackReference(value = "person_idCard")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	@MapsId
	private Person person;
	
	private String idNumber;
	private LocalDate dob;
	private LocalDate dateOfIssue;
	private String placeOfIssue;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nationality")
	private Country nationality;
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return this.id != null || this.person != null;
	}
	
	@Override
	public void setReference(Object referenced) {
		this.setPerson((Person) referenced);
	}

	@Override
	public String getIllegalMessage() {
		return "Internal failure: trying to add Id card without person";
	}
}
