/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.data.PhoneDTO;
import com.avc.mis.beta.entities.data.Phone;

/**
 * @author Zvi
 *
 */
public interface PhoneRepository extends ContactEntityRepository<Phone> {
	
//	@Query("select p from Phone p "
//			+ "where p.contactDetails = :contactId "
//				+ "and p.deleted = false")
//	List<Phone> findAllByContactDetailsId(int contactId);
	
//	@Query("select new com.avc.mis.beta.dto.data.PhoneDTO(p.id, p.version, p.value) "
//			+ "from Phone p "
//			+ "where p.contactDetails.id = :contactId "
//				+ "and p.deleted = false")
//	List<PhoneDTO> findAllByContactDetailsId(int contactId);
	
	@Query("select p from Phone p "
			+ "where p.deleted = false")
	List<Phone> findAll();
}
