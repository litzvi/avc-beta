/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.values.PoCodeDTO;
import com.avc.mis.beta.entities.ObjectDataEntity;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.PoCode;

/**
 * @author Zvi
 *
 */
public interface ObjectTablesRepository extends BaseRepository<ObjectDataEntity> {
	
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
	

	/**
	 * Gets po code basic info for orders that are yet to be fully received 
	 * and not cancelled. Can be used for choosing an order to receive.
	 * @param processName
	 * @return Set of PoCodeDTO
	 */
	@Query("select new com.avc.mis.beta.dto.values.PoCodeDTO("
			+ "po_code.id, po_code.code, ct.code, ct.suffix, s.name) "
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
//						+ "left join sf.unitAmount unit "
							+ "left join UOM uom "
								+ "on uom.fromUnit = ri.measureUnit and uom.toUnit = units.measureUnit "
		+ "where "
			+ "t.processName = :processName "
			+ "and (lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.PENDING "
				+ "or lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
		+ "group by oi, units "
		+ "having coalesce("
			+ "SUM("
				+ "CASE "
					+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN null  "
					+ "ELSE (sf.unitAmount * coalesce(sf.numberUnits, 0) * uom.multiplicand / uom.divisor) "
				+ "END), "
			+ "0) < units.amount ")
	Set<PoCodeDTO> findOpenPoCodeByType(ProcessName processName);
	
	/**
	 * Gets po codes for given processes that are in one of the given process statuses
	 * @param processNames
	 * @param statuses
	 * @return Set of PoCodeDTO
	 */
	@Query("select new com.avc.mis.beta.dto.values.PoCodeDTO("
			+ "po_code.id, po_code.code, c.code, c.suffix, s.name) "
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
	 * Gets set of All PoCodes that have item/s currently in available inventory 
	 * and contain the given item and belong to given supply group.
	 * Items are considered available inventory if the producing process status is final 
	 * and it's not completely used by another using process where the using process isn't cancelled.
	 * @param supplyGroup constrain to only this supply group, if null than any.
	 * @param itemId constrain to only this item, if null than any.
	 * @return Set of PoCodeDTO
	 */
	@Query("select new com.avc.mis.beta.dto.values.PoCodeDTO("
			+ "poCode.id, poCode.code, ct.code, ct.suffix, s.name) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
			+ "join pi.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
				+ "join p.lifeCycle lc "
			+ "join pi.allStorages sf "
				+ "join sf.group sf_group "
					+ "join sf_group.process sf_p "
						+ "join sf_p.lifeCycle sf_lc "
				+ "left join sf.usedItems ui "
					+ "left join ui.group used_g "
						+ "left join used_g.process used_p "
							+ "left join used_p.lifeCycle used_lc "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and sf_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null)  "
			+ "and (:checkProductionUses = false or item.productionUse in :productionUses)  "
			+ "and (item.id = :itemId or :itemId is null)  "
		+ "group by sf, sf.numberUnits "
//		+ "having (sf.numberUnits > sum(coalesce(ui.numberUsedUnits, 0))) "
		+ "having sf.numberUnits > "
			+ "SUM("
				+ "(CASE "
					+ "WHEN (ui IS NOT null AND used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
						+ "THEN ui.numberUnits "
					+ "ELSE 0 "
				+ "END)"
			+ " ) ")
	Set<PoCodeDTO> findAvailableInventoryPoCodeByType(boolean checkProductionUses, ProductionUse[] productionUses, ItemGroup itemGroup, Integer itemId);

	@Query("select new com.avc.mis.beta.dto.values.PoCodeDTO("
			+ "po_code.id, po_code.code, c.code, c.suffix, s.name) "
		+ "from Receipt r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType c "
				+ "join po_code.supplier s "
			+ "join r.processType t "
		+ "where t.processName in ?1 "
		+ "order by po_code.id desc ")
	List<PoCodeDTO> findReceivedPoCodeByTypes(ProcessName[] processNames);

	//will also give old (history) po_codes
	@Query("select distinct new com.avc.mis.beta.dto.values.PoCodeDTO("
			+ "po_code.id, po_code.code, c.code, c.suffix, s.name) "
		+ "from PoCode po_code "
				+ "join po_code.contractType c "
				+ "join po_code.supplier s "
//			+ "join p.processType t "
//		+ "where t.processName in ?1 "
		+ "order by po_code.id desc ")
	List<PoCodeDTO> findAllPoCodeDTOs();

	@Query("select new com.avc.mis.beta.dto.values.PoCodeDTO("
			+ "po_code.id, po_code.code, c.code, c.suffix, s.name) "
		+ "from PoCode po_code "
			+ "join po_code.contractType c "
			+ "join po_code.supplier s "
		+ "where (po_code.id = :poCodeId or :poCodeId is null) "
			+ "and (po_code.processes is empty "
				+ "or not exists ("
					+ "select p_2 "
					+ "from po_code.processes p_2 "
						+ "left join PO po "
							+ "on p_2 = po "
						+ "left join Receipt r "
							+ "on p_2 = r "
						+ "join p_2.lifeCycle lc_2 "
					+ "where "
						+ "(po is not null or r is not null) "
						+ "and lc_2.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
				+ ")"
			+ ") "
		+ "order by po_code.id desc ")
	List<PoCodeDTO> findFreePoCodes(Integer poCodeId);

	
}
