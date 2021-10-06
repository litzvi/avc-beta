/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.plan.ProcessItemPlanDTO;
import com.avc.mis.beta.dto.plan.ProductionPlanRowDTO;
import com.avc.mis.beta.dto.plan.UsedItemPlanDTO;
import com.avc.mis.beta.entities.plan.ProductionPlan;

/**
 * @author zvi
 *
 */
public interface ProductionPlanRepository extends ProcessRepository<ProductionPlan> {

//	@Query("select new com.avc.mis.beta.dto.processInfo.ProductionPlanInfo() "
//		+ "from ProductionPlan p "
//		+ "where p.id = :processId ")
//	ProductionPlanInfo findProductionPlanInfo(int processId);

	@Query("select p "
		+ "from ProductionPlan p "
		+ "where p.id = :processId")
	Optional<ProductionPlan> findById(int processId);

	@Query("select new com.avc.mis.beta.dto.plan.ProductionPlanRowDTO ( "
			+ "r.id, r.version, r_pt.id, r_pt.value,"
			+ "pl.id, pl.value, "
			+ "r.shift, r.numOfWorkers, r.plannedDate)"
		+ "from ProductionPlan p "
			+ "join p.productionPlanRows r "
				+ "join r.processType r_pt "
				+ "left join r.productionLine pl "
		+ "where p.id = :processId ")
	List<ProductionPlanRowDTO> findProductionPlanRows(Integer processId);

	@Query("select new com.avc.mis.beta.dto.plan.ProcessItemPlanDTO ( "
			+ "pip.id, pip.version, pip.ordinal, pr.id, "
			+ "i.id, i.value, pip.numberUnits)"
		+ "from ProductionPlanRow pr "
			+ "join pr.processItemPlans pip "
			+ "join pip.item i "
		+ "where pr.id in :rowsIds "
		+ "order by pr.id, pip.ordinal ")
	Stream<ProcessItemPlanDTO> findProcessItemPlans(int[] rowsIds);

	@Query("select new com.avc.mis.beta.dto.plan.UsedItemPlanDTO ( "
			+ "uip.id, uip.version, uip.ordinal, pr.id, "
			+ "pi.id, pi.version, uip.numberUnits)"
		+ "from ProductionPlanRow pr "
			+ "join pr.usedItemPlans uip "
			+ "join uip.processItem pi "
		+ "where pr.id in :rowsIds "
		+ "order by pr.id, uip.ordinal ")
	Stream<UsedItemPlanDTO> findUsedItemPlans(int[] rowsIds);

	

}
