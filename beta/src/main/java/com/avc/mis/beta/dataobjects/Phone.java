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
public class Phone {
	
	private int id;
	private int contactId;
	private String name;
	
	/**
	 * @param jdbcTemplateObject
	 * @param contactId 
	 * @param phones
	 */
	public static void insertPhones(JdbcTemplate jdbcTemplateObject, int contactId, Phone[] phones) {

		String sql = "insert into category_suppliers (contactId, phone) values (?, ?)";
		jdbcTemplateObject.batchUpdate(sql, 
				new BatchPreparedStatementSetter() {
		            
					public void setValues(PreparedStatement ps, int i) throws SQLException {
		                ps.setInt(1, contactId);
		                ps.setString(2, phones[i].getName());
		            }
		
		            public int getBatchSize() {
		                return phones.length;
		            }
        		});
	}

	
	
	
}
