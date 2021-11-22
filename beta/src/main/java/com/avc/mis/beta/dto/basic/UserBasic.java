/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicDataValueDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Shows partial information of a user.
 * Used for reference and display.
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class UserBasic extends BasicDataValueDTO {
	
	@JsonIgnore
	@ToString.Exclude
	String username;
		
	public UserBasic(Integer id, Integer version, String username) {
		super(id, version);
		this.username = username;
	}
	
	@ToString.Include(name = "value")
	@Override
	public String getValue() {
		return getUsername();
	}
}
