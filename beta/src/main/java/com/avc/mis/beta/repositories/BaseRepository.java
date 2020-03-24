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
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.SupplyCategory;

/**
 * @author Zvi
 * @param <T>
 *
 */
@RestResource(exported = false)
public interface BaseRepository<T extends Insertable> extends Repository<T, Integer>{

	@Query("select c from City c where c.deleted = false")
	List<City> findAllCities();
	
	@Query("select c from Country c where c.deleted = false")
	List<Country> findAllCountries();
	
	@Query("select cp from CompanyPosition cp where cp.deleted = false")
	List<CompanyPosition> findAllCompanyPositions();
	
	@Query("select b from Bank b where b.deleted = false")
	List<Bank> findAllBanks();
	
	@Query("select bb from BankBranch bb where bb.deleted = false")
	List<BankBranch> findAllBankBranches();
	
	@Query("select new com.avc.mis.beta.dto.values.BankBranchDTO(bb.id, bb.value, bank.value) "
			+ "from BankBranch bb "
			+ "join bb.bank bank "
			+ " where bb.deleted = false")
	List<BankBranchDTO> findAllBankBranchesDTO();
	
	@Query("select new com.avc.mis.beta.dto.values.CityDTO(c.id, c.value, ctry.value) "
			+ "from City c "
			+ "join c.country ctry "
			+ " where c.deleted = false")
	List<CityDTO> findAllCitiesDTO();
	
	@Query("select sc from SupplyCategory sc where sc.deleted = false")
	List<SupplyCategory> findAllSupplyCategories();
	
	@Query("select i from Item i where i.deleted = false")
	List<Item> findAllItems();
	
	@Query("select t from ContractType t where t.deleted = false")
	List<ContractType> findAllContractTypes();


}
