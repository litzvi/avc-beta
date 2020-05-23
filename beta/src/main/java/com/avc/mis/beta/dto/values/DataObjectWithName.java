/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.entities.ObjectEntityWithIdAndName;
import com.avc.mis.beta.entities.data.Person;
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
public class DataObjectWithName extends DataObject {
	
	@JsonIgnore
	@ToString.Exclude
	String name;
	
	public DataObjectWithName(Integer id, Integer version, String name) {
		super(id, version);
		this.name = name;
	}
	
	public DataObjectWithName(@NonNull ObjectEntityWithIdAndName entity) {
		super(entity.getId(), entity.getVersion());
		this.name = entity.getName();
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return getName();
	}
}
