/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="ID_INFORMATION")
public class IdCard implements Insertable, KeyIdentifiable {
	
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
	private Date dob;
	private Date dateOfIssue;
	private String placeOfIssue;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nationality")
	private Country nationality;
	
	protected boolean canEqual(Object o) {
		return KeyIdentifiable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return this.id != null;
	}

	@Override
	public void setReference(Object referenced) {
		this.setPerson((Person) referenced);
	}
}
