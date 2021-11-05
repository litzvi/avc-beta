/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessDAO;
import com.avc.mis.beta.dto.plan.ProcessItemPlanDTO;
import com.avc.mis.beta.dto.plan.ProductionPlanDTO;
import com.avc.mis.beta.dto.plan.ProductionPlanRowDTO;
import com.avc.mis.beta.dto.plan.UsedItemPlanDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.plan.ProductionPlan;
import com.avc.mis.beta.repositories.ProductionPlanRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * 
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ProductionPlans {
	
	@Autowired private ProcessDAO dao;

	@Autowired private DeletableDAO deletableDAO;
	
	@Autowired private ProductionPlanRepository productionPlanRepository;

	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public Integer addProductionPlan(ProductionPlanDTO productionPlan) {
		productionPlan.setProcessName(ProcessName.PRODUCTION_PLAN);
		return dao.addGeneralProcessEntity(productionPlan, ProductionPlan::new);
//		ProductionPlan productionPlan = productionPlanDTO.fillEntity(new ProductionPlan());
//		productionPlan.setProcessType(dao.getProcessTypeByValue(ProcessName.PRODUCTION_PLAN));
//		dao.addEntity(productionPlan);
//		return productionPlan.getId();
	}
	
	public ProductionPlanDTO getProductionPlan(int processId) {
		ProductionPlanDTO productionPlanDTO = new ProductionPlanDTO();
		productionPlanDTO.setGeneralProcessInfo(getProductionPlanRepository()
				.findGeneralProcessInfoByProcessId(processId, ProductionPlan.class)
				.orElseThrow(
						()->new IllegalArgumentException("No production plan with given process id")));
//		productionPlanDTO.setProductionPlanInfo(getProductionPlanRepository().findProductionPlanInfo(processId));
		
		//TODO fill rows
		List<ProductionPlanRowDTO> productionPlanRows = getProductionPlanRepository().findProductionPlanRows(productionPlanDTO.getId());
		int[] rowsIds = productionPlanRows.stream().mapToInt(ProductionPlanRowDTO::getId).toArray();
		
		Map<Integer, List<ProcessItemPlanDTO>> processItemPlansMap = 
				getProductionPlanRepository().findProcessItemPlans(rowsIds)
				.collect(Collectors.groupingBy(ProcessItemPlanDTO::getPlanId, LinkedHashMap::new, Collectors.toList()));
		Map<Integer, List<UsedItemPlanDTO>> usedItemPlansMap = 
				getProductionPlanRepository().findUsedItemPlans(rowsIds)
				.collect(Collectors.groupingBy(UsedItemPlanDTO::getPlanId, LinkedHashMap::new, Collectors.toList()));
		
		for(ProductionPlanRowDTO i: productionPlanRows) {
			i.setProcessItemPlans(processItemPlansMap.get(i.getId()));
			i.setUsedItemPlans(usedItemPlansMap.get(i.getId()));
		}
		
		
		productionPlanDTO.setProductionPlanRows(productionPlanRows);
		
		return productionPlanDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editProductionPlan(ProductionPlanDTO productionPlan) {
		dao.editGeneralProcessEntity(productionPlan, ProductionPlan::new);
//		ProductionPlan productionPlan = productionPlanDTO.fillEntity(new ProductionPlan());
//		dao.editEntity(productionPlan);
//		return productionPlan.getId();
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void removeProductionPlan(Integer productionPlanId) {
		deletableDAO.permenentlyRemoveEntity(ProductionPlan.class, productionPlanId);
	}
		

}
