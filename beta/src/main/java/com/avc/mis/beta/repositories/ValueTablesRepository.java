/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.ValueEntity;
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

//	@Query("select new com.avc.mis.beta.dto.values.ItemDTO(i.id, i.value, i.defaultMeasureUnit, i.itemGroup, i.productionUse) "
//			+ "from Item i "
//			+ "where i.itemGroup = :itemGroup "
//				+ "and i.active = true "
//			+ "order by i.value ")
//	List<ItemDTO> findItemsByGroupBasic(ItemGroup itemGroup);
	
	@Query("select new com.avc.mis.beta.dto.values.ItemWithUnitDTO("
				+ "i.id, i.value, i.measureUnit, i.itemGroup, i.productionUse, "
				+ "u.amount, u.measureUnit, type(i)) "
			+ "from Item i "
				+ "join i.unit u "
			+ "where (i.itemGroup = :itemGroup or :itemGroup is null) "
				+ "and (i.productionUse = :productionUse or :productionUse is null) "
				+ "and (type(i) in :classes) "
			+ "order by i.value ")
	List<ItemWithUnitDTO> findItemsByGroupBasic(ItemGroup itemGroup, ProductionUse productionUse, List<Class<? extends Item>> classes);

	@Query("select new com.avc.mis.beta.dto.values.BasicValueEntity(i.id, i.value) "
			+ "from Item i "
			+ "where (i.itemGroup = :itemGroup or :itemGroup is null)"
				+ "and (i.productionUse = :productionUse or :productionUse is null)"
				+ "and i.active = true "
			+ "order by i.value ")
	List<BasicValueEntity<Item>> findBasicItems(ItemGroup itemGroup, ProductionUse productionUse);

//	@Query("select new com.avc.mis.beta.dto.values.BasicValueEntity(i.id, i.value) "
//			+ "from Item i "
//			+ "where i.productionUse = :productionUse "
//				+ "and i.active = true "
//			+ "order by i.value ")
//	List<BasicValueEntity<Item>> findItemsByProductionUse(ProductionUse productionUse);
//
//	@Query("select new com.avc.mis.beta.dto.values.BasicValueEntity(i.id, i.value) "
//			+ "from Item i "
//			+ "where i.itemGroup = :itemGroup "
//				+ "and i.active = true "
//			+ "order by i.value ")
//	List<BasicValueEntity<Item>> findItemsByGroup(ItemGroup itemGroup);
	
	
}
