/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.CityDTO;
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
import com.avc.mis.beta.entities.values.SupplyCategory;

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
@Repository
@Transactional(readOnly = true)
public class ValueTablesReader extends DAO {
	
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
	
}
