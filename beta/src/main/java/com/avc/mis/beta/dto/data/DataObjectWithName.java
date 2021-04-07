/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.entities.ObjectEntityWithName;
import com.avc.mis.beta.entities.ObjectWithNameInterface;
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
public class DataObjectWithName<T extends ObjectWithNameInterface> extends DataObject<T> {
	
	@JsonIgnore
	@ToString.Exclude
	String name;
	
	public DataObjectWithName(Integer id, Integer version, String name) {
		super(id, version);
		this.name = name;
	}
	
	public DataObjectWithName(@NonNull ObjectEntityWithName entity) {
		super(entity.getId(), entity.getVersion());
		this.name = entity.getName();
	}
	
	@ToString.Include(name = "value")
	public String getValue() {
		return getName();
	}
}
