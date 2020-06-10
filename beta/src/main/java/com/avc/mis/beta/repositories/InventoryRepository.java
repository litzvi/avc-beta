/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.values.PoInventoryRow;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.process.PoCode;

/**
 * @author Zvi
 *
 */
public interface InventoryRepository extends BaseRepository<PoCode> {

//	@Query("select com.avc.mis.beta.dto.values.PoInventoryRow() "
//			+ "from ReceiptItem i "
//			+ "join i.storageForms s ")
//	List<PoInventoryRow> findInventoryTable(SupplyGroup cashew);

}
