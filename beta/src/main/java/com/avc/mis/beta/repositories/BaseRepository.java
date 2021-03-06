/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.CashewStandard;
import com.avc.mis.beta.entities.values.City;
import com.avc.mis.beta.entities.values.CompanyPosition;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.entities.values.ShippingPort;
import com.avc.mis.beta.entities.values.SupplyCategory;
import com.avc.mis.beta.entities.values.Warehouse;

/**
 * Base repository which is the root for all Spring repositories.
 * Defines methods that are often accessed my multiple types of repositories.
 * e.g. general referenced lists of @see ValueEntity e.g. list of cities.
 * 
 * @author Zvi
 * @param <T>
 * 
 */
@NoRepositoryBean
public interface BaseRepository<T extends Insertable> extends Repository<T, Integer>{
	
	@Query("select new com.avc.mis.beta.dto.values.BasicValueEntity(s.id, s.value) "
			+ "from Warehouse s where s.active = true "
			+ "order by s.value ")
	List<BasicValueEntity<Warehouse>> findAllWarehousesDTO();
	
	@Query("select s "
			+ "from Warehouse s where s.active = true")
	List<Warehouse> findAllWarehouses();
	
	@Query("select t from ProcessType t where t.processName = :value")
	Optional<ProcessType> findProcessTypeByValue(ProcessName value);
		
	@Query("select b from Bank b where b.active = true")
	List<Bank> findAllBanks();
	
	@Query("select c from City c where c.active = true")
	List<City> findAllCities();
	
	@Query("select c from Country c where c.active = true")
	List<Country> findAllCountries();
	
	@Query("select cp from CompanyPosition cp where cp.active = true")
	List<CompanyPosition> findAllCompanyPositions();
	
	@Query("select bb from BankBranch bb where bb.active = true")
	List<BankBranch> findAllBankBranches();
	
	@Query("select new com.avc.mis.beta.dto.values.BankBranchDTO(bb.id, bb.value, bank.value) "
			+ "from BankBranch bb "
			+ "join bb.bank bank "
			+ " where bb.active = true")
	List<BankBranchDTO> findAllBankBranchesDTO();
	
	@Query("select new com.avc.mis.beta.dto.values.CityDTO(c.id, c.value, ctry.value) "
			+ "from City c "
			+ "join c.country ctry "
			+ " where c.active = true")
	List<CityDTO> findAllCitiesDTO();
	
	@Query("select sc from SupplyCategory sc where sc.active = true")
	List<SupplyCategory> findAllSupplyCategories();
	
	@Query("select i from Item i where i.active = true")
	List<Item> findAllItems();
	
	@Query("select i from ShippingPort i where i.active = true")
	List<ShippingPort> findAllShippingPorts();
	
	@Query("select t from ContractType t "
			+ "where t.active = true "
				+ "and t.supplyGroup in :supplyGroups "
			+ "order by t.value ")
	List<ContractType> findAllContractTypes(SupplyGroup[] supplyGroups);

	@Query("select t from ProcessType t where t.active = true")
	List<ProcessType> findAllProcessTypes();

//	@Query("select t from ProductionLine t where t.active = true")
//	List<ProductionLine> findAllProductionLines();

	@Query("select t from ProductionLine t "
			+ "where t.active = true "
				+ "and t.productionFunctionality in :functionalities "
			+ "order by t.value ")
	List<ProductionLine> findProductionLinesByFuncionality(ProductionFunctionality[] functionalities);

	@Query("select t from CashewStandard t "
			+ "where t.active = true ")
	List<CashewStandard> findAllCashewStandard();
	
	//for inserting contract type in testing
	@Query("select t from ContractType t where t.code = :code and t.currency = :currency")
	ContractType findContractTypeByCodeAndCurrency(String code, Currency currency);

}
