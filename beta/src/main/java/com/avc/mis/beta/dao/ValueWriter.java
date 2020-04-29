/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.values.*;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(rollbackFor = Throwable.class)
public class ValueWriter extends DAO {
	
	public void edit(ValueEntity entity) {
		super.editEntity(entity);
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
