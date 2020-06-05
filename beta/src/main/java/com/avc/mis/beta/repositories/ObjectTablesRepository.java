/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.entities.ObjectEntityWithId;
import com.avc.mis.beta.entities.data.BankAccount;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.data.ProcessAlert;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ProcessName;
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

	@Query("select e from ProcessAlert e")
	List<ProcessAlert> findAllProcessAlerts();

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

	@Query("select new com.avc.mis.beta.dto.process.PoCodeDTO("
			+ "po_code.code, c.code, s.name) "
		+ "from PO po "
		+ "join po.poCode po_code "
			+ "join po_code.contractType c "
			+ "join po_code.supplier s "
		+ "join po.processType t "
		+ "join po.orderItems oi "
		+ "where not exists (select ri from ReceiptItem ri where ri.orderItem = oi) "
			+ "and t.processName = ?1 ")
	List<PoCodeDTO> findOpenPoCodeByType(ProcessName processName);

	@Query("select new com.avc.mis.beta.dto.process.PoCodeDTO("
			+ "po_code.code, c.code, s.name) "
		+ "from Receipt r "
		+ "join r.poCode po_code "
			+ "join po_code.contractType c "
			+ "join po_code.supplier s "
		+ "join r.processType t "
			+ "where t.processName in ?1 ")
	List<PoCodeDTO> findReceivedPoCodeByTypes(ProcessName[] processNames);

	//will also give old (history) po_codes
	@Query("select distinct new com.avc.mis.beta.dto.process.PoCodeDTO("
			+ "po_code.code, c.code, s.name) "
		+ "from ProductionProcess p "
		+ "join p.poCode po_code "
			+ "join po_code.contractType c "
			+ "join po_code.supplier s "
		+ "join p.processType t "
			+ "where t.processName in ?1 ")
	List<PoCodeDTO> findPoCodeByTypes(ProcessName[] processNames);
}
