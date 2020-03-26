/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import com.avc.mis.beta.entities.ContactEntity;

/**
 * @author Zvi
 *
 */
@NoRepositoryBean
public interface ContactEntityRepository<T extends ContactEntity> extends BaseRepository<T> {
	
	@Query("select e from #{#entityName} e where e.contactDetails.id = ?1 and e.active = true")
	List<T> findAllByContactDetailsId(int contactId);

}
