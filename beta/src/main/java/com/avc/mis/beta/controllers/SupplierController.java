/**
 * 
 */
package com.avc.mis.beta.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.data.BankAccountDTO;
import com.avc.mis.beta.dto.data.CompanyContactDTO;
import com.avc.mis.beta.dto.data.PaymentAccountDTO;
import com.avc.mis.beta.dto.data.SupplierDTO;
import com.avc.mis.beta.dto.link.ContactDetailsDTO;
import com.avc.mis.beta.dto.values.BankBranchDTO;
import com.avc.mis.beta.dto.values.CityDTO;
import com.avc.mis.beta.dto.values.CompanyPositionDTO;
import com.avc.mis.beta.dto.values.CountryDTO;
import com.avc.mis.beta.dto.values.SupplyCategoryDTO;
import com.avc.mis.beta.dto.view.SupplierRow;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.PaymentAccount;
import com.avc.mis.beta.service.Suppliers;
import com.avc.mis.beta.service.ValueTablesReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author Zvi
 *
 */
@RestController
@RequestMapping(path = "/api/suppliers")
public class SupplierController {
	
	@Autowired
	private Suppliers suppliersDao;
	
	@Autowired
	private ValueTablesReader refeDao;

	
	@RequestMapping("/allSuppliers")
	public List<SupplierRow> suppliers() {
		return suppliersDao.getSuppliersTable();
	}

	@DeleteMapping(value ="/removeSupplier/{id}")
	public ResponseEntity<?> removeSupplier(@PathVariable("id") int id) {
		suppliersDao.removeSupplier(id);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping("/supplierDetails/{id}")
	public SupplierDTO supplierDetails(@PathVariable("id") int id) {
		return suppliersDao.getSupplier(id);
	}
	
	@PutMapping("/editMainSupplier")
	public  SupplierDTO editMainSupplier(@RequestBody SupplierDTO supplier) {
		suppliersDao.editSupplierMainInfo(supplier);
		return suppliersDao.getSupplier(supplier.getId());
	}
	
	@PutMapping("/editContactInfo/{id}")
	public SupplierDTO editContactInfo(@RequestBody ContactDetailsDTO contactDetails, @PathVariable("id") int companyId) {
		suppliersDao.editContactInfo(contactDetails, companyId);
		return suppliersDao.getSupplier(companyId);
	}
	
	@PutMapping("/editContactPersons/{id}")
	public SupplierDTO editContactPerson(@RequestBody JsonNode listChanges, @PathVariable("id") int companyId) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		for(CompanyContactDTO var: mapper.readValue((listChanges.get("added")).toString(), new TypeReference<List<CompanyContactDTO>>(){})) {
			if(StringUtils.isNoneBlank(var.getPerson().getName())){
				suppliersDao.addContactPerson(var, companyId);
			}
		}
		for(CompanyContactDTO var: mapper.readValue((listChanges.get("updated")).toString(), new TypeReference<List<CompanyContactDTO>>(){})) {
			suppliersDao.editContactPerson(var);
		}
		for(CompanyContact var: mapper.readValue((listChanges.get("removed")).toString(), new TypeReference<List<CompanyContact>>(){})) {
//			suppliersDao.removeContactPerson(var);
		}
		return suppliersDao.getSupplier(companyId);
	}
	
	@PutMapping("/editPaymentAccounts/{contactId}/{companyId}")
	public SupplierDTO editAccount(@RequestBody JsonNode listChanges, @PathVariable("contactId") int contactId, @PathVariable("companyId") int companyId) throws IOException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		for(PaymentAccountDTO var: mapper.readValue((listChanges.get("added")).toString(), new TypeReference<List<PaymentAccountDTO>>(){})) {
			BankAccountDTO bank = var.getBankAccount();
			if(!(bank.getAccountNo() == null && bank.getOwnerName() == null && bank.getBranch() == null)) {
				suppliersDao.addAccount(var, contactId);
			}
		}
		for(PaymentAccountDTO var: mapper.readValue((listChanges.get("updated")).toString(), new TypeReference<List<PaymentAccountDTO>>(){})) {
			suppliersDao.editAccount(var);
		}
		for(PaymentAccount var: mapper.readValue((listChanges.get("removed")).toString(), new TypeReference<List<PaymentAccount>>(){})) {
			suppliersDao.removeAccount(var.getId());
		}
		return suppliersDao.getSupplier(companyId);
	}
	
	@PostMapping(value="/addSupplier")
	public SupplierDTO addSupplier(@RequestBody SupplierDTO supplier) {
		Integer id = suppliersDao.addSupplier(supplier);
		return suppliersDao.getSupplier(id);
	}
	
	@RequestMapping("/getSetUpSuppliers")
	public List<Object> getSetUpSuppliers() {
		List<Object> result = new ArrayList<Object>();
		
		List<CityDTO> cityholder = refeDao.getAllCities();
		result.add(cityholder);
		List<CountryDTO> countryholder = refeDao.getAllCountries();
		result.add(countryholder);
		List<CompanyPositionDTO> Positionholder = refeDao.getAllCompanyPositions();
		result.add(Positionholder);
		List<SupplyCategoryDTO> Suplyholder = refeDao.getAllSupplyCategories();
		result.add(Suplyholder);
		List<BankBranchDTO> Branchholder = refeDao.getAllBankBranches();
		result.add(Branchholder);
		return result;
	}
}


