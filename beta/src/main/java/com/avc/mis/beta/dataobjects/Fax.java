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

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Fax fax: faxes) {
			if(fax.getName() != null) {
				batchArgs.add(new Object[] {contactId, fax.getName()});
			}
		}
		String sql = "insert into faxes (contactId, fax) values (?, ?)";
		jdbcTemplateObject.batchUpdate(sql, batchArgs, new int[]{Types.INTEGER, Types.VARCHAR});
	}
	
}
