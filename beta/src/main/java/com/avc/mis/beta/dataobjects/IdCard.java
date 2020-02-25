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
@Entity
@Table(name="ID_INFORMATION")
public class IdCard {
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nationality")
	private Country nationality;
	
	/**
	 * @param jdbcTemplateObject
	 * @param idCard
	 */
	public static void insertIdCard(JdbcTemplate jdbcTemplateObject, IdCard idCard) {
		Integer personId = idCard.getId();
		if(personId == null) {
			throw new IllegalArgumentException("Inserted id card has to have an id(person id)");
		}
		if(idCard.getIdNumber() != null) {
			int nationalityId = idCard.getNationality() != null ? idCard.getNationality().getId(): null;
			String sql = "INSERT INTO ID_INFORMATION (personId, nationality, IdNumber, dateOfIssue, placeOfIssue, dob) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			jdbcTemplateObject.update(sql, new Object[] {personId, nationalityId, 
					idCard.getIdNumber(), idCard.getDateOfIssue(), idCard.getPlaceOfIssue(), idCard.getDob()});
		}
	}
}
