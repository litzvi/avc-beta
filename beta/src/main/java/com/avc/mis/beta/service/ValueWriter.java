/**
 * 
 */
package com.avc.mis.beta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.SoftDeletableDAO;
import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.PackedItem;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.CashewStandard;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.entities.values.Warehouse;

/**
 * Used for adding (inserting, persisting) Value entities - {@link com.avc.mis.beta.entities.ValueEntity}.
 * System Manager is usually the only one adding, removing or editing value entities.
 * Value entities should be first in restore order before other types of Entities, 
 * addX methods should be ordered by restore order.
 * 
 * @author Zvi
 *
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class ValueWriter {
	
	@Autowired private SoftDeletableDAO dao;
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
	
	/**
	 * Edit the existing entity to it's new set values. 
	 * Only edits editable values, ignores if non editable values are changed.
	 * @param entity ValueEntity with all values set for the state after edit.
	 * @throws IllegalArgumentException if entity's id isn't set or new data isn't legal.
	 */
	public <T extends ValueEntity> T edit(T entity) {
		return dao.editEntity(entity);
	}
	
	/**
	 * For testing only, needed because calling DAO directly has no transaction
	 * @param entity to be permanently removed.
	 */
	@Deprecated
	public <T extends ValueEntity> void permenentlyRemoveEntity(T entity) {
		deletableDAO.permenentlyRemoveEntity(entity);
	}
	
	/**
	 * Sets the entity as not active, dosen't permanently remove from database.
	 * @param <T>
	 * @param entityClass
	 * @param entityId
	 */
	public <T extends ValueEntity> void remove(Class<T> entityClass, int entityId) {
		//check if it's one of the permitted classes
		dao.removeEntity(entityClass, entityId);
	}

	public void addCountry(Country country) {
		dao.addEntity(country);
	}
	
	public void addCity(City city) {		
		dao.addEntity(city, city.getCountry());
	}
	
	public void addBank(Bank bank) {
		dao.addEntity(bank);
	}
	
	public void addBankBranch(BankBranch branch) {
		dao.addEntity(branch, branch.getBank());
	}
	
	public void addSupplyCategory(SupplyCategory category) {
		dao.addEntity(category);
	}
	
	public void addCompanyPosition(CompanyPosition position) {
		dao.addEntity(position);
	}
	
	//item should practically be abstarct
//	public void addItem(Item item) {
//		dao.addEntity(item);
//	}
	
	public void addPackedItem(PackedItem item) {
		dao.addEntity(item);
	}
	
	public void addBulkItem(BulkItem item) {
		dao.addEntity(item);
	}
	
	public void addContractType(ContractType type) {
		dao.addEntity(type);
	}
	
//	public void addProcessStatus(ProcessStatus status) {
//		dao.addEntity(status);
//	}
	
//	public void addProcessType(ProcessType type) {
//		dao.addEntity(type);
//	}
	
	public void addProductionLine(ProductionLine line) {
		dao.addEntity(line);
	}
	
	public void addWarehouse(Warehouse warehouse) {
		dao.addEntity(warehouse);
	}
	
	public void addCashewStandard(CashewStandard standard) {
		dao.addEntity(standard);
	}
	
	public void addShippingPort(ShippingPort port) {
		dao.addEntity(port);
	}
}
