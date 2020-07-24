/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.repositories.ObjectTablesRepository;

import lombok.AccessLevel;
import lombok.Getter;

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
	
	/**
	 * Gets the po code basic information of all open Cashew Orders  - id, poCode and supplier.
	 * Open order - orders that are yet to be fully received and not cancelled. 
	 * Can be used for choosing an order to receive.
	 * @return Set of PoCodeDTO for all open Cashew orders.
	 */
	public Set<PoCodeDTO> findOpenCashewOrdersPoCodes() {
		return getObjectTablesRepository().findOpenPoCodeByType(ProcessName.CASHEW_ORDER);		
	}
	
	/**
	 * Gets the po code basic information of all open General Orders  - id, poCode and supplier.
	 * Open order - orders that are yet to be fully received and not cancelled. 
	 * Can be used for choosing an order to receive.
	 * @return Set of PoCodeDTO for all open General orders.
	 */
	public Set<PoCodeDTO> findOpenGeneralOrdersPoCodes() {
		return getObjectTablesRepository().findOpenPoCodeByType(ProcessName.GENERAL_ORDER);
	}
	
	/**
	 * Gets the po code basic information of all open and pending Cashew Orders  - id, poCode and supplier.
	 * Open and pending order - orders that aren't fully received(receipt finalized) and not cancelled. 
	 * Can be used for choosing an order for receipt QC and samples.
	 * @return Set of PoCodeDTO for all open or pending Cashew orders.
	 */
	public Set<PoCodeDTO> findOpenAndPendingCashewOrdersPoCodes() {
		Set<PoCodeDTO> poCodes = getObjectTablesRepository().findAllPoCodeByType(new ProcessName[] {ProcessName.CASHEW_RECEIPT, ProcessName.CASHEW_ORDER_RECEIPT}, 
				new ProcessStatus[] {ProcessStatus.PENDING});
		poCodes.addAll(getObjectTablesRepository().findOpenPoCodeByType(ProcessName.CASHEW_ORDER));

		return poCodes;
	}
	
	/**
	 * Gets the po code basic information of all Cashew in inventory - id, poCode and supplier.
	 * Cashew in inventory - process outcomes that are finalized. 
	 * Can be used for choosing a po for factory processing.
	 * @return Set of PoCodeDTO for all inventory Cashew.
	 */
	public Set<PoCodeDTO> findCashewInventoryPoCodes() {
		return getObjectTablesRepository().findInventoryPoCodeByType(SupplyGroup.CASHEW, null);		
	}
	
	/**
	 * Gets the po code basic information of all General items in inventory - id, poCode and supplier.
	 * General inventory - process outcomes that are finalized. 
	 * Can be used for choosing a po for factory processing.
	 * @return Set of PoCodeDTO for all General inventory.
	 */
	public Set<PoCodeDTO> findGeneralInventoryPoCodes() {
		return getObjectTablesRepository().findInventoryPoCodeByType(SupplyGroup.GENERAL, null);		
	}
	
	/**
	 * Gets the basic information of all po codes for the given item in inventory - id, poCode and supplier.
	 * Can be used for choosing a po for factory processing of a certain item.
	 * @param itemId id of the item
	 * @return Set of PoCodeDTO
	 */
	public Set<PoCodeDTO> findInventoryPoCodes(Integer itemId) {
		return getObjectTablesRepository().findInventoryPoCodeByType(null, itemId);		
	}
	
	/**
	 * Get the table of all po codes.
	 * Can be used for searching reports for any PO.
	 * @return Set of PoRow for po's that are still active - still in production.
	 */
	public Set<PoCodeDTO> findAllPoCodes() {
		return getObjectTablesRepository().findAllPoCodeDTOs();
	}
	
	//commented so should use the same methods that return list of PoCodeDTO
//	/**
//	 * Gets the basic information of all open Cashew Orders with given OrderStatus - id, poCode, supplier and orderStatus.
//	 * @return List of PoBasic for all open Cashew orders.
//	 */
//	public List<PoBasic> findOpenCashewOrdersBasic() {
//		return getObjectTablesRepository().findOpenOrderByTypeBasic(ProcessName.CASHEW_ORDER);		
//	}
//	
//	/**
//	 * Gets the basic information of all General Orders with given OrderStatus - id, poCode, supplier and orderStatus.
//	 * @return List of PoBasic for all open General orders.
//	 */
//	public List<PoBasic> findOpenGeneralOrdersBasic() {
//		return getObjectTablesRepository().findOpenOrderByTypeBasic(ProcessName.GENERAL_ORDER);
//	}
//	
//	/**
//	 * Get the table of all active Cashew po that can be processed.
//	 * @return list of PoRow for po's that are still active - still in production.
//	 */
//	public List<PoBasic> findActiveCashewPoBasic() {
//		List<PoBasic> list = findOpenCashewOrdersBasic();
//		list.addAll(getObjectTablesRepository().findReceivedPOsBasic(
//				new ProcessName[] {ProcessName.CASHEW_ORDER_RECEIPT, ProcessName.CASHEW_RECEIPT}));
//		return list;
//	}
	
}
