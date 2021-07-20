/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.basic.PoCodeBasicWithProductCompany;
import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.ShipmentCode;
import com.avc.mis.beta.repositories.ObjectTablesRepository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Used to access full lists (tables) of active Object entities - {@link com.avc.mis.beta.entities.ObjectDataEntity}
 * Gets list for user input data to reference (usually by choosing from a list).
 * Dosen't get non active entities (soft deleted entities).
 * Object entities should be second in restore order before other types of Entities and after Value entities, 
 * ideally getters should be ordered by restore order.
 * 
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ObjectTablesReader {
	
	@Autowired private ObjectTablesRepository objectTablesRepository;
	
//----------------------------Independent Objects------------------------------------------

	public List<BankAccount> getAllBankAccounts() {
		return getObjectTablesRepository().findAllBankAccounts();		
	}
		
	public List<Person> getAllPersons() {
		return getObjectTablesRepository().findAllPersons();		
	}
	
	public List<Company> getAllCompanies() {
		return getObjectTablesRepository().findAllCompanies();		
	}
	
	public List<PoCode> getAllPoCodes() {
		return getObjectTablesRepository().findAllPoCodes();		
	}
	
	public List<GeneralPoCode> getAllGeneralPoCodes() {
		return getObjectTablesRepository().findAllGeneralPoCodes();		
	}
	
	public List<ShipmentCode> getAllShipmentCodes() {
		return getObjectTablesRepository().findAllShipmentCodes();		
	}
	
//-----------------------------Dependent Objects--------------------------------------------
	
	public List<CompanyContact> getAllCompanyContacts() {
		return getObjectTablesRepository().findAllCompanyContacts();		
	}
	
	public List<ProcessManagement> getAllProcessTypeAlerts() {
		return getObjectTablesRepository().findAllProcessAlerts();		
	}
	
	public List<ContactDetails> getAllContactDetails() {
		return getObjectTablesRepository().findAllContactDetails();		
	}
	
	public List<UserEntity> getAllUsers() {
		return getObjectTablesRepository().findAllUsers();		
	}
	
//----------------------------Contact Details Objects-------------------------------------------
	
	//no need to add them for now - address, email, fax, phone and payment account.
	
	
//---------------------------------DTOs---------------------------------------------------------
	
	public List<PoCodeBasic> findFreePoCodes() {
		return getObjectTablesRepository().findFreePoCodes(null, PoCode.class);		
	}
	
	public List<PoCodeBasic> findFreeGeneralPoCodes() {
		return getObjectTablesRepository().findFreePoCodes(null, GeneralPoCode.class);		
	}
	
	public List<PoCodeBasic> findFreeMixPoCodes() {
		return getObjectTablesRepository().findMixFreePoCodes(null);		
	}
	
	public List<ShipmentCodeBasic> findFreeShipmentCodes() {
		return getObjectTablesRepository().findFreeShipmentCodes(null);		
	}
	
	/**
	 * Gets the po code basic information of all open Cashew Orders  - id, poCode and supplier.
	 * Open order - orders that are yet to be fully received and not cancelled. 
	 * Can be used for choosing an order to receive.
	 * @return Set of PoCodeBasic for all open Cashew orders.
	 */
	public Set<PoCodeBasic> findOpenCashewOrdersPoCodes() {
		return getObjectTablesRepository().findOpenPoCodeByType(ProcessName.CASHEW_ORDER);		
	}
	
	/**
	 * Gets the po code basic information of all open General Orders  - id, poCode and supplier.
	 * Open order - orders that are yet to be fully received and not cancelled. 
	 * Can be used for choosing an order to receive.
	 * @return Set of PoCodeBasic for all open General orders.
	 */
	public Set<PoCodeBasic> findOpenGeneralOrdersPoCodes() {
		return getObjectTablesRepository().findOpenPoCodeByType(ProcessName.GENERAL_ORDER);
	}
	
	/**
	 * Gets the po code basic information of all open and pending Cashew Orders  - id, poCode and supplier.
	 * Open and pending order - orders that aren't fully received(receipt finalized) and not cancelled. 
	 * Can be used for choosing an order for receipt QC and samples.
	 * @return Set of PoCodeBasic for all open or pending Cashew orders.
	 */
	public Set<PoCodeBasic> findOpenAndPendingCashewOrdersPoCodes() {
		Set<PoCodeBasic> poCodes = getObjectTablesRepository().findAllPoCodeByType(new ProcessName[] {ProcessName.CASHEW_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.PENDING});
		poCodes.addAll(getObjectTablesRepository().findOpenPoCodeByType(ProcessName.CASHEW_ORDER));

		return poCodes;
	}
	
	
	/**
	 * Get the table of all product po codes.
	 * Can be used for searching reports for any PO.
	 * @return List of PoCodeBasic
	 */
	public List<PoCodeBasicWithProductCompany> findAllPoCodes() {
		return getObjectTablesRepository().findAllPoCodeBasics(PoCode.class);
	}
	
	/**
	 * Get the table of all general po codes.
	 * Can be used for searching reports for any PO.
	 * @return List of PoCodeBasic
	 */
	public List<PoCodeBasicWithProductCompany> findAllGeneralPoCodes() {
		return getObjectTablesRepository().findAllPoCodeBasics(GeneralPoCode.class);
	}
	
	/**
	 * Get the table of all shipment codes.
	 * @return List of ShipmentCodeBasic
	 */
	public List<ShipmentCodeBasic> findAllShipmentCodes() {
		return getObjectTablesRepository().findAllShipmentCodeBasics();
	}
	
//	--------------------------------------Duplicate in WarehouseManagement - should be removed----------------------------------------
	
	@Autowired private WarehouseManagement warehouseManagement;
	
	public Set<PoCodeBasic> findCashewAvailableInventoryPoCodes() {
		return getWarehouseManagement().findCashewAvailableInventoryPoCodes();		
	}

	public Set<PoCodeBasic> findGeneralAvailableInventoryPoCodes() {
		return getWarehouseManagement().findGeneralAvailableInventoryPoCodes();		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(Integer itemId) {
		return getWarehouseManagement().findAvailableInventoryPoCodes(itemId);	
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(@NonNull ProductionUse[] productionUses) {
		return getWarehouseManagement().findAvailableInventoryPoCodes(productionUses);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(@NonNull ProductionUse[] productionUses, ProductionFunctionality[] functionalities) {
		return getWarehouseManagement().findAvailableInventoryPoCodes(productionUses, functionalities);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(ItemGroup group) {
		return getWarehouseManagement().findAvailableInventoryPoCodes(group);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(ProductionUse[] productionUses, ItemGroup group) {
		return getWarehouseManagement().findAvailableInventoryPoCodes(productionUses, group);
	}


}
