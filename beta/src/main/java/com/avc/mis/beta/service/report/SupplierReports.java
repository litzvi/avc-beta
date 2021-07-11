/**
 * 
 */
package com.avc.mis.beta.service.report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.generic.ValueObject;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.dto.view.SupplierRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.repositories.SupplierRepository;
import com.avc.mis.beta.service.report.row.SupplierQualityRow;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class SupplierReports {

	@Autowired private SupplierRepository supplierRepository;

	/**
	 * Get Table of all suppliers with partial info - id, name, emails, phones and supply categories -
	 * to show in the table.
	 * @return List of SupplierRow of all suppliers
	 */
	@Transactional(readOnly = true)
	public List<SupplierRow> getSuppliersTable() {
		
		List<SupplierRow> supplierRows = getSupplierRepository().findAllSupplierRows();
		Map<Integer, Set<String>> phones = getSupplierRepository().findAllPhoneValues()
				.collect(Collectors.groupingBy(ValueObject<String>::getId, 
						Collectors.mapping(ValueObject<String>::getValue, Collectors.toSet())));
		Map<Integer, Set<String>> emails = getSupplierRepository().findAllEmailValues()
				.collect(Collectors.groupingBy(ValueObject<String>::getId, 
						Collectors.mapping(ValueObject<String>::getValue, Collectors.toSet())));
		Map<Integer, Set<String>> categories = getSupplierRepository().findAllSupplyCategoryValues()
				.collect(Collectors.groupingBy(ValueObject<String>::getId, 
						Collectors.mapping(ValueObject<String>::getValue, Collectors.toSet())));
		supplierRows.forEach((s) -> {
			s.setPhones(phones.get(s.getContactDetailsId())); 
			s.setEmails(emails.get(s.getContactDetailsId()));
			s.setSupplyCategories(categories.get(s.getId()));
		});
		return supplierRows;
	}
	
	public List<SupplierQualityRow> getSupplierQualityLines(Integer supplierId, LocalDateTime startTime, LocalDateTime endTime) {
		List<SupplierQualityRow> rows = getSupplierRepository().findSupplierWithPos(ProcessName.CASHEW_RECEIPT, supplierId, startTime, endTime);
		Map<Integer, SupplierQualityRow> rowMap = rows.stream().collect(Collectors.toMap(SupplierQualityRow::getId, Function.identity()));
		Set<Integer> poCodeIds = rowMap.keySet();
		//get and set QC and currentAmount
		return rows;
	}
	
}
