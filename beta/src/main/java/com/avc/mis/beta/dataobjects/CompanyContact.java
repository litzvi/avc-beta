/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@IdClass(CompanyContactPK.class)
@Entity
@Table(name = "COMPANY_CONTACTS")
public class CompanyContact {

//	@Id
//	@Column(name = "companyId")
//	private Integer companyId;

	@Id
	@ToString.Exclude @EqualsAndHashCode.Exclude
	@JsonBackReference(value = "company_companyContacts")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId", updatable = false)
	private Company company;

	@Id
	@ManyToOne(cascade = {CascadeType.PERSIST})
	@JoinColumn(name = "personId", updatable = false)
	private Person person;

	@ManyToOne
	@JoinColumn(name = "positionId")
	private CompanyPosition position;

	@Column(columnDefinition = "boolean default true", nullable = false)
	private boolean isActive;

	/**
	 * @param jdbcTemplateObject
	 * @param cc
	 *//*
		 * public static void insertCompanyContact(JdbcTemplate jdbcTemplateObject,
		 * CompanyContact cc) { if(cc.getCompanyId() != null && cc.getPerson() != null)
		 * { Person.insertPerson(jdbcTemplateObject, cc.getPerson()); Integer personId =
		 * cc.getPerson().getId(); if(personId != null) { Integer positionId =
		 * cc.getPosition() != null ? cc.getPosition().getId(): null; String sql =
		 * "INSERT INTO COMPANY_CONTACTS (personId, companyId, positionId) VALUES (?, ?, ?)"
		 * ; jdbcTemplateObject.update(sql, new Object[] {personId, cc.getCompanyId(),
		 * positionId}); } }
		 * 
		 * }
		 */

	/**
	 * @param jdbcTemplateObject
	 */
	/*
	 * public void editCompanyContact(JdbcTemplate jdbcTemplateObject) { if
	 * (getCompanyId() == null) { throw new
	 * IllegalArgumentException("Company id can't be null"); } if (getPerson() ==
	 * null && getPerson().getId() == null) { throw new
	 * IllegalArgumentException("Contact person id can't be null"); } // TODO if
	 * removed change to isActive=false // if position changed, delete current
	 * position and add new one // if person changed call edit on person. // perhaps
	 * should add a personId, in case the person isn't changed
	 * 
	 * }
	 */

}
