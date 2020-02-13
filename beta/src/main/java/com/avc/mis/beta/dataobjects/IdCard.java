/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.Date;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class IdCard {
	private Integer id;
	private String idNumber;
	private Date dateOfIssue;
	private String placeOfIssue;
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
			String sql = "INSERT INTO ID_INFORMATION (personId, nationality, IdNumber, dateOfIssue, placeOfIssue) "
					+ "VALUES (?, ?, ?, ?, ?)";
			jdbcTemplateObject.update(sql, new Object[] {personId, nationalityId, 
					idCard.getIdNumber(), idCard.getDateOfIssue(), idCard.getPlaceOfIssue()});
		}
	}
}
