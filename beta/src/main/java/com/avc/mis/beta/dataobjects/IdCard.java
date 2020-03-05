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

import org.springframework.jdbc.core.JdbcTemplate;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="ID_INFORMATION")
public class IdCard implements Insertable {
	
	@EqualsAndHashCode.Include
	@Id
	private Integer id;
	
	@ToString.Exclude @EqualsAndHashCode.Exclude
	@JsonBackReference(value = "person_idCard")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	@MapsId
	private Person person;
	
	private String idNumber;
	private Date dob;
	private Date dateOfIssue;
	private String placeOfIssue;
	
	@EqualsAndHashCode.Include
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nationality")
	private Country nationality;
	
	@Override
	public boolean isLegal() {
		return this.id != null;
	}

	@Override
	public void setReference(Object referenced) {
		this.setPerson((Person) referenced);
	}
}
