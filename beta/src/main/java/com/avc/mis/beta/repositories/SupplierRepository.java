/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.generic.ValueObject;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.ItemAmountWithPo;
import com.avc.mis.beta.dto.view.SupplierRow;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.service.report.row.SupplierQualityRow;

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
	List<DataObjectWithName<Supplier>> findSuppliersByCategoryBasic(Integer categoryId);
	
	@Query("select distinct new com.avc.mis.beta.dto.data.DataObjectWithName(s.id, s.version, s.name) "
			+ "from Supplier s "
			+ "left join s.supplyCategories c "
			+ "where c.supplyGroup = :supplyGroup "
				+ "and s.active = true "
			+ "ORDER BY s.name")
	List<DataObjectWithName<Supplier>> findSuppliersByGroupBasic(SupplyGroup supplyGroup);
		
	@Query("select new com.avc.mis.beta.dto.data.DataObjectWithName(s.id, s.version, s.name) "
			+ "from Supplier s "
			+ "where s.active = true")
	List<DataObjectWithName<Supplier>> findAllSuppliersBasic();
	
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
	
	@Query("select new com.avc.mis.beta.dto.generic.ValueObject(cd.id, p.value) "
			+ "from Phone p "
				+ "join p.contactDetails cd ")
	Stream<ValueObject<String>> findAllPhoneValues();
	
	@Query("select new com.avc.mis.beta.dto.generic.ValueObject(cd.id, e.value) "
			+ "from Email e "
				+ "join e.contactDetails cd ")
	Stream<ValueObject<String>> findAllEmailValues();

	@Query("select new com.avc.mis.beta.dto.generic.ValueObject(s.id, c.value) "
			+ "from Supplier s "
				+ "join s.supplyCategories c ")
	Stream<ValueObject<String>> findAllSupplyCategoryValues();

	@Query("select new com.avc.mis.beta.service.report.row.SupplierQualityRow( "
			+ "po_code.id, "
			+ "concat(t.code, '-', po_code.code, coalesce(t.suffix, '')), "
			+ "s.name, "
			+ "item.value,  r.recordedTime, "
			+ "SUM(sf.unitAmount * sf.numberUnits * uom.multiplicand / uom.divisor), item.measureUnit ) "
		+ "from Receipt r "
			+ "join r.lifeCycle lc "
			+ "join r.poCode po_code "
				+ "join po_code.supplier s "
				+ "join po_code.contractType t "
			+ "join r.processItems pi "
				+ "join pi.item item "
					+ "join item.unit item_unit "
				+ "join pi.storageForms sf "
			+ "join UOM uom "
				+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
			+ "join r.processType pt "
		+ "where pt.processName = :processName "
			+ "and lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (s.id = :supplierId or :supplierId is null)"
			+ "and (:startTime is null or r.recordedTime >= :startTime) "
			+ "and (:endTime is null or r.recordedTime < :endTime) "
		+ "group by po_code "
		+ "order by s, r.recordedTime ")
	List<SupplierQualityRow> findSupplierWithPos(ProcessName processName, Integer supplierId, LocalDateTime startTime, LocalDateTime endTime);


}
