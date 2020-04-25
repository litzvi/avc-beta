/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.Supplier;

/**
 * Spring repository for accessing information of company suppliers.
 * 
 * @author Zvi
 *
 */
public interface SupplierRepository extends BaseRepository<Supplier> {
	
	@Query("select new com.avc.mis.beta.dto.values.SupplierBasic(s.id, s.version, s.name) "
			+ "from Supplier s "
			+ "join s.supplyCategories c "
			+ "where c.id = :categoryId "
				+ "and s.active = true")
	List<SupplierBasic> findSuppliersByCategoryBasic(Integer categoryId);
	
	@Query("select new com.avc.mis.beta.dto.values.SupplierBasic(s.id, s.version, s.name) "
			+ "from Supplier s "
			+ "where s.active = true")
	List<SupplierBasic> findAllSuppliersBasic();
	
	@Query("select s from Supplier s "
			+ "left join fetch s.contactDetails cd "
			+ "where s.active = true")
	Stream<Supplier> findAll();
	
	@Query("select s from Supplier s "
			+ "left join fetch s.contactDetails cd "
			+ "where s.id = :id")
	Optional<Supplier> findById(Integer id);
	
	@Query("select cc from CompanyContact cc "
			+ "left join fetch cc.position "
			+ "left join fetch cc.person p "
				+ "left join fetch p.idCard id "
				+ "left join fetch p.contactDetails cd "
			+ "where cc.company.id = :id "
				+ "and cc.active = true")
	List<CompanyContact> findCompanyContactsByCompnyId(Integer id);
	
}
