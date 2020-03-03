/**
 * 
 */
package com.avc.mis.beta;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dao.ReferenceTables;
import com.avc.mis.beta.dao.Suppliers;
import com.avc.mis.beta.dataobjects.Supplier;
import com.avc.mis.beta.dto.SupplierDTO;
import com.avc.mis.beta.dto.SupplierRow;
import com.google.gson.Gson;

/**
 * @author Zvi
 *
 */
@RestController
public class Controller {
	
	@Autowired
	private Suppliers suppliersDao;
	
	@Autowired
	private ReferenceTables refeDao;

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@RequestMapping("/suppliers")
	public List<SupplierRow> suppliers() {
		return suppliersDao.getSuppliers();
	}

	@RequestMapping("/supplier-details/{id}")
	public SupplierDTO supplierDetails(@PathVariable("id") int id) {
		System.out.println(id);
		return suppliersDao.getSupplier(id);
	}
	
	@PostMapping(value="/setSupplier", consumes = "application/json")
	public int setSupplier(@RequestBody String supplierJson) {
		System.out.println(supplierJson);
		Supplier supplier = new Gson().fromJson(supplierJson, Supplier.class);
		//System.out.println(supplierJson);
		suppliersDao.addSupplier(supplier);
		return supplier.getId();
	}
	
	@RequestMapping("/setup")
	public List<String> getSetup() {
		List<String> result = new ArrayList<String>();
		String cityholder = refeDao.getCities();
		result.add(cityholder);
		String countryholder = refeDao.getCountries();
		result.add(countryholder);
		String Supplyholder = refeDao.getSupplyCategories();
		result.add(Supplyholder);
		String Positionholder = refeDao.getCompanyPositions();
		result.add(Positionholder);  
		return result; 
	}
	
}
