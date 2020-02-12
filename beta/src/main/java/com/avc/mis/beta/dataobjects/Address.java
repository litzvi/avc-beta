/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Address address: addresses) {
			if(address.getStreetAddress() != null) {
				batchArgs.add(new Object[] {contactId, address.getStreetAddress(), address.getCity().getId()});
			}
		}
		String sql = "insert into addresses (contactId, streetAddress, cityId) values (?, ?, ?)";
		jdbcTemplateObject.batchUpdate(sql, batchArgs, new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
		
	
	}
	
}
