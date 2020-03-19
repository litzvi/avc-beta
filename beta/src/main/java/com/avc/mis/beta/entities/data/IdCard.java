/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.avc.mis.beta.entities.BaseEntityNoId;
import com.avc.mis.beta.entities.Insertable;
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
public class IdCard extends BaseEntityNoId {
	
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
	
	@PrePersist @PreUpdate
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal())
			throw new IllegalArgumentException("Internal failure: trying to add Id card without person");
	}

	@Override
	public void setReference(Object referenced) {
		this.setPerson((Person) referenced);
	}
}
