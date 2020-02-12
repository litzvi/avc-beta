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
public class Address {

	private int id;
	private int contactId;
	private String streetAddress;
	private City city;
	/**
	 * @param jdbcTemplateObject
	 * @param contactId 
	 * @param addresses
	 */
	public static void insertAddresses(JdbcTemplate jdbcTemplateObject, int contactId, Address[] addresses) {

		String sql = "insert into addresses (contactId, streetAddress, cityId) values (?, ?, ?)";
		jdbcTemplateObject.batchUpdate(sql, 
				new BatchPreparedStatementSetter() {
		            
					public void setValues(PreparedStatement ps, int i) throws SQLException {
		                ps.setInt(1, contactId);
		                ps.setString(2, addresses[i].getStreetAddress());
		                ps.setInt(3, addresses[i].getCity().getId());
		            }
		
		            public int getBatchSize() {
		                return addresses.length;
		            }
        		});
	}
	
}
