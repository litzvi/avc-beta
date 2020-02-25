/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "PERSONS")
public class Person {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable = false)
	private String name;

	@JsonManagedReference(value = "person_idCard")
	@OneToOne(mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	private IdCard idCard;

	@JsonManagedReference(value = "person_contactDetails")
	@OneToOne(mappedBy = "person", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
	private ContactDetails contactDetails;

	/**
	 * @param jdbcTemplateObject
	 * @param person
	 *//*
		 * public static void insertPerson(JdbcTemplate jdbcTemplateObject, Person
		 * person) { if(person.getName() != null) { GeneratedKeyHolder keyHolder = new
		 * GeneratedKeyHolder(); String sql = "insert into PERSONS (name) values (?)";
		 * jdbcTemplateObject.update( new PreparedStatementCreatorImpl(sql, new Object[]
		 * {person.getName()}, new String[] {"id"}), keyHolder); int personId =
		 * keyHolder.getKey().intValue(); person.setId(personId);
		 * 
		 * 
		 * if(person.getIdCard() != null) { person.getIdCard().setId(personId);
		 * IdCard.insertIdCard(jdbcTemplateObject, person.getIdCard()); }
		 * 
		 * ContactDetails cd = person.getContactDetails(); if(cd == null) { cd = new
		 * ContactDetails(); } cd.setPersonId(personId);
		 * ContactDetails.insertContactDetails(jdbcTemplateObject, cd);
		 * 
		 * 
		 * 
		 * } }
		 */
}
