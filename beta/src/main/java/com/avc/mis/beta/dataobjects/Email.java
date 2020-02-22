/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="EMAILS")
public class Email {

	@Id @GeneratedValue
	private Integer id;
	
//	@Column(name="contactId", nullable = false)
//	private int contactId;
	
	@ToString.Exclude @EqualsAndHashCode.Exclude
	@JsonBackReference(value = "contactDetails_emails")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactId", updatable = false)
	private ContactDetails contactDetails;

	@Column(name = "email", nullable = false)
	@NonNull
	private String name;
	

	/**
	 * @return
	 */
	@JsonIgnore
	public boolean isLegal() {
		return getName() != null;
	}
	
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
