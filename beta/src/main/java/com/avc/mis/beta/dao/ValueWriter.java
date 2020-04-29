/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.values.*;

/**
 * Used for adding (inserting, persisting) Value entities - {@link com.avc.mis.beta.entities.ValueEntity}.
 * System Manager is usually the only one adding, removing or editing value entities.
 * Value entities should be first in restore order before other types of Entities, 
 * addX methods should be ordered by restore order.
 * 
 * @author Zvi
 *
 */
@Repository
@Transactional(rollbackFor = Throwable.class)
public class ValueWriter extends SoftDeletableDAO {
	
	/**
	 * Edit the existing entity to it's new set values. 
	 * Only edits editable values, ignores if non editable values are changed.
	 * @param entity ValueEntity with all values set for the state after edit.
	 * @throws IllegalArgumentException if entity's id isn't set or new data isn't legal.
	 */
	public void edit(ValueEntity entity) {
		super.editEntity(entity);
	}
	
	/**
	 * Sets the entity as not active, dosen't permanently remove from database.
	 * @param entity the ValueEntity to be removed - CAUTION if any other editable field is changed it will be edited.
	 */
	public void remove(ValueEntity entity) {
		removeEntity(entity);
	}

	public void addCountry(Country country) {
		addEntity(country);
	}
	
	public void addCity(City city) {		
		addEntity(city, city.getCountry());
	}
	
	public void addBank(Bank bank) {
		addEntity(bank);
	}
	
	public void addBankBranch(BankBranch branch) {
		addEntity(branch, branch.getBank());
	}
	
	public void addSupplyCategory(SupplyCategory category) {
		addEntity(category);
	}
	
	public void addCompanyPosition(CompanyPosition position) {
		addEntity(position);
	}
	
	public void addItem(Item item) {
		addEntity(item);
	}
	
	public void addContractType(ContractType type) {
		addEntity(type);
	}
	
	public void addProcessStatus(ProcessStatus status) {
		addEntity(status);
	}
	
	public void addProcessType(ProcessType type) {
		addEntity(type);
	}
	
	public void addProductionLine(ProductionLine line) {
		addEntity(line);
	}
}
