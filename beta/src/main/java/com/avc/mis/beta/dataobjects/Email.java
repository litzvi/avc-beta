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
@Table(name="EMAILS")
public class Email {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="contactId", nullable = false)
	private int contactId;
	
	@ManyToOne @JoinColumn(name = "contactId", updatable=false, insertable=false)
	private ContactDetails contactDetails;
	
	@Column(name = "email", nullable = false)
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
