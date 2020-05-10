/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.ItemBasic;
import com.avc.mis.beta.dto.values.SupplierBasic;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.entities.values.Storage;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.repositories.SupplierRepository;
import com.avc.mis.beta.repositories.ValueTablesRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Used to access full lists (tables) of active Value entities - {@link com.avc.mis.beta.entities.ValueEntity}
 * Gets list for user input data to reference (usually by choosing from a list).
 * Dosen't get non active entities (soft deleted entities).
 * Value entities should be first in restore order before other types of Entities, 
 * ideally getters should be ordered by restore order.
 *  
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ValueTablesReader {
	
	@Autowired private SupplierRepository supplierRepository;
	@Autowired private ValueTablesRepository valueTablesRepository;
	
	
	public List<Storage> getAllStorages() {
		return getValueTablesRepository().findAllStorages();		
	}
	
	public List<Country> getAllCountries() {
		return getValueTablesRepository().findAllCountries();
	}
		
	public List<City> getAllCities() {
		return getValueTablesRepository().findAllCities();
	}
		
	public List<Bank> getAllBanks() {
		return getValueTablesRepository().findAllBanks();
	}
	
	public List<BankBranch> getAllBankBranches() {
		return getValueTablesRepository().findAllBankBranches();
	}
		
	public List<SupplyCategory> getAllSupplyCategories() {
		return getValueTablesRepository().findAllSupplyCategories();
	}
	
	public List<CompanyPosition> getAllCompanyPositions() {	
		return getValueTablesRepository().findAllCompanyPositions();
	}
	
	public List<Item> getAllItems() {
		return getValueTablesRepository().findAllItems();
	}
	
	public List<ContractType> getAllContractTypes() {
		return getValueTablesRepository().findAllContractTypes();
	}
	
	public List<ProcessStatus> getAllProcessStatuses() {
		return getValueTablesRepository().findAllProcessStatuses();
	}
	
	public List<ProcessType> getAllProcessTypes() {
		return getValueTablesRepository().findAllProcessTypes();
	}
	
	public List<ProductionLine> getAllProductionLines() {
		return getValueTablesRepository().findAllProductionLines();
	}
	
//----------------------------DTO---------------------------------------------------------	
	
	public List<CityDTO> getAllCitiesDTO() {
		return getValueTablesRepository().findAllCitiesDTO();
	}	
	
	public Map<String, List<CityDTO>> getCitiesByCountry() {		
		return getValueTablesRepository().findAllCitiesDTO().stream()
				.collect(Collectors.groupingBy(city -> city.getCountryName()));
	}	
	
	public List<BankBranchDTO> getAllBankBranchesDTO() {
		return getValueTablesRepository().findAllBankBranchesDTO();
	}
	
//---------------------------Basic values---------------------------------------------------
	
	/**
	 * Get a list of all suppliers basic information -  id, name and version.
	 * Usually used for referencing a supplier in another process.
	 * @return List of SupplierBasic of all existing suppliers.
	 */
	@Transactional(readOnly = true)
	public List<SupplierBasic> getSuppliersBasic() {
		return getSupplierRepository().findAllSuppliersBasic();
	}
			
	/**
	 * Get a list of suppliers basic information -  id, name and version - for given supply category.
	 * @param categoryId id of SupplyCategory
	 * @return List of SupplierBasic of all suppliers with given SupplyCategory
	 */
	@Transactional(readOnly = true)
	public List<SupplierBasic> getSuppliersBasic(Integer categoryId) {
		return getSupplierRepository().findSuppliersByCategoryBasic(categoryId);
	}
	
	/**
	 * Get a list of CASHEW suppliers basic information -  id, name and version.
	 * @return List of SupplierBasic of all CASHEW suppliers.
	 */
//	@Transactional(readOnly = true)
	public List<SupplierBasic> getCashewSuppliersBasic() {
		return getSupplierRepository().findSuppliersByGroupBasic(SupplyGroup.CASHEW);
	}
	
	/**
	 * Get a list of GENERAL suppliers basic information -  id, name and version.
	 * @return List of SupplierBasic of all GENERAL suppliers.
	 */
//	@Transactional(readOnly = true)
	public List<SupplierBasic> getGeneralSuppliersBasic() {
		return getSupplierRepository().findSuppliersByGroupBasic(SupplyGroup.GENERAL);
	}
	
	/**
	 * Get a list of CASHEW items basic -  id and value.
	 * @return List of ItemBasic of all CASHEW items.
	 */
//	@Transactional(readOnly = true)
	public List<ItemBasic> getCashewitemsBasic() {
		return getValueTablesRepository().findItemsByGroupBasic(SupplyGroup.CASHEW);
	}
	
	/**
	 * Get a list of GENERAL items basic -  id and value.
	 * @return List of ItemBasic of all GENERAL items.
	 */
//	@Transactional(readOnly = true)
	public List<ItemBasic> getGeneralItemsBasic() {
		return getValueTablesRepository().findItemsByGroupBasic(SupplyGroup.GENERAL);
	}
	
}
