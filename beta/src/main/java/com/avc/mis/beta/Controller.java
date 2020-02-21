/**
 * 
 */
package com.avc.mis.beta;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dao.ReferenceTables;
import com.avc.mis.beta.dao.Suppliers;
import com.avc.mis.beta.dataobjects.City;
import com.avc.mis.beta.dataobjects.CompanyPosition;
import com.avc.mis.beta.dataobjects.Country;
import com.avc.mis.beta.dataobjects.Supplier;
import com.avc.mis.beta.dataobjects.SupplyCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	public String suppliers() {
		return suppliersDao.getSuppliersList();
	}

	@RequestMapping("/supplier-details/{id}")
	public Supplier supplierDetails(@PathVariable("id") int id) {
		System.out.println(id);
		return suppliersDao.getSupplier(id);
	}
	
	@PostMapping(value="/setSupplier")
	public Supplier setSupplier(@RequestBody Supplier supplier) throws JsonMappingException, JsonProcessingException {
//		System.out.println(supplier);
//		ObjectMapper mapper = new ObjectMapper();
//		Supplier supplier = mapper.readValue(supplierJson, Supplier.class);
//		Supplier supplier = new Gson().fromJson(supplierJson, Supplier.class);
//		System.out.println(supplier);
		suppliersDao.addSupplier(supplier);
		return supplier;
	}
	
	@RequestMapping("/setup")
	public List<List> getSetup() {
		List<List> result = new ArrayList<>();
		List<City> cityholder = refeDao.getAllCities();
		result.add(cityholder);
		List<Country> countryholder = refeDao.getAllCountries();
		result.add(countryholder);
		List<SupplyCategory> Supplyholder = refeDao.getAllSupplyCategories();
		result.add(Supplyholder);
		List<CompanyPosition> Positionholder = refeDao.getAllCompanyPositions();
		result.add(Positionholder); 
//		SupplyCategory supplyCategory = new SupplyCategory();
//		supplyCategory.setName("Bags");
//		refeDao.insertSupplyCategory(supplyCategory);
//		supplyCategory.setName("Cashew");
//		refeDao.insertSupplyCategory(supplyCategory);
		return result; 
	}
	
}
