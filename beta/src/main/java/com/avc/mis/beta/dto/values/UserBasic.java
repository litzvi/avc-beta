/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.data.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UserBasic extends DataDTO {
	
	@JsonIgnore
	@ToString.Exclude
	String username;
		
	public UserBasic(Integer id, Integer version, String username) {
		super(id, version);
		this.username = username;
	}
	
	public UserBasic(@NonNull UserEntity user) {
		super(user.getId(), user.getVersion());
		this.username = user.getUsername();
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return getUsername();
	}
}
