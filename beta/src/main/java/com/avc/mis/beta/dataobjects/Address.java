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
@Table(name="ADDRESSES")
public class Address {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="contactId", nullable = false)
	private int contactId;
	
	@ManyToOne @JoinColumn(name = "contactId", updatable=false, insertable=false)
	private ContactDetails contactDetails;
	
	@Column(nullable = false)
	private String streetAddress;
	
	@ManyToOne @JoinColumn(name="cityId", nullable = false)
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
