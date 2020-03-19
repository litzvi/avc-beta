/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.BankBranchDTO;
import com.avc.mis.beta.dto.CityDTO;
import com.avc.mis.beta.entities.data.Bank;
import com.avc.mis.beta.entities.data.BankBranch;
import com.avc.mis.beta.entities.data.City;
import com.avc.mis.beta.entities.data.CompanyPosition;
import com.avc.mis.beta.entities.data.Country;
import com.avc.mis.beta.entities.data.Item;
import com.avc.mis.beta.entities.data.SupplyCategory;
import com.avc.mis.beta.entities.process.ContractType;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(readOnly = true)
public class ReferenceTables extends DAO {
	
	public List<SupplyCategory> getAllSupplyCategories() {
		return getReferenceRepository().findAllSupplyCategories();
	}
	
	public List<CityDTO> getAllCitiesDTO() {
		return getReferenceRepository().findAllCitiesDTO();
	}
	
	public List<City> getAllCities() {
		return getReferenceRepository().findAllCities();
	}
	
	public List<Country> getAllCountries() {
		return getReferenceRepository().findAllCountries();
	}
	
	public Map<String, List<CityDTO>> getCitiesByCountry() {		
		return getReferenceRepository().findAllCitiesDTO().stream()
				.collect(Collectors.groupingBy(city -> city.getCountryName()));
	}

	public List<CompanyPosition> getAllCompanyPositions() {	
		return getReferenceRepository().findAllCompanyPositions();
	}
	
	public List<Bank> getAllBanks() {
		return getReferenceRepository().findAllBanks();
	}
	
	public List<BankBranchDTO> getAllBankBranchesDTO() {
		return getReferenceRepository().findAllBankBranchesDTO();
	}
	
	public List<BankBranch> getAllBankBranches() {
		return getReferenceRepository().findAllBankBranches();
	}
	
	public List<Item> getAllItems() {
		return getReferenceRepository().findAllItems();
	}
	
	public List<ContractType> getAllContractTypes() {
		return getReferenceRepository().findAllContractTypes();
	}
	
}
