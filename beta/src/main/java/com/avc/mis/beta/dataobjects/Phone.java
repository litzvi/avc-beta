/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "PHONES")
public class Phone {

	@Id @GeneratedValue
	private int id;

//	@Column(name="contactId", insertable = false, updatable = false)
//	private Integer contactId;

	@ToString.Exclude @EqualsAndHashCode.Exclude
	@JsonBackReference(value = "contactDetails_phones")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactId", updatable = false)
	private ContactDetails contactDetails;

	@Column(name = "phone", nullable = false)
	private String name;

	/**
	 * @param jdbcTemplateObject
	 * @param contactId
	 * @param phones
	 *//*
		 * public static void insertPhones(JdbcTemplate jdbcTemplateObject, int
		 * contactId, Phone[] phones) {
		 * 
		 * List<Object[]> batchArgs = new ArrayList<Object[]>(); for(Phone phone:
		 * phones) { if(phone.getName() != null) { batchArgs.add(new Object[]
		 * {contactId, phone.getName()}); } } String sql =
		 * "insert into phones (contactId, phone) values (?, ?)";
		 * jdbcTemplateObject.batchUpdate(sql, batchArgs, new int[]{Types.INTEGER,
		 * Types.VARCHAR});
		 * 
		 * 
		 * }
		 */

}
