/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.avc.mis.beta.dao.services.PreparedStatementCreatorImpl;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class Person {
	private Integer id;
	private String name;
	private Date dob;
	private IdCard idCard;
	private ContactDetails contactDetails;
	/**
	 * @param jdbcTemplateObject
	 * @param person
	 */
	public static void insertPerson(JdbcTemplate jdbcTemplateObject, Person person) {
		if(person.getName() != null) {
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			String sql = "insert into PERSONS (name) values (?)";
			jdbcTemplateObject.update(
					new PreparedStatementCreatorImpl(sql, 
							new Object[] {person.getName()}, new String[] {"id"}), keyHolder);			
			int personId = keyHolder.getKey().intValue();
			person.setId(personId);
			
			if(person.getDob() != null) {
				sql = "INSERT INTO PERSON_DOB (personId, dob) VALUES (?, ?)";
				jdbcTemplateObject.update(sql, new Object[] {personId, person.getDob()});
			}
			
			if(person.getIdCard() != null) {
				person.getIdCard().setId(personId);
				IdCard.insertIdCard(jdbcTemplateObject, person.getIdCard());
			}
			
			ContactDetails cd = person.getContactDetails();
			if(cd == null) {
				cd = new ContactDetails();
			}
			cd.setPersonId(personId);
			ContactDetails.insertContactDetails(jdbcTemplateObject, cd);
		
			
			
		}
	}
}
