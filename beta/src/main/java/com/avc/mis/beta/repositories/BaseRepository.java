/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BankDTO;
import com.avc.mis.beta.dto.values.CashewGradeDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.CompanyPositionDTO;
import com.avc.mis.beta.dto.values.ContractTypeDTO;
import com.avc.mis.beta.dto.values.CountryDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ProcessTypeDTO;
import com.avc.mis.beta.dto.values.ProductionLineDTO;
import com.avc.mis.beta.dto.values.ShippingPortDTO;
import com.avc.mis.beta.dto.values.SupplyCategoryDTO;
import com.avc.mis.beta.dto.values.WarehouseDTO;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.values.CashewGrade;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.ProcessType;
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
interface BaseRepository<T extends Insertable> extends Repository<T, Integer>{
	
	@Query("select new com.avc.mis.beta.dto.basic.BasicValueEntity(s.id, s.value) "
			+ "from Warehouse s where s.active = true "
			+ "order by s.value ")
	List<BasicValueEntity<Warehouse>> findAllWarehousesDTO();
	
	@Query("select new com.avc.mis.beta.dto.basic.BasicValueEntity(s.id, s.value) "
			+ "from CashewGrade s where s.active = true "
			+ "order by s.value ")
	List<BasicValueEntity<CashewGrade>> findAllCashewGradesDTO();
	
	@Query("select new com.avc.mis.beta.dto.values.WarehouseDTO(s.id, s.value, s.weightCapacityKg, s.volumeSpaceM3) "
			+ "from Warehouse s "
			+ "where s.active = true "
			+ "order by s.value ")
	List<WarehouseDTO> findAllWarehouses();
	
	@Query("select new com.avc.mis.beta.dto.values.CashewGradeDTO(s.id, s.value) "
			+ "from CashewGrade s "
			+ "where s.active = true "
			+ "order by s.value ")
	List<CashewGradeDTO> findAllCashewGrades();
	
	@Query("select t from ProcessType t where t.processName = :value")
	Optional<ProcessType> findProcessTypeByValue(ProcessName value);
		
	@Query("select new com.avc.mis.beta.dto.values.BankDTO(b.id, b.value) "
			+ "from Bank b "
			+ "where b.active = true "
			+ "order by b.value ")
	List<BankDTO> findAllBanks();
	
	@Query("select new com.avc.mis.beta.dto.values.CountryDTO(c.id, c.value) "
			+ "from Country c "
			+ "where c.active = true "
			+ "order by c.value ")
	List<CountryDTO> findAllCountries();
	
	@Query("select new com.avc.mis.beta.dto.values.CompanyPositionDTO(c.id, c.value) "
			+ "from CompanyPosition c "
			+ "where c.active = true "
			+ "order by c.value ")
	List<CompanyPositionDTO> findAllCompanyPositions();
	
	@Query("select new com.avc.mis.beta.dto.values.BankBranchDTO(bb.id, bb.value, b.id, b.value) "
			+ "from BankBranch bb "
				+ "join bb.bank b "
			+ "where bb.active = true "
			+ "order by bb.value ")
	List<BankBranchDTO> findAllBankBranches();
	
	@Query("select new com.avc.mis.beta.dto.values.CityDTO(c.id, c.value, ctry.id, ctry.value) "
			+ "from City c "
				+ "join c.country ctry "
			+ "where c.active = true "
			+ "order by c.value ")
	List<CityDTO> findAllCities();
	
	@Query("select new com.avc.mis.beta.dto.values.SupplyCategoryDTO(s.id, s.value, s.supplyGroup) "
			+ "from SupplyCategory s "
			+ "where s.active = true "
			+ "order by s.value ")
	List<SupplyCategoryDTO> findAllSupplyCategories();
	
	@Query("select new com.avc.mis.beta.dto.values.ItemDTO( "
			+ "i.id, i.value, i.code, i.brand, i.measureUnit, i.itemGroup, i.productionUse, i.unit, type(i) "
			+ ") "
			+ "from Item i "
			+ "where i.active = true "
			+ "order by i.value ")
	List<ItemDTO> findAllItems();
	
	@Query("select new com.avc.mis.beta.dto.values.ShippingPortDTO(i.id, i.value, i.code) "
			+ "from ShippingPort i "
			+ "where i.active = true "
			+ "order by i.value ")
	List<ShippingPortDTO> findAllShippingPorts();
	
	@Query("select new com.avc.mis.beta.dto.values.ContractTypeDTO("
			+ "t.id, t.value, t.code, t.currency, t.suffix, t.supplyGroup"
			+ ") "
			+ "from ContractType t "
			+ "where t.active = true "
				+ "and t.supplyGroup in :supplyGroups "
			+ "order by t.value ")
	List<ContractTypeDTO> findAllContractTypes(SupplyGroup[] supplyGroups);
	
	@Query("select new com.avc.mis.beta.dto.basic.BasicValueEntity(t.id, t.value) "
			+ "from ContractType t "
			+ "where t.active = true "
				+ "and t.supplyGroup in :supplyGroups "
			+ "order by t.value ")
	List<BasicValueEntity<ContractType>> findAllContractTypesDTO(SupplyGroup[] supplyGroups);

	@Query("select new com.avc.mis.beta.dto.values.ProcessTypeDTO(t.id, t.value, t.processName) "
			+ "from ProcessType t "
			+ "where t.active = true "
			+ "order by t.value ")
	List<ProcessTypeDTO> findAllProcessTypes();

	@Query("select new com.avc.mis.beta.dto.values.ProductionLineDTO(t.id, t.value, t.productionFunctionality) "
			+ "from ProductionLine t "
			+ "where t.active = true "
				+ "and t.productionFunctionality in :functionalities "
			+ "order by t.value ")
	List<ProductionLineDTO> findProductionLinesByFuncionality(ProductionFunctionality[] functionalities);
	
	@Query("select t.productionFunctionality from ProductionLine t "
			+ "where t.id in :productionLineId ")
	ProductionFunctionality findFunctionalityByProductionLine(Integer productionLineId);
	

}
