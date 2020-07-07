/**
 * 
 */
package com.avc.mis.beta.entities;

import java.util.Arrays;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Main Interface defining required characteristics for all entities that can be inserted to the database.
 * Has to have an Id, method to check legality and setting the required foreign key reference to the non owning side.
 * 
 * @author Zvi
 *
 */
public interface Insertable {
	
	public Integer getId();
	
	//used for setting id to null when restoring, so dosen't check if record exists for old id.
	public void setId(Integer id);
	
//	/**
//	 * Checks if object has the required not null-able data.
//	 * Dosen't check if required references - foreign keys for database are set.
//	 * @return true if all required data is set, false otherwise.
//	 */
//	public boolean isLegal();
//	
//	public String getIllegalMessage();
//	
//	public void prePersist();
//	public void preUpdate();

	/**
	 * Empty implementation
	 */
	default public void setReference(Object referenced) {}

	
	/**
	 * Filters out the illegal entities and applies the setReference operator for every element.
	 * @param <T> 
	 * @param tArray array of entities
	 * @param p operator that sets the reference of T with the owning entity.
	 * @return Set of entities that are legal to insert/update with their reference set.
	 */
//	static <T extends Insertable> Set<T> filterAndSetReference(T[] tArray, UnaryOperator<T> p) {
//		return Arrays.stream(tArray)
//			.filter(t -> t != null && t.isLegal())
//			.map(t -> p.apply(t))
//			.collect(Collectors.toSet());
//	}
	
	/**
	 * Applies the setReference operator for every element.
	 * @param <T> 
	 * @param tArray array of entities
	 * @param p operator that sets the reference of T with the owning entity.
	 * @return Set of entities to insert/update with their reference set.
	 */
	static <T extends Insertable> Set<T> setReferences(T[] tArray, UnaryOperator<T> p) {
		return Arrays.stream(tArray)
			.filter(t -> t != null)
			.map(t -> p.apply(t))
			.collect(Collectors.toSet());
	}
	
	/**
	 * Checks If Id's for both elements aren't set. If so, they can't equal.
	 * Used in order to tune Lombok's equals function to consider transient entities, 
	 * by redefining canEqual method for Lombok.@Data entities.
	 * @param <T>
	 * @param t
	 * @param o
	 * @return false if o isn't an instance of T, or Id's aren't set for both entities.
	 */
	static <T extends Insertable> boolean canEqualCheckNullId(T t, Object o) {		
		if(t.getClass().isInstance(o)) {
			Insertable other = (Insertable) o;
			return !(t.getId() == null && other.getId() == null);
		}
		return false;
		
	}
	
}
