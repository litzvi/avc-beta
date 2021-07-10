/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.basic.ProductionLineBasic;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.generic.ValueEntityObject;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.CashewItemDTO;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.PackageType;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.entities.values.CashewGrade;
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
import com.avc.mis.beta.repositories.QCRepository;
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
	@Autowired private QCRepository qcRepository;
	
	public List<Warehouse> getAllWarehouses() {
		return getValueTablesRepository().findAllWarehouses();		
	}
	
	public List<CashewGrade> getAllCashewGrades() {
		return getValueTablesRepository().findAllCashewGrades();		
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
		return getValueTablesRepository().findAllContractTypes(SupplyGroup.values());
	}
	
	public List<ContractType> getCashewContractTypes() {
		return getValueTablesRepository().findAllContractTypes(new SupplyGroup[]{SupplyGroup.CASHEW});
	}
	
	public List<ContractType> getGeneralContractTypes() {
		return getValueTablesRepository().findAllContractTypes(new SupplyGroup[]{SupplyGroup.GENERAL});
	}
	
	public List<ProcessType> getAllProcessTypes() {
		return getValueTablesRepository().findAllProcessTypes();
	}
	
	public List<ProductionLine> getAllProductionLines() {
		return getValueTablesRepository().findProductionLinesByFuncionality(ProductionFunctionality.values());
	}
	
	public List<ProductionLine> getProductionLinesByFuncionality(ProductionFunctionality[] functionalities) {
		return getValueTablesRepository().findProductionLinesByFuncionality(functionalities);
	}
	
	public List<CashewStandard> getAllCashewStandards() {
		return getValueTablesRepository().findAllCashewStandard();
	}
	
	public List<ShippingPort> getAllShippingPorts() {
		return getValueTablesRepository().findAllShippingPorts();
	}
	
//----------------------------DTO---------------------------------------------------------	
	
	public List<CashewStandardDTO> getAllCashewStandardsDTO() {
		List<CashewStandardDTO> cashewStandards = getQcRepository().findAllCashewStandardDTO();
		Map<Integer, Set<BasicValueEntity<Item>>> items = getQcRepository().findAllStandardItems()
				.collect(Collectors.groupingBy(ValueEntityObject::getId, 
						Collectors.mapping(ValueEntityObject::getValue, Collectors.toSet())));
		cashewStandards.forEach(s -> s.setItems(items.get(s.getId())));
		return cashewStandards;
	}
	
	public List<BasicValueEntity<Warehouse>> getAllWarehousesDTO() {
		return getValueTablesRepository().findAllWarehousesDTO();		
	}
	
	public List<BasicValueEntity<CashewGrade>> getAllCashewGradesDTO() {
		return getValueTablesRepository().findAllCashewGradesDTO();		
	}
	
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
	public List<DataObjectWithName<Supplier>> getSuppliersBasic() {
		return getSupplierRepository().findAllSuppliersBasic();
	}
			
	/**
	 * Get a list of suppliers basic information -  id, name and version - for given supply category.
	 * @param categoryId id of SupplyCategory
	 * @return List of SupplierBasic of all suppliers with given SupplyCategory
	 */
	public List<DataObjectWithName<Supplier>> getSuppliersBasic(Integer categoryId) {
		return getSupplierRepository().findSuppliersByCategoryBasic(categoryId);
	}
	
	public List<DataObjectWithName<Supplier>> getSuppliersBasicByGroup(SupplyGroup supplyGroup) {
		return getSupplierRepository().findSuppliersByGroupBasic(supplyGroup);
	}
	
	/**
	 * Get a list of CASHEW suppliers basic information -  id, name and version.
	 * @return List of SupplierBasic of all CASHEW suppliers.
	 */
	public List<DataObjectWithName<Supplier>> getCashewSuppliersBasic() {
		return getSupplierRepository().findSuppliersByGroupBasic(SupplyGroup.CASHEW);
	}
	
	/**
	 * Get a list of GENERAL suppliers basic information -  id, name and version.
	 * @return List of SupplierBasic of all GENERAL suppliers.
	 */
	public List<DataObjectWithName<Supplier>> getGeneralSuppliersBasic() {
		return getSupplierRepository().findSuppliersByGroupBasic(SupplyGroup.GENERAL);
	}
	
	public List<ItemDTO> getItemsByPrudoctionUse(ProductionUse[] productionUses) {
		return getItems(null, productionUses, null);//, Arrays.asList(BulkItem.class, PackedItem.class));
	}

	public List<ItemDTO> getItemsByGroup(ItemGroup itemGroup) {
		return getItems(itemGroup, null, null);//, Arrays.asList(BulkItem.class, PackedItem.class));
	}
	
	public List<ItemDTO> getItems(ItemGroup itemGroup, ProductionUse[] productionUses) {
		return getItems(itemGroup, productionUses, null);//, Arrays.asList(BulkItem.class, PackedItem.class));
	}
	
	public List<ItemDTO> getItems(ItemGroup itemGroup, ProductionUse[] productionUses, PackageType packageType) {
		boolean checkProductionUses = (productionUses != null);
		Integer packageTypeOrdinal = packageType != null ? packageType.ordinal() : null;
		return getValueTablesRepository().findItemsByGroupBasic(itemGroup, checkProductionUses, productionUses, packageTypeOrdinal);
	}
	
	public List<CashewItemDTO> getCashewItems(ItemGroup itemGroup, ProductionUse productionUse, Integer gradeId, PackageType packageType) {
		Integer packageTypeOrdinal = packageType != null ? packageType.ordinal() : null;
		return getValueTablesRepository().findCashewItems(itemGroup, productionUse, gradeId, packageTypeOrdinal);
	}
	
	/**
	 * Get a list of CASHEW items from given category -  id and value.
	 * @param category ItemCategory
	 * @return List of BasicValueEntity of CASHEW items that belong to given category.
	 */
//	@Transactional(readOnly = true)
	public List<BasicValueEntity<Item>> getBasicItemsByPrudoctionUse(ProductionUse productionUse) {
		return getValueTablesRepository().findBasicItems(null, productionUse);
	}

	public List<BasicValueEntity<Item>> getBasicItemsByGroup(ItemGroup itemGroup) {
		return getValueTablesRepository().findBasicItems(itemGroup, null);
	}
	
	public List<BasicValueEntity<Item>> getBasicItems(ItemGroup itemGroup, ProductionUse productionUse) {
		return getValueTablesRepository().findBasicItems(itemGroup, productionUse);
	}
	
	public List<ProductionLineBasic> getAllBasicProductionLines() {
		return getValueTablesRepository().findBasicProductionLines(ProductionFunctionality.values());
	}
	
	public List<ProductionLineBasic> getAllBasicProductionLinesByFuncionality(ProductionFunctionality[] functionalities) {
		return getValueTablesRepository().findBasicProductionLines(functionalities);
	}
	
	
	
	

	
}
