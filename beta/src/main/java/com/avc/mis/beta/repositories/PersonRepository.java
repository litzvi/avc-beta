/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.basic.DataObjectWithName;
import com.avc.mis.beta.entities.data.Person;

/**
 * @author Zvi
 *
 */
public interface PersonRepository extends BaseRepository<Person> {

	/**
	 * @return List of all active persons, each with id, version and name.
	 */
	@Query("select new com.avc.mis.beta.dto.basic.DataObjectWithName(p.id, p.version, p.name) "
			+ "from Person p "
			+ "where p.active = true")
	List<DataObjectWithName<Person>> findAllPersonsBasic();
}
