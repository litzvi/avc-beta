/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.item.BillOfMaterialsDTO;
import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.item.BillOfMaterials;
import com.avc.mis.beta.entities.settings.UOM;

/**
 * @author Zvi
 *
 */
public interface BillOfMaterialsRepository extends BaseRepository<LinkEntity> {

	@Query("select bom "
		+ "from BillOfMaterials bom "
			+ "join fetch bom.bomList "
		+ "where bom.product.id = :productId ")
	BillOfMaterials findBillOfMaterialsByProduct(int productId);
	
	

}
