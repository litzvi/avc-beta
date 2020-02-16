/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="PHONES")
public class Phone {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="contactId", nullable = false)
	private int contactId;
	
	@ManyToOne @JoinColumn(name = "contactId", updatable=false, insertable=false)
	private ContactDetails contactDetails;
	
	@Column(name = "phone", nullable = false)
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
