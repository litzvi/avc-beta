/**
 * 
 */
package com.avc.mis.beta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.SoftDeletableDAO;
import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BankDTO;
import com.avc.mis.beta.dto.values.CashewGradeDTO;
import com.avc.mis.beta.dto.values.CashewItemDTO;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.CompanyPositionDTO;
import com.avc.mis.beta.dto.values.ContractTypeDTO;
import com.avc.mis.beta.dto.values.CountryDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ProductionLineDTO;
import com.avc.mis.beta.dto.values.ShippingPortDTO;
import com.avc.mis.beta.dto.values.SupplyCategoryDTO;
import com.avc.mis.beta.dto.values.WarehouseDTO;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.CashewGrade;
import com.avc.mis.beta.entities.values.CashewItem;
import com.avc.mis.beta.entities.values.CashewStandard;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.entities.values.Item;
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
	public <T extends ValueDTO> void edit(T dto) {
		try {
			dao.editEntity(dto);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException("Internal error while editing");
		}
	}
	
	/**
	 * For testing only, needed because calling DAO directly has no transaction
	 * @param entity to be permanently removed.
	 */
	@Deprecated
	public <T extends ValueDTO> void permenentlyRemoveEntity(T dto) {
		deletableDAO.permenentlyRemoveEntity(dto.getEntityClass(), dto.getId());
	}
	
	/**
	 * Sets the entity as not active, dosen't permanently remove from database.
	 * @param <T>
	 * @param entityClass
	 * @param entityId
	 */
	public <T extends ValueDTO> void remove(Class<T> entityClass, int entityId) {
		//check if it's one of the permitted classes
		try {
			dao.removeEntity(entityClass.newInstance().getEntityClass(), entityId);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException("Internal error while removing");
		}
	}

	public Integer addCountry(CountryDTO country) {
		return dao.addEntity(country, Country::new);
	}
	
	public Integer addCity(CityDTO city) {
		if(city.getCountry() == null || city.getCountry().getId() == null)
			throw new IllegalArgumentException("Country is mandatory");
		return dao.addEntity(city, City::new);
	}
	
	public Integer addBank(BankDTO bank) {
		return dao.addEntity(bank, Bank::new);
	}
	
	public Integer addBankBranch(BankBranchDTO branch) {
		if(branch.getBank() == null || branch.getBank().getId() == null)
			throw new IllegalArgumentException("Bank is mandatory");
		return dao.addEntity(branch, BankBranch::new);
	}
	
	public Integer addSupplyCategory(SupplyCategoryDTO category) {
		return dao.addEntity(category, SupplyCategory::new);
	}
	
	public Integer addCompanyPosition(CompanyPositionDTO position) {
		return dao.addEntity(position, CompanyPosition::new);
	}
	
	public Integer addItem(ItemDTO item) {
		return dao.addEntity(item, Item::new);
	}
	
	public Integer addCashewItem(CashewItemDTO item) {
		return dao.addEntity(item, CashewItem::new);
	}
	
	public Integer addContractType(ContractTypeDTO type) {
		return dao.addEntity(type, ContractType::new);
	}
	
	public Integer addProductionLine(ProductionLineDTO line) {
		return dao.addEntity(line, ProductionLine::new);
	}
	
	public Integer addWarehouse(WarehouseDTO warehouse) {
		return dao.addEntity(warehouse, Warehouse::new);
	}
	
	public Integer addCashewStandard(CashewStandardDTO standard) {
		return dao.addEntity(standard, CashewStandard::new);
	}
	
	public Integer addShippingPort(ShippingPortDTO port) {
		return dao.addEntity(port, ShippingPort::new);
	}
	
	public Integer addCashewGrade(CashewGradeDTO cashewGrade) {
		return dao.addEntity(cashewGrade, CashewGrade::new);
	}
	
}
