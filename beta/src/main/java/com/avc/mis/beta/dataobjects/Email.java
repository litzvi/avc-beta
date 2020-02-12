/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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

		String sql = "insert into emails (contactId, email) values (?, ?)";
		jdbcTemplateObject.batchUpdate(sql, 
				new BatchPreparedStatementSetter() {
		            
					public void setValues(PreparedStatement ps, int i) throws SQLException {
		                ps.setInt(1, contactId);
		                ps.setString(2, emails[i].getName());
		            }
		
		            public int getBatchSize() {
		                return emails.length;
		            }
        		});
	}
	
}
