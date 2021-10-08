/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.basic.ProductionLineBasic;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.dto.values.CashewItemDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

/**
 * Spring repository for accessing lists of {@link ValueEntity} entities, 
 * that usually serve as constants referenced by user recorded data.
 * 
 * @author Zvi
 *
 */
public interface ValueTablesRepository extends BaseRepository<ValueEntity> {
	
	@Query("select new com.avc.mis.beta.dto.values.ItemDTO("
				+ "i.id, i.value, i.code, i.brand, "
				+ "i.measureUnit, i.itemGroup, i.productionUse, "
				+ "u, type(i)) "
			+ "from Item i "
				+ "join i.unit u "
			+ "where (i.itemGroup = :itemGroup or :itemGroup is null) "
				+ "and (:checkProductionUses = false or i.productionUse in :productionUses) "
//				+ "and (type(i) in :classes) "
				+ "and (:packageTypeOrdinal = "
					+ "(CASE "
						+ "WHEN u.measureUnit = com.avc.mis.beta.entities.enums.MeasureUnit.NONE "
							+ "THEN 0 "
						+ "ELSE 1 "
					+ "END) "
					+ "or :packageTypeOrdinal is null) "
			+ "order by i.productionUse, i.code, i.value ")
	List<ItemDTO> findItemsByGroupBasic(ItemGroup itemGroup, 
			boolean checkProductionUses, ProductionUse[] productionUses, 
			Integer packageTypeOrdinal);

	@Query("select new com.avc.mis.beta.dto.values.CashewItemDTO("
			+ "i.id, i.value, i.code, i.brand, i.measureUnit, i.itemGroup, i.productionUse, "
			+ "i.unit, type(i), "
			+ "i.numBags, "
			+ "grade.id, grade.value,  "
			+ "i.whole, i.roast, i.toffee, i.saltLevel) "
		+ "from CashewItem i "
			+ "join i.unit u "
			+ "left join i.grade grade "
		+ "where (i.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and (i.productionUse = :productionUse or :productionUse is null) "
			+ "and (grade.id = :gradeId or :gradeId is null) "
			+ "and (:packageTypeOrdinal = "
				+ "(CASE "
					+ "WHEN u.measureUnit = com.avc.mis.beta.entities.enums.MeasureUnit.NONE "
						+ "THEN 0 "
					+ "ELSE 1 "
				+ "END) "
				+ "or :packageTypeOrdinal is null) "
//			+ "and i.numBags >= :minBagsInBox "
		+ "order by i.productionUse, i.brand, i.code, i.value ")
	List<CashewItemDTO> findCashewItems(ItemGroup itemGroup, ProductionUse productionUse, Integer gradeId, Integer packageTypeOrdinal);

	@Query("select new com.avc.mis.beta.dto.reference.BasicValueEntity(i.id, i.value) "
			+ "from Item i "
			+ "where (i.itemGroup = :itemGroup or :itemGroup is null)"
				+ "and (i.productionUse = :productionUse or :productionUse is null)"
				+ "and i.active = true "
			+ "order by i.productionUse, i.code, i.value ")
	List<BasicValueEntity<Item>> findBasicItems(ItemGroup itemGroup, ProductionUse productionUse);

	@Query("select new com.avc.mis.beta.dto.basic.ProductionLineBasic(t.id, t.value, t.productionFunctionality) "
			+ "from ProductionLine t "
			+ "where t.active = true "
				+ "and t.productionFunctionality in :functionalities "
			+ "order by t.value ")
	List<ProductionLineBasic> findBasicProductionLines(ProductionFunctionality[] functionalities);

	@Query("select new com.avc.mis.beta.dto.values.ItemWithUnitDTO("
			+ "i.id, i.value, i.measureUnit, i.itemGroup, i.productionUse, "
			+ "u, type(i)) "
		+ "from StorageBase s "
			+ "join s.processItem pi "
				+ "join pi.item i "
					+ "join i.unit u "
		+ "where s.id in :storageIds "
		+ "group by i ")
	List<ItemWithUnitDTO> findStoragesItems(Set<Integer> storageIds);

	@Query("select new com.avc.mis.beta.dto.values.ItemDTO("
			+ "i.id, i.value, i.code, i.brand, "
			+ "i.measureUnit, i.itemGroup, i.productionUse, "
			+ "u, type(i)) "
		+ "from BillOfMaterials bom "
			+ "join bom.product product "
			+ "join bom.bomList bom_line "
				+ "join bom_line.material i "
					+ "join i.unit u "
		+ "where product = product "
			+ "and (i.itemGroup = :itemGroup or :itemGroup is null)"
			+ "and (i.productionUse = :productionUse or :productionUse is null)"
			+ "and i.active = true "
		+ "order by i.productionUse, i.code, i.value ")
	List<ItemDTO> findProductBillOfMaterials(Integer product, ItemGroup itemGroup, ProductionUse productionUse);
		
}
