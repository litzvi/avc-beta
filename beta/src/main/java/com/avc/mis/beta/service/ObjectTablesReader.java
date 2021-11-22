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
import com.avc.mis.beta.dto.data.BankAccountDTO;
import com.avc.mis.beta.dto.data.PersonDTO;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.ProductPoCode;
import com.avc.mis.beta.entities.enums.ItemGroup;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.enums.ProductionUse;
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

	public List<BankAccountDTO> getAllBankAccounts() {
		return getObjectTablesRepository().findAllBankAccounts();		
	}
		
	public List<PersonDTO> getAllPersons() {
		return getObjectTablesRepository().findAllPersons();		
	}
	
//-----------------------------Dependent Objects--------------------------------------------
	
	
//----------------------------Contact Details Objects-------------------------------------------
	
	//no need to add them for now - address, email, fax, phone and payment account.
	
	
//---------------------------------DTOs---------------------------------------------------------
	
	public List<PoCodeBasic> findFreePoCodes() {
		return getObjectTablesRepository().findFreePoCodes(null, ProductPoCode.class);		
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
	public List<PoCodeBasicWithProductCompany> findAllProductPoCodes() {
		return getObjectTablesRepository().findAllPoCodeBasics(ProductPoCode.class);
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
	
	@Deprecated
	@Autowired private WarehouseManagement warehouseManagement;
	
	@Deprecated
	public Set<PoCodeBasic> findCashewAvailableInventoryPoCodes() {
		return getWarehouseManagement().findCashewAvailableInventoryPoCodes();		
	}

	@Deprecated
	public Set<PoCodeBasic> findGeneralAvailableInventoryPoCodes() {
		return getWarehouseManagement().findGeneralAvailableInventoryPoCodes();		
	}
	
	@Deprecated
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(Integer itemId) {
		return getWarehouseManagement().findAvailableInventoryPoCodes(itemId);	
	}
	
	@Deprecated
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(@NonNull ProductionUse[] productionUses) {
		return getWarehouseManagement().findAvailableInventoryPoCodes(productionUses, null);		
	}
	
	@Deprecated
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(@NonNull ProductionUse[] productionUses, ProductionFunctionality[] functionalities) {
		return getWarehouseManagement().findAvailableInventoryPoCodes(productionUses, functionalities, null);		
	}
	
	@Deprecated
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(ItemGroup group) {
		return getWarehouseManagement().findAvailableInventoryPoCodes(group, null);		
	}
	
	@Deprecated
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(ProductionUse[] productionUses, ItemGroup group) {
		return getWarehouseManagement().findAvailableInventoryPoCodes(productionUses, group, null);
	}


}
