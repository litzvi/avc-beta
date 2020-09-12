/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class for encapsulating the id of a DTO that represents an entity.
 * DTOs that are both representing persisted entities - were already assigned an id,
 * or both non persisted - ids are null - will compare normally.
 * While comparing one dto with id=null and the other with a value,
 * should ignore comparing ids, so tests that compare a newly built entity
 * will compare correctly with the dto received for the same entity after persisted.
 * 
 * @author zvi
 *
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoId {

	private Integer value;
	
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
	    if (!(o instanceof DtoId)) return false;
	    DtoId other = (DtoId) o;
	    
	    //Exclusively one is null so they are equal (objects with this id can equal)
	    if(this.getValue() == null ^ other.getValue() == null) 
	    	return true;
	    
	    if(this.getValue() != null) { //both not null compare with id
	    	return this.getValue().equals(other.getValue());
	    }
	    else { //both are null
	    	return true;
	    }
	}
	  
	/**
	 * Hash code can't depend on the field,
	 * because any Object who's value is set, 
	 * will be equal to an Object with null value.
	 */
	@Override 
	public int hashCode() {
	    final int PRIME = 59;
		return PRIME;
	}
	  
	  
}
