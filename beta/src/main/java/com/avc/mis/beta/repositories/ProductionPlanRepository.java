/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.collectionItems.ProcessItemPlanDTO;
import com.avc.mis.beta.dto.process.collectionItems.UsedItemPlanDTO;
import com.avc.mis.beta.dto.process.group.ProductionPlanRowDTO;
import com.avc.mis.beta.entities.process.ProductionPlan;

/**
 * @author zvi
 *
 */
public interface ProductionPlanRepository extends ProcessRepository<ProductionPlan> {

	@Query("select new com.avc.mis.beta.dto.process.group.ProductionPlanRowDTO ( "
			+ "r.id, r.version, r_pt.id, r_pt.value,"
			+ "pl.id, pl.value, "
			+ "r.shift, r.numOfWorkers, r.plannedDate)"
		+ "from ProductionPlan p "
			+ "join p.productionPlanRows r "
				+ "join r.processType r_pt "
				+ "left join r.productionLine pl "
		+ "where p.id = :processId ")
	List<ProductionPlanRowDTO> findProductionPlanRows(Integer processId);

	@Query("select new com.avc.mis.beta.dto.process.collectionItems.ProcessItemPlanDTO ( "
			+ "pip.id, pip.version, pip.ordinal, pr.id, "
			+ "i.id, i.value, pip.numberUnits)"
		+ "from ProductionPlanRow pr "
			+ "join pr.processItemPlans pip "
			+ "join pip.item i "
		+ "where pr.id in :rowsIds "
		+ "order by pr.id, pip.ordinal ")
	Stream<ProcessItemPlanDTO> findProcessItemPlans(int[] rowsIds);

	@Query("select new com.avc.mis.beta.dto.process.collectionItems.UsedItemPlanDTO ( "
			+ "uip.id, uip.version, uip.ordinal, pr.id, "
			+ "pi.id, pi.version, uip.numberUnits)"
		+ "from ProductionPlanRow pr "
			+ "join pr.usedItemPlans uip "
			+ "join uip.processItem pi "
		+ "where pr.id in :rowsIds "
		+ "order by pr.id, uip.ordinal ")
	Stream<UsedItemPlanDTO> findUsedItemPlans(int[] rowsIds);

	

}
