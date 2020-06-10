/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.values.PoInventoryRow;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.repositories.InventoryRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class CashewReports {
	
	@Autowired InventoryRepository inventoryRepository;
	
//	public List<PoInventoryRow> getCashewInventoryTable() {
//		return getInventoryRepository().findInventoryTable(SupplyGroup.CASHEW);
//	}

}
