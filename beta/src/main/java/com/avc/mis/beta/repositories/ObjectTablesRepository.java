/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.entities.ObjectEntityWithId;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.process.PoCode;

/**
 * @author Zvi
 *
 */
public interface ObjectTablesRepository extends BaseRepository<ObjectEntityWithId> {
	
//-----------Finding all Base entities--------------
	
	@Query("select e from PoCode e")
	List<PoCode> findAllPoCodes();
	
//-----------Finding all Object entities with active=true--------------

	@Query("select e from BankAccount e where e.active = true")
	List<BankAccount> findAllBankAccounts();

	@Query("select e from Company e where e.active = true")
	List<Company> findAllCompanies();

	@Query("select e from Person e where e.active = true")
	List<Person> findAllPersons();

	@Query("select e from CompanyContact e where e.active = true")
	List<CompanyContact> findAllCompanyContacts();
	
	@Query("select e from UserEntity e where e.active = true")
	List<UserEntity> findAllUsers();
	
//------------Finding all Link entities------------

	@Query("select e from ProcessManagement e")
	List<ProcessManagement> findAllProcessAlerts();

	@Query("select e from ContactDetails e")
	List<ContactDetails> findAllContactDetails();
	
//--------------------Finding PO code lists-----------------------
	

//	@Query("select new com.avc.mis.beta.dto.values.PoBasic(po.id, po.version, "
//				+ "po_code.code, po_code.contractType, s.name, s.id, s.version) "
//			+ "from PO po "
//			+ "join po.poCode po_code "
//			+ "join po_code.supplier s "
//			+ "join po.processType t "
//			+ "join po.orderItems oi "
//			+ "where not exists (select ri from ReceiptItem ri where ri.orderItem = oi) "
//				+ "and t.processName = ?1 ")
//	List<PoBasic> findOpenOrderByTypeBasic(ProcessName orderType);
//
//	@Query("select new com.avc.mis.beta.dto.values.PoBasic(r.id, r.version, "
//				+ "po_code.code, po_code.contractType, s.name, s.id, s.version) "
//			+ "from Receipt r "
//			+ "join r.poCode po_code "
//			+ "join po_code.supplier s "
//			+ "join r.processType t "
//				+ "where t.processName in ?1 ")
//	List<PoBasic> findReceivedPOsBasic(ProcessName[] receiptType);

	/**
	 * Gets po code basic info for orders that are yet to be fully received 
	 * and not cancelled. Can be used for choosing an order to receive.
	 * @param processName
	 * @return Set of PoCodeDTO
	 */
	@Query("select new com.avc.mis.beta.dto.process.PoCodeDTO("
			+ "po_code.code, ct.code, ct.suffix, s.name) "
		+ "from PO po "
			+ "join po.lifeCycle lc "
			+ "join po.poCode po_code "
				+ "join po_code.contractType ct "
				+ "join po_code.supplier s "
			+ "join po.processType t "
			+ "join po.orderItems oi "
				+ "join oi.numberUnits units "
				+ "left join oi.receiptItems ri "
					+ "left join ri.process r "
						+ "left join r.lifeCycle rlc "		
					+ "left join ri.storageForms sf "
//						+ "left join sf.numberUnits sfNumberUnits"
						+ "left join sf.unitAmount unit "
							+ "left join UOM uom "
								+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = units.measureUnit "
		+ "where "
			+ "t.processName = :processName "
			+ "and (lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.PENDING "
				+ "or lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
		+ "group by oi, units "
		+ "having coalesce("
			+ "SUM("
				+ "CASE "
					+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN null  "
					+ "ELSE (unit.amount * coalesce(sf.numberUnits, 0) * uom.multiplicand / uom.divisor) "
				+ "END), "
			+ "0) < units.amount ")
	Set<PoCodeDTO> findOpenPoCodeByType(ProcessName processName);
	
	/**
	 * Gets po codes for given processes that are in one of the given process statuses
	 * @param processNames
	 * @param statuses
	 * @return Set of PoCodeDTO
	 */
	@Query("select new com.avc.mis.beta.dto.process.PoCodeDTO("
			+ "po_code.code, c.code, c.suffix, s.name) "
		+ "from Receipt r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType c "
				+ "join po_code.supplier s "
			+ "join r.processType t "
			+ "join r.lifeCycle lc "
		+ "where t.processName in :processNames "
			+ "and lc.processStatus in :statuses ")
	Set<PoCodeDTO> findAllPoCodeByType(ProcessName[] processNames, ProcessStatus[] statuses);
	
	/**
	 * Gets set of All PoCodes that are currently in inventory 
	 * and contain the given item and belong to given supply group.
	 * @param supplyGroup constrain to only this supply group, if null than any.
	 * @param itemId constrain to only this item, if null than any.
	 * @return Set of PoCodeDTO
	 */
	@Query("select new com.avc.mis.beta.dto.process.PoCodeDTO("
			+ "poCode.code, ct.code, ct.suffix, s.name) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
			+ "join pi.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join p.lifeCycle lc "
			+ "join pi.allStorags sf "
//				+ "join sf.numberUnits sfNumberUnits"
				+ "join sf.unitAmount unit "
					+ "join UOM uom "
						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
				+ "left join sf.usedItems ui "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (item.supplyGroup = :supplyGroup or :supplyGroup is null)  "
			+ "and (:checkCategories = false or item.category in :itemCategories)  "
			+ "and (item.id = :itemId or :itemId is null)  "
		+ "group by sf, sf.numberUnits "
		+ "having (sf.numberUnits > sum(coalesce(ui.numberUsedUnits, 0))) ")
	Set<PoCodeDTO> findInventoryPoCodeByType(boolean checkCategories, ItemCategory[] itemCategories, SupplyGroup supplyGroup, Integer itemId);

	@Query("select new com.avc.mis.beta.dto.process.PoCodeDTO("
			+ "po_code.code, c.code, c.suffix, s.name) "
		+ "from Receipt r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType c "
				+ "join po_code.supplier s "
			+ "join r.processType t "
		+ "where t.processName in ?1 "
		+ "order by po_code.code desc ")
	List<PoCodeDTO> findReceivedPoCodeByTypes(ProcessName[] processNames);

	//will also give old (history) po_codes
	@Query("select distinct new com.avc.mis.beta.dto.process.PoCodeDTO("
			+ "po_code.code, c.code, c.suffix, s.name) "
		+ "from PoCode po_code "
				+ "join po_code.contractType c "
				+ "join po_code.supplier s "
//			+ "join p.processType t "
//		+ "where t.processName in ?1 "
		+ "order by po_code.code desc ")
	Set<PoCodeDTO> findAllPoCodeDTOs();

	
}
