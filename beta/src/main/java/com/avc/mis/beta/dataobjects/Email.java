/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
public class Email {

	private int id;
	private int contactId;
	private String email;
	/**
	 * @param jdbcTemplateObject
	 * @param contactId 
	 * @param emails
	 */
	public static void insertEmails(JdbcTemplate jdbcTemplateObject, int contactId, Email[] emails) {

		String sql = "insert into faxes (contactId, email) values (?, ?)";
		jdbcTemplateObject.batchUpdate(sql, 
				new BatchPreparedStatementSetter() {
		            
					public void setValues(PreparedStatement ps, int i) throws SQLException {
		                ps.setInt(1, contactId);
		                ps.setString(2, emails[i].getEmail());
		            }
		
		            public int getBatchSize() {
		                return emails.length;
		            }
        		});
	}
	
}
