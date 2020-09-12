/**
 * 
 */
package com.avc.mis.beta.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO's represent the information of the entities, to be presented and used.
 * The reasons for using DTOs rather than sending the entities themselves are:
 * 1. Avoid sending unnecessary data of the entity and adjust the way to present it. 
 * 		e.g. avoid list that aren't needed, present Enums as Strings etc.
 * 2. Structured way to fetch data needed, when it is lazily loaded by the persistence provider.
 * 		e.g. list of phones lazily loaded is fetched while building the DTO (see 5).
 * 3. Can be used for comparing entities while testing, without affecting entity class behaviour.
 * 		e.g. ignore id value for comparing inserted entity to actual entity from DB.
 * 4. Tune the queries to fetch only the necessary data with 'select new DTO()s'.
 * 		e.g. only basic supplier info needed when getting a purchase order.
 * 5. Control join fetch all cross-table data efficiently with 'select new DTO()'.
 *
 * Entity DTO base class which all entity DTO's inherit from. contains an ID.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class BaseDTO {
	
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	@EqualsAndHashCode.Include
	private DtoId dtoId = new DtoId();
	
	public BaseDTO(Integer id) {
		super();
		this.dtoId.setValue(id);	
	}

	public Integer getId() {
		return this.dtoId.getValue();
	}

	public void setId(Integer id) {
		this.dtoId.setValue(id);
	}


	


}
