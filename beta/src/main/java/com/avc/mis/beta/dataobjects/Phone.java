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

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Phone phone: phones) {
			if(phone.getName() != null) {
				batchArgs.add(new Object[] {contactId, phone.getName()});
			}
		}
		String sql = "insert into phones (contactId, phone) values (?, ?)";
		jdbcTemplateObject.batchUpdate(sql, batchArgs, new int[]{Types.INTEGER, Types.VARCHAR});
		
		
	}

	
	
	
}
