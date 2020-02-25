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

import io.micrometer.core.instrument.util.StringUtils;
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
@Table(name="ADDRESSES")
public class Address {

	@Id @GeneratedValue
	private Integer id;
	
//	@Column(name="contactId", nullable = false)
//	private int contactId;
	
	@ToString.Exclude @EqualsAndHashCode.Exclude
	@JsonBackReference(value = "contactDetails_addresses")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactId", updatable=false)
	private ContactDetails contactDetails;
	
	@Column(nullable = false)
	private String streetAddress;
	
	@ManyToOne @JoinColumn(name="cityId")
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

	/**
	 * @return
	 */
	@JsonIgnore
	public boolean isLegal() {
		return StringUtils.isNotBlank(getStreetAddress());
	}
	
}
