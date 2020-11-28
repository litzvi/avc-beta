/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.values.ValueObject;
import com.avc.mis.beta.dto.view.SupplierRow;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.SupplyGroup;

/**
 * Spring repository for accessing information of company suppliers.
 * 
 * @author Zvi
 *
 */
public interface SupplierRepository extends BaseRepository<Supplier> {
	
	@Query("select new com.avc.mis.beta.dto.data.DataObjectWithName(s.id, s.version, s.name) "
			+ "from Supplier s "
			+ "left join s.supplyCategories c "
			+ "where c.id = :categoryId "
				+ "and s.active = true")
	List<DataObjectWithName> findSuppliersByCategoryBasic(Integer categoryId);
	
	@Query("select distinct new com.avc.mis.beta.dto.data.DataObjectWithName(s.id, s.version, s.name) "
			+ "from Supplier s "
			+ "left join s.supplyCategories c "
			+ "where c.supplyGroup = :supplyGroup "
				+ "and s.active = true "
			+ "ORDER BY s.name")
	List<DataObjectWithName> findSuppliersByGroupBasic(SupplyGroup supplyGroup);
		
	@Query("select new com.avc.mis.beta.dto.data.DataObjectWithName(s.id, s.version, s.name) "
			+ "from Supplier s "
			+ "where s.active = true")
	List<DataObjectWithName> findAllSuppliersBasic();
	
//	@Query("select s from Supplier s "
//			+ "left join fetch s.contactDetails cd "
//			+ "where s.active = true "
//			+ "ORDER BY s.name ASC ")
//	Stream<Supplier> findAll();
	
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
	
	@Query("select new com.avc.mis.beta.dto.view.SupplierRow(s.id, s.name, cd.id) "
			+ "from Supplier s "
			+ "left join s.contactDetails cd "
			+ "where s.active = true "
			+ "order by s.name ")
	List<SupplierRow> findAllSupplierRows();
	
	@Query("select new com.avc.mis.beta.dto.values.ValueObject(cd.id, p.value) "
			+ "from Phone p "
				+ "join p.contactDetails cd ")
	Stream<ValueObject<String>> findAllPhoneValues();
	
	@Query("select new com.avc.mis.beta.dto.values.ValueObject(cd.id, e.value) "
			+ "from Email e "
				+ "join e.contactDetails cd ")
	Stream<ValueObject<String>> findAllEmailValues();

	@Query("select new com.avc.mis.beta.dto.values.ValueObject(s.id, c.value) "
			+ "from Supplier s "
				+ "join s.supplyCategories c ")
	Stream<ValueObject<String>> findAllSupplyCategoryValues();
}
