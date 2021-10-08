/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.query.BomProductWithMaterialLine;
import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
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

	
	@Query("select new com.avc.mis.beta.dto.query.BomProductWithMaterialLine( "
			+ "bom.id, prod.id, prod.value, bom.defaultBatch, "
			+ "bom_line.id, bom_line.ordinal, mat.id, mat.value, bom_line.defaultAmount) "
			+ "from BillOfMaterials bom "
				+ "join bom.product prod "
				+ "join bom.bomList bom_line "
					+ "join bom_line.material mat "
			+ "order by prod.code, bom_line.ordinal ")
	List<BomProductWithMaterialLine> findAllBomProductWithMaterialLine();

	
	

}
