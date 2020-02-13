/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class Email {

	private int id;
	private int contactId;
	private String name;
	/**
	 * @param jdbcTemplateObject
	 * @param contactId 
	 * @param emails
	 */
	public static void insertEmails(JdbcTemplate jdbcTemplateObject, int contactId, Email[] emails) {

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Email email: emails) {
			if(email.getName() != null) {
				batchArgs.add(new Object[] {contactId, email.getName()});
			}
		}
		String sql = "insert into emails (contactId, email) values (?, ?)";
		jdbcTemplateObject.batchUpdate(sql, batchArgs, new int[]{Types.INTEGER, Types.VARCHAR});
		
	}
	
}
