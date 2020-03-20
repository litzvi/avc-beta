/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.entities.Insertable;
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
 * @param <T>
 *
 */
@RestResource(exported = false)
public interface BaseRepository<T extends Insertable> extends Repository<T, Integer>{

	@Query("select c from City c")
	List<City> findAllCities();
	
	@Query("select c from Country c")
	List<Country> findAllCountries();
	
	@Query("select cp from CompanyPosition cp")
	List<CompanyPosition> findAllCompanyPositions();
	
	@Query("select b from Bank b")
	List<Bank> findAllBanks();
	
	@Query("select bb from BankBranch bb")
	List<BankBranch> findAllBankBranches();
	
	@Query("select new com.avc.mis.beta.dto.values.BankBranchDTO(bb.id, bb.value, bank.value) "
			+ "from BankBranch bb "
			+ "join bb.bank bank ")
	List<BankBranchDTO> findAllBankBranchesDTO();
	
	@Query("select new com.avc.mis.beta.dto.values.CityDTO(c.id, c.value, ctry.value) "
			+ "from City c "
			+ "join c.country ctry ")
	List<CityDTO> findAllCitiesDTO();
	
	@Query("select sc from SupplyCategory sc")
	List<SupplyCategory> findAllSupplyCategories();
	
	@Query("select i from Item i")
	List<Item> findAllItems();
	
	@Query("select t from ContractType t")
	List<ContractType> findAllContractTypes();


}
