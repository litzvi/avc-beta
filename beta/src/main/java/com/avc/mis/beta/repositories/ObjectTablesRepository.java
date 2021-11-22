/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.basic.PoCodeBasicWithProductCompany;
import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.dto.codes.PoCodeDTO;
import com.avc.mis.beta.dto.codes.ShipmentCodeDTO;
import com.avc.mis.beta.dto.data.BankAccountDTO;
import com.avc.mis.beta.dto.data.PersonDTO;
import com.avc.mis.beta.entities.ObjectDataEntity;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.codes.GeneralPoCode;
import com.avc.mis.beta.entities.codes.ProductPoCode;
import com.avc.mis.beta.entities.codes.ShipmentCode;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.link.ContactDetails;
import com.avc.mis.beta.entities.link.ProcessManagement;

/**
 * @author Zvi
 *
 */
public interface ObjectTablesRepository extends BaseRepository<ObjectDataEntity> {
	
//-----------Finding all Base entities--------------
	
	@Query("select e from ProductPoCode e")
	List<ProductPoCode> findAllProductPoCodes();
	
	@Query("select e from GeneralPoCode e")
	List<GeneralPoCode> findAllGeneralPoCodes();
	
	@Query("select e from ShipmentCode e")
	List<ShipmentCode> findAllShipmentCodes();
	
	@Query("select new com.avc.mis.beta.dto.codes.ShipmentCodeDTO(e.id, e.code, port.id, port.value, port.code) "
			+ "from ShipmentCode e "
				+ "join e.portOfDischarge port "
			+ "where e.id = :id")
	ShipmentCodeDTO findShipmentCode(Integer id);


	
//-----------Finding all Object entities with active=true--------------

	@Query("select new com.avc.mis.beta.dto.data.BankAccountDTO( "
			+ "a.id, a.version, "
			+ "a.accountNo, a.ownerName, "
			+ "bb.id, bb.value, b.id, b.value) "
			+ "from BankAccount a "
				+ "join a.branch bb "
					+ "join bb.bank b "
			+ "where a.active = true")
	List<BankAccountDTO> findAllBankAccounts();

	@Query("select new com.avc.mis.beta.dto.data.PersonDTO( "
			+ "p.id, p.version, p.name) "
			+ "from Person p "
			+ "where p.active = true")
	List<PersonDTO> findAllPersons();

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
	 * @return Set of PoCodeBasic
	 */
	@Query("select new com.avc.mis.beta.dto.basic.PoCodeBasic("
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
					+ "left join ri.receivedOrderUnits rnu "
						+ "left join UOM uom "
							+ "on uom.fromUnit = rnu.measureUnit and uom.toUnit = units.measureUnit "
		+ "where po.closed = false "
			+ "and t.processName = :processName "
			+ "and (lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.PENDING "
				+ "or lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
		+ "group by oi, units "
		+ "having coalesce("
			+ "SUM("
				+ "CASE "
					+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN null  "
					+ "ELSE (rnu.amount * uom.multiplicand / uom.divisor) "
				+ "END), "
			+ "0) < units.amount ")
	Set<PoCodeBasic> findOpenPoCodeByType(ProcessName processName);
	
	/**
	 * Gets po codes for given processes that are in one of the given process statuses
	 * @param processNames
	 * @param statuses
	 * @return Set of PoCodeBasic
	 */
	@Query("select new com.avc.mis.beta.dto.basic.PoCodeBasic("
			+ "po_code.id, po_code.code, c.code, c.suffix, s.name) "
		+ "from Receipt r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType c "
				+ "join po_code.supplier s "
			+ "join r.processType t "
			+ "join r.lifeCycle lc "
		+ "where t.processName in :processNames "
			+ "and lc.processStatus in :statuses ")
	Set<PoCodeBasic> findAllPoCodeByType(ProcessName[] processNames, ProcessStatus[] statuses);
	


	//will also give old (history) po_codes
	@Query("select new com.avc.mis.beta.dto.basic.PoCodeBasicWithProductCompany("
			+ "po_code.id, po_code.code, c.code, c.suffix, s.name, pc.name) "
		+ "from BasePoCode po_code "
				+ "join po_code.contractType c "
				+ "join po_code.supplier s "
				+ "left join po_code.productCompany pc "
		+ "where type(po_code) = :clazz "
		+ "order by po_code.id desc ")
	 <T extends BasePoCode> List<PoCodeBasicWithProductCompany> findAllPoCodeBasics(Class<T> clazz);
	
	//will also give old (history) shipment codes
	@Query("select new com.avc.mis.beta.dto.basic.ShipmentCodeBasic("
			+ "s_code.id, s_code.code, port.code, port.value) "
		+ "from ShipmentCode s_code "
			+ "join s_code.portOfDischarge port "
		+ "order by s_code.id desc ")
	List<ShipmentCodeBasic> findAllShipmentCodeBasics();

	@Query("select new com.avc.mis.beta.dto.basic.PoCodeBasic("
			+ "po_code.id, po_code.code, c.code, c.suffix, s.name) "
		+ "from BasePoCode po_code "
			+ "join po_code.contractType c "
			+ "join po_code.supplier s "
		+ "where type(po_code) = :clazz "
			+ "and (po_code.id = :poCodeId or :poCodeId is null) "
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
	<T extends BasePoCode> List<PoCodeBasic> findFreePoCodes(Integer poCodeId, Class<T> clazz);

	@Query("select new com.avc.mis.beta.dto.basic.ShipmentCodeBasic("
			+ "s_code.id, s_code.code, port.code, port.value) "
		+ "from ShipmentCode s_code "
			+ "join s_code.portOfDischarge port "
		+ "where (s_code.id = :shipmentCodeId or :shipmentCodeId is null) "
			+ "and (s_code.loadings is empty "
				+ "or not exists ("
					+ "select p_2 "
					+ "from s_code.loadings p_2 "
						+ "join p_2.lifeCycle lc_2 "
					+ "where "
						+ "lc_2.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
				+ ")"
			+ ") "
		+ "order by s_code.id desc ")
	List<ShipmentCodeBasic> findFreeShipmentCodes(Integer shipmentCodeId);

	@Query("select new com.avc.mis.beta.dto.basic.PoCodeBasic("
			+ "po_code.id, po_code.code, c.code, c.suffix, s.name) "
		+ "from MixPoCode po_code "
			+ "left join po_code.contractType c "
			+ "left join po_code.supplier s "
		+ "where (po_code.id = :poCodeId or :poCodeId is null) "
			+ "and (po_code.processes is empty "
				+ "or not exists ("
					+ "select p_2 "
					+ "from po_code.processes p_2 "
						+ "join p_2.lifeCycle lc_2 "
					+ "where "
						+ "lc_2.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
				+ ")"
			+ ") "
		+ "order by po_code.id desc ")
	List<PoCodeBasic> findMixFreePoCodes(Integer poCodeId);

	@Query("select new com.avc.mis.beta.dto.codes.PoCodeDTO( "
			+ "po_code.id, po_code.code, "
			+ "s.id, s.version, s.name, "
			+ "ct.id, ct.value, ct.code, ct.currency, ct.suffix, ct.supplyGroup, "
			+ "pc.id, pc.version, pc.name) "
		+ "from BasePoCode po_code "
			+ "join po_code.supplier s "
			+ "join po_code.contractType ct "
			+ "left join po_code.productCompany pc "
		+ "where po_code.id = :poCodeId ")
	Optional<PoCodeDTO> findPoCodeById(int poCodeId);

	
}
