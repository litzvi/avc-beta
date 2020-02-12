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
public class Fax {
	
	private int id;
	private int contactId;
	private String name;
	/**
	 * @param jdbcTemplateObject
	 * @param contactId 
	 * @param faxes
	 */
	public static void insertFaxes(JdbcTemplate jdbcTemplateObject, int contactId, Fax[] faxes) {

		String sql = "insert into faxes (contactId, fax) values (?, ?)";
		jdbcTemplateObject.batchUpdate(sql, 
				new BatchPreparedStatementSetter() {
		            
					public void setValues(PreparedStatement ps, int i) throws SQLException {
		                ps.setInt(1, contactId);
		                ps.setString(2, faxes[i].getName());
		            }
		
		            public int getBatchSize() {
		                return faxes.length;
		            }
        		});
	}
	
}
