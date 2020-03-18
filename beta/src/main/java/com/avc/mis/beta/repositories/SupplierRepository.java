/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.SupplierBasic;
import com.avc.mis.beta.entities.data.Supplier;

/**
 * @author Zvi
 *
 */
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
	
	@Query("select new com.avc.mis.beta.dto.SupplierBasic(s.id, s.name) "
			+ "from Supplier s "
			+ "join s.supplyCategories c "
			+ "where c.id = :categoryId ")
	List<SupplierBasic> findSuppliersByCategoryBasic(Integer categoryId);
}
