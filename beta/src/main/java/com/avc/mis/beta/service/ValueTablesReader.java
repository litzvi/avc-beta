/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.basic.ValueEntityObject;
import com.avc.mis.beta.dto.data.DataObjectWithName;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.PackedItem;
import com.avc.mis.beta.entities.item.ProductionUse;
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
//		getValueTablesRepository().findAllItems().forEach(i->System.out.println(i));
//		if(true)
//			throw new NullPointerException();
		return getValueTablesRepository().findAllItems();
	}
	
	public List<ContractType> getAllContractTypes() {
		return getValueTablesRepository().findAllContractTypes();
	}
	
//	public List<ProcessStatus> getAllProcessStatuses() {
//		return getValueTablesRepository().findAllProcessStatuses();
//	}
	
	public List<ProcessType> getAllProcessTypes() {
		return getValueTablesRepository().findAllProcessTypes();
	}
	
	public List<ProductionLine> getAllProductionLines() {
		return getValueTablesRepository().findAllProductionLines();
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
	public List<DataObjectWithName> getSuppliersBasic() {
		return getSupplierRepository().findAllSuppliersBasic();
	}
			
	/**
	 * Get a list of suppliers basic information -  id, name and version - for given supply category.
	 * @param categoryId id of SupplyCategory
	 * @return List of SupplierBasic of all suppliers with given SupplyCategory
	 */
	@Transactional(readOnly = true)
	public List<DataObjectWithName> getSuppliersBasic(Integer categoryId) {
		return getSupplierRepository().findSuppliersByCategoryBasic(categoryId);
	}
	
	/**
	 * Get a list of CASHEW suppliers basic information -  id, name and version.
	 * @return List of SupplierBasic of all CASHEW suppliers.
	 */
//	@Transactional(readOnly = true)
	public List<DataObjectWithName> getCashewSuppliersBasic() {
		return getSupplierRepository().findSuppliersByGroupBasic(SupplyGroup.CASHEW);
	}
	
	/**
	 * Get a list of GENERAL suppliers basic information -  id, name and version.
	 * @return List of SupplierBasic of all GENERAL suppliers.
	 */
//	@Transactional(readOnly = true)
	public List<DataObjectWithName> getGeneralSuppliersBasic() {
		return getSupplierRepository().findSuppliersByGroupBasic(SupplyGroup.GENERAL);
	}
	
	/**
	 * Get a list of CASHEW items basic -  id and value.
	 * @return List of ItemDTO of all CASHEW items.
	 */
//	@Transactional(readOnly = true)
//	public List<ItemDTO> getCashewItemsBasic() {
//		return getValueTablesRepository().findItemsByGroupBasic(ItemGroup.PRODUCT, null);
//	}
	
	/**
	 * Get a list of GENERAL items basic -  id and value.
	 * @return List of ItemDTO of all GENERAL items.
	 */
//	@Transactional(readOnly = true)
//	public List<ItemDTO> getGeneralItemsBasic() {
//		return getValueTablesRepository().findItemsByGroupBasic(ItemGroup.GENERAL, null);
//	}
	
	public List<ItemDTO> getItemsByPrudoctionUse(ProductionUse productionUse) {
		return getValueTablesRepository().findItemsByGroupBasic(null, productionUse, Arrays.asList(BulkItem.class, PackedItem.class));
	}

	public List<ItemDTO> getItemsByGroup(ItemGroup itemGroup) {
		return getValueTablesRepository().findItemsByGroupBasic(itemGroup, null, Arrays.asList(BulkItem.class, PackedItem.class));
	}
	
	public List<ItemDTO> getItems(ItemGroup itemGroup, ProductionUse productionUse) {
		return getValueTablesRepository().findItemsByGroupBasic(itemGroup, productionUse, Arrays.asList(BulkItem.class, PackedItem.class));
	}
	
	public List<ItemDTO> getItems(ItemGroup itemGroup, ProductionUse productionUse, Class<? extends Item> clazz) {
		return getValueTablesRepository().findItemsByGroupBasic(itemGroup, productionUse, Arrays.asList(clazz));
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
	
}
