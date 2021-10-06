/**
 * 
 */
package com.avc.mis.beta.repositories;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.item.BillOfMaterials;

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
