/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO's represent the information of the entities, to be presented and used.
 * The reasons for using DTOs rather than sending the entities themselves are:
 * 1. Avoid sending unnecessary data of the entity and adjust the way to present it. 
 * 		e.g. avoid list that aren't needed, present Enums as Strings etc.
 * 2. Structured way to fetch data needed, when it is lazily loaded by the persistence provider.
 * 		e.g. list of phones lazily loaded is fetched while building the DTO (see 5).
 * 3. Can be used for comparing entities while testing, without affecting entity class behavior.
 * 		e.g. ignore id value for comparing inserted entity to actual entity from DB.
 * 4. Tune the queries to fetch only the necessary data with select constructors ('select new DTO())'.
 * 		e.g. only basic supplier info needed when getting a purchase order.
 * 5. Control join fetch all cross-table data efficiently with 'select new DTO()'.
 *
 * Entity DTO base class which all entity DTO's inherit from. contains an ID.
 * 
 * @author Zvi
 *
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class BaseEntityDTO {
	
	@EqualsAndHashCode.Include
	private Integer id;

	/**
	 * When comparing two DTO objects of the same class: 
	 * If they both have an ID, compares the id value with equals otherwise returns false.
	 * Needed for testing; so when comparing a non persistent entity's dto (that dosen't have an id),
	 * with the persisted dto (which contains an id) they could still be equal.
	 * When both objects id's are null - both represent a non persistent entity - then returns true,
	 * comparing the rest of the objects regularly.
	 */
	@Override 
	public boolean equals(Object o) {
	    if (o == this) return true;
	    if (!(o instanceof BaseEntityDTO)) return false;
	    BaseEntityDTO other = (BaseEntityDTO) o;
	    
	    //Exclusively one is null so they are equal (objects with this id can equal)
	    if(this.getId() == null ^ other.getId() == null) 
	    	return true;
	    
	    if(this.getId() != null) { //both not null compare with id
	    	return this.getId().equals(other.getId());
	    }
	    else { //both are null
	    	return true;
	    }
	}
	  
	/**
	 * Hash code can't depend on the id,
	 * because any Object who's id is set, 
	 * won't be equal to an Object with null value.
	 * which isn't true for comparing a new entity with the persisted one for testing.
	 */
	@Override 
	public int hashCode() {
	    final int PRIME = 59;
		return PRIME;
	}


}
